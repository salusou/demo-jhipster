package com.demo.thalisson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.demo.thalisson.IntegrationTest;
import com.demo.thalisson.domain.Loja;
import com.demo.thalisson.repository.LojaRepository;
import com.demo.thalisson.service.dto.LojaDTO;
import com.demo.thalisson.service.mapper.LojaMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LojaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LojaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lojas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private LojaMapper lojaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLojaMockMvc;

    private Loja loja;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loja createEntity(EntityManager em) {
        Loja loja = new Loja().nome(DEFAULT_NOME).endereco(DEFAULT_ENDERECO).telefone(DEFAULT_TELEFONE);
        return loja;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loja createUpdatedEntity(EntityManager em) {
        Loja loja = new Loja().nome(UPDATED_NOME).endereco(UPDATED_ENDERECO).telefone(UPDATED_TELEFONE);
        return loja;
    }

    @BeforeEach
    public void initTest() {
        loja = createEntity(em);
    }

    @Test
    @Transactional
    void createLoja() throws Exception {
        int databaseSizeBeforeCreate = lojaRepository.findAll().size();
        // Create the Loja
        LojaDTO lojaDTO = lojaMapper.toDto(loja);
        restLojaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lojaDTO)))
            .andExpect(status().isCreated());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeCreate + 1);
        Loja testLoja = lojaList.get(lojaList.size() - 1);
        assertThat(testLoja.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testLoja.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testLoja.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void createLojaWithExistingId() throws Exception {
        // Create the Loja with an existing ID
        loja.setId(1L);
        LojaDTO lojaDTO = lojaMapper.toDto(loja);

        int databaseSizeBeforeCreate = lojaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLojaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lojaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lojaRepository.findAll().size();
        // set the field null
        loja.setNome(null);

        // Create the Loja, which fails.
        LojaDTO lojaDTO = lojaMapper.toDto(loja);

        restLojaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lojaDTO)))
            .andExpect(status().isBadRequest());

        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = lojaRepository.findAll().size();
        // set the field null
        loja.setTelefone(null);

        // Create the Loja, which fails.
        LojaDTO lojaDTO = lojaMapper.toDto(loja);

        restLojaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lojaDTO)))
            .andExpect(status().isBadRequest());

        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLojas() throws Exception {
        // Initialize the database
        lojaRepository.saveAndFlush(loja);

        // Get all the lojaList
        restLojaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loja.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)));
    }

    @Test
    @Transactional
    void getLoja() throws Exception {
        // Initialize the database
        lojaRepository.saveAndFlush(loja);

        // Get the loja
        restLojaMockMvc
            .perform(get(ENTITY_API_URL_ID, loja.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loja.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE));
    }

    @Test
    @Transactional
    void getNonExistingLoja() throws Exception {
        // Get the loja
        restLojaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoja() throws Exception {
        // Initialize the database
        lojaRepository.saveAndFlush(loja);

        int databaseSizeBeforeUpdate = lojaRepository.findAll().size();

        // Update the loja
        Loja updatedLoja = lojaRepository.findById(loja.getId()).get();
        // Disconnect from session so that the updates on updatedLoja are not directly saved in db
        em.detach(updatedLoja);
        updatedLoja.nome(UPDATED_NOME).endereco(UPDATED_ENDERECO).telefone(UPDATED_TELEFONE);
        LojaDTO lojaDTO = lojaMapper.toDto(updatedLoja);

        restLojaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lojaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lojaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeUpdate);
        Loja testLoja = lojaList.get(lojaList.size() - 1);
        assertThat(testLoja.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLoja.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testLoja.getTelefone()).isEqualTo(UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void putNonExistingLoja() throws Exception {
        int databaseSizeBeforeUpdate = lojaRepository.findAll().size();
        loja.setId(count.incrementAndGet());

        // Create the Loja
        LojaDTO lojaDTO = lojaMapper.toDto(loja);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLojaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lojaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lojaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoja() throws Exception {
        int databaseSizeBeforeUpdate = lojaRepository.findAll().size();
        loja.setId(count.incrementAndGet());

        // Create the Loja
        LojaDTO lojaDTO = lojaMapper.toDto(loja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLojaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lojaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoja() throws Exception {
        int databaseSizeBeforeUpdate = lojaRepository.findAll().size();
        loja.setId(count.incrementAndGet());

        // Create the Loja
        LojaDTO lojaDTO = lojaMapper.toDto(loja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLojaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lojaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLojaWithPatch() throws Exception {
        // Initialize the database
        lojaRepository.saveAndFlush(loja);

        int databaseSizeBeforeUpdate = lojaRepository.findAll().size();

        // Update the loja using partial update
        Loja partialUpdatedLoja = new Loja();
        partialUpdatedLoja.setId(loja.getId());

        partialUpdatedLoja.nome(UPDATED_NOME);

        restLojaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoja.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoja))
            )
            .andExpect(status().isOk());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeUpdate);
        Loja testLoja = lojaList.get(lojaList.size() - 1);
        assertThat(testLoja.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLoja.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testLoja.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void fullUpdateLojaWithPatch() throws Exception {
        // Initialize the database
        lojaRepository.saveAndFlush(loja);

        int databaseSizeBeforeUpdate = lojaRepository.findAll().size();

        // Update the loja using partial update
        Loja partialUpdatedLoja = new Loja();
        partialUpdatedLoja.setId(loja.getId());

        partialUpdatedLoja.nome(UPDATED_NOME).endereco(UPDATED_ENDERECO).telefone(UPDATED_TELEFONE);

        restLojaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoja.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoja))
            )
            .andExpect(status().isOk());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeUpdate);
        Loja testLoja = lojaList.get(lojaList.size() - 1);
        assertThat(testLoja.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLoja.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testLoja.getTelefone()).isEqualTo(UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void patchNonExistingLoja() throws Exception {
        int databaseSizeBeforeUpdate = lojaRepository.findAll().size();
        loja.setId(count.incrementAndGet());

        // Create the Loja
        LojaDTO lojaDTO = lojaMapper.toDto(loja);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLojaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lojaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lojaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoja() throws Exception {
        int databaseSizeBeforeUpdate = lojaRepository.findAll().size();
        loja.setId(count.incrementAndGet());

        // Create the Loja
        LojaDTO lojaDTO = lojaMapper.toDto(loja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLojaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lojaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoja() throws Exception {
        int databaseSizeBeforeUpdate = lojaRepository.findAll().size();
        loja.setId(count.incrementAndGet());

        // Create the Loja
        LojaDTO lojaDTO = lojaMapper.toDto(loja);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLojaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lojaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loja in the database
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoja() throws Exception {
        // Initialize the database
        lojaRepository.saveAndFlush(loja);

        int databaseSizeBeforeDelete = lojaRepository.findAll().size();

        // Delete the loja
        restLojaMockMvc
            .perform(delete(ENTITY_API_URL_ID, loja.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Loja> lojaList = lojaRepository.findAll();
        assertThat(lojaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
