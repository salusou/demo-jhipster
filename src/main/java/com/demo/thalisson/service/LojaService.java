package com.demo.thalisson.service;

import com.demo.thalisson.service.dto.LojaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.demo.thalisson.domain.Loja}.
 */
public interface LojaService {
    /**
     * Save a loja.
     *
     * @param lojaDTO the entity to save.
     * @return the persisted entity.
     */
    LojaDTO save(LojaDTO lojaDTO);

    /**
     * Partially updates a loja.
     *
     * @param lojaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LojaDTO> partialUpdate(LojaDTO lojaDTO);

    /**
     * Get all the lojas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LojaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" loja.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LojaDTO> findOne(Long id);

    /**
     * Delete the "id" loja.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
