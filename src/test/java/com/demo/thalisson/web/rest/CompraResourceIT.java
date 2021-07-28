package com.demo.thalisson.web.rest;

import static com.demo.thalisson.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.demo.thalisson.IntegrationTest;
import com.demo.thalisson.domain.Cliente;
import com.demo.thalisson.domain.Compra;
import com.demo.thalisson.domain.Loja;
import com.demo.thalisson.repository.CompraRepository;
import com.demo.thalisson.service.dto.CompraDTO;
import com.demo.thalisson.service.mapper.CompraMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CompraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompraResourceIT {

    private static final String DEFAULT_NOME_PRODUTO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_PRODUTO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CompraMapper compraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompraMockMvc;

    private Compra compra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createEntity(EntityManager em) {
        Compra compra = new Compra().nomeProduto(DEFAULT_NOME_PRODUTO).valor(DEFAULT_VALOR);
        // Add required entity
        Loja loja;
        if (TestUtil.findAll(em, Loja.class).isEmpty()) {
            loja = LojaResourceIT.createEntity(em);
            em.persist(loja);
            em.flush();
        } else {
            loja = TestUtil.findAll(em, Loja.class).get(0);
        }
        compra.getLojas().add(loja);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        compra.getClientes().add(cliente);
        return compra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createUpdatedEntity(EntityManager em) {
        Compra compra = new Compra().nomeProduto(UPDATED_NOME_PRODUTO).valor(UPDATED_VALOR);
        // Add required entity
        Loja loja;
        if (TestUtil.findAll(em, Loja.class).isEmpty()) {
            loja = LojaResourceIT.createUpdatedEntity(em);
            em.persist(loja);
            em.flush();
        } else {
            loja = TestUtil.findAll(em, Loja.class).get(0);
        }
        compra.getLojas().add(loja);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        compra.getClientes().add(cliente);
        return compra;
    }

    @BeforeEach
    public void initTest() {
        compra = createEntity(em);
    }

    @Test
    @Transactional
    void createCompra() throws Exception {
        int databaseSizeBeforeCreate = compraRepository.findAll().size();
        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);
        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isCreated());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeCreate + 1);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getNomeProduto()).isEqualTo(DEFAULT_NOME_PRODUTO);
        assertThat(testCompra.getValor()).isEqualByComparingTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void createCompraWithExistingId() throws Exception {
        // Create the Compra with an existing ID
        compra.setId(1L);
        CompraDTO compraDTO = compraMapper.toDto(compra);

        int databaseSizeBeforeCreate = compraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeProdutoIsRequired() throws Exception {
        int databaseSizeBeforeTest = compraRepository.findAll().size();
        // set the field null
        compra.setNomeProduto(null);

        // Create the Compra, which fails.
        CompraDTO compraDTO = compraMapper.toDto(compra);

        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isBadRequest());

        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = compraRepository.findAll().size();
        // set the field null
        compra.setValor(null);

        // Create the Compra, which fails.
        CompraDTO compraDTO = compraMapper.toDto(compra);

        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isBadRequest());

        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompras() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compra.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeProduto").value(hasItem(DEFAULT_NOME_PRODUTO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))));
    }

    @Test
    @Transactional
    void getCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get the compra
        restCompraMockMvc
            .perform(get(ENTITY_API_URL_ID, compra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compra.getId().intValue()))
            .andExpect(jsonPath("$.nomeProduto").value(DEFAULT_NOME_PRODUTO))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)));
    }

    @Test
    @Transactional
    void getNonExistingCompra() throws Exception {
        // Get the compra
        restCompraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra
        Compra updatedCompra = compraRepository.findById(compra.getId()).get();
        // Disconnect from session so that the updates on updatedCompra are not directly saved in db
        em.detach(updatedCompra);
        updatedCompra.nomeProduto(UPDATED_NOME_PRODUTO).valor(UPDATED_VALOR);
        CompraDTO compraDTO = compraMapper.toDto(updatedCompra);

        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getNomeProduto()).isEqualTo(UPDATED_NOME_PRODUTO);
        assertThat(testCompra.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void putNonExistingCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        partialUpdatedCompra.nomeProduto(UPDATED_NOME_PRODUTO).valor(UPDATED_VALOR);

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getNomeProduto()).isEqualTo(UPDATED_NOME_PRODUTO);
        assertThat(testCompra.getValor()).isEqualByComparingTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void fullUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        partialUpdatedCompra.nomeProduto(UPDATED_NOME_PRODUTO).valor(UPDATED_VALOR);

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getNomeProduto()).isEqualTo(UPDATED_NOME_PRODUTO);
        assertThat(testCompra.getValor()).isEqualByComparingTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void patchNonExistingCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeDelete = compraRepository.findAll().size();

        // Delete the compra
        restCompraMockMvc
            .perform(delete(ENTITY_API_URL_ID, compra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
