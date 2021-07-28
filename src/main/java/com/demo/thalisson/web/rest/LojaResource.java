package com.demo.thalisson.web.rest;

import com.demo.thalisson.repository.LojaRepository;
import com.demo.thalisson.service.LojaService;
import com.demo.thalisson.service.dto.LojaDTO;
import com.demo.thalisson.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.demo.thalisson.domain.Loja}.
 */
@RestController
@RequestMapping("/api")
public class LojaResource {

    private final Logger log = LoggerFactory.getLogger(LojaResource.class);

    private static final String ENTITY_NAME = "loja";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LojaService lojaService;

    private final LojaRepository lojaRepository;

    public LojaResource(LojaService lojaService, LojaRepository lojaRepository) {
        this.lojaService = lojaService;
        this.lojaRepository = lojaRepository;
    }

    /**
     * {@code POST  /lojas} : Create a new loja.
     *
     * @param lojaDTO the lojaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lojaDTO, or with status {@code 400 (Bad Request)} if the loja has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lojas")
    public ResponseEntity<LojaDTO> createLoja(@Valid @RequestBody LojaDTO lojaDTO) throws URISyntaxException {
        log.debug("REST request to save Loja : {}", lojaDTO);
        if (lojaDTO.getId() != null) {
            throw new BadRequestAlertException("A new loja cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LojaDTO result = lojaService.save(lojaDTO);
        return ResponseEntity
            .created(new URI("/api/lojas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lojas/:id} : Updates an existing loja.
     *
     * @param id the id of the lojaDTO to save.
     * @param lojaDTO the lojaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lojaDTO,
     * or with status {@code 400 (Bad Request)} if the lojaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lojaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lojas/{id}")
    public ResponseEntity<LojaDTO> updateLoja(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LojaDTO lojaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Loja : {}, {}", id, lojaDTO);
        if (lojaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lojaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lojaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LojaDTO result = lojaService.save(lojaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lojaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lojas/:id} : Partial updates given fields of an existing loja, field will ignore if it is null
     *
     * @param id the id of the lojaDTO to save.
     * @param lojaDTO the lojaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lojaDTO,
     * or with status {@code 400 (Bad Request)} if the lojaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lojaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lojaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lojas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LojaDTO> partialUpdateLoja(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LojaDTO lojaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Loja partially : {}, {}", id, lojaDTO);
        if (lojaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lojaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lojaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LojaDTO> result = lojaService.partialUpdate(lojaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lojaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lojas} : get all the lojas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lojas in body.
     */
    @GetMapping("/lojas")
    public ResponseEntity<List<LojaDTO>> getAllLojas(Pageable pageable) {
        log.debug("REST request to get a page of Lojas");
        Page<LojaDTO> page = lojaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lojas/:id} : get the "id" loja.
     *
     * @param id the id of the lojaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lojaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lojas/{id}")
    public ResponseEntity<LojaDTO> getLoja(@PathVariable Long id) {
        log.debug("REST request to get Loja : {}", id);
        Optional<LojaDTO> lojaDTO = lojaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lojaDTO);
    }

    /**
     * {@code DELETE  /lojas/:id} : delete the "id" loja.
     *
     * @param id the id of the lojaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lojas/{id}")
    public ResponseEntity<Void> deleteLoja(@PathVariable Long id) {
        log.debug("REST request to delete Loja : {}", id);
        lojaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
