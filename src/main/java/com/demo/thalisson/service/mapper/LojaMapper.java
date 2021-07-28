package com.demo.thalisson.service.mapper;

import com.demo.thalisson.domain.*;
import com.demo.thalisson.service.dto.LojaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Loja} and its DTO {@link LojaDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompraMapper.class })
public interface LojaMapper extends EntityMapper<LojaDTO, Loja> {
    @Mapping(target = "compra", source = "compra", qualifiedByName = "id")
    LojaDTO toDto(Loja s);
}
