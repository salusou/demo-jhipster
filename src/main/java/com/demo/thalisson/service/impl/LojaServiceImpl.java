package com.demo.thalisson.service.impl;

import com.demo.thalisson.domain.Loja;
import com.demo.thalisson.repository.LojaRepository;
import com.demo.thalisson.service.LojaService;
import com.demo.thalisson.service.dto.LojaDTO;
import com.demo.thalisson.service.mapper.LojaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Loja}.
 */
@Service
@Transactional
public class LojaServiceImpl implements LojaService {

    private final Logger log = LoggerFactory.getLogger(LojaServiceImpl.class);

    private final LojaRepository lojaRepository;

    private final LojaMapper lojaMapper;

    public LojaServiceImpl(LojaRepository lojaRepository, LojaMapper lojaMapper) {
        this.lojaRepository = lojaRepository;
        this.lojaMapper = lojaMapper;
    }

    @Override
    public LojaDTO save(LojaDTO lojaDTO) {
        log.debug("Request to save Loja : {}", lojaDTO);
        Loja loja = lojaMapper.toEntity(lojaDTO);
        loja = lojaRepository.save(loja);
        return lojaMapper.toDto(loja);
    }

    @Override
    public Optional<LojaDTO> partialUpdate(LojaDTO lojaDTO) {
        log.debug("Request to partially update Loja : {}", lojaDTO);

        return lojaRepository
            .findById(lojaDTO.getId())
            .map(
                existingLoja -> {
                    lojaMapper.partialUpdate(existingLoja, lojaDTO);

                    return existingLoja;
                }
            )
            .map(lojaRepository::save)
            .map(lojaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LojaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lojas");
        return lojaRepository.findAll(pageable).map(lojaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LojaDTO> findOne(Long id) {
        log.debug("Request to get Loja : {}", id);
        return lojaRepository.findById(id).map(lojaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Loja : {}", id);
        lojaRepository.deleteById(id);
    }
}
