package com.demo.thalisson.service.mapper;

import com.demo.thalisson.domain.*;
import com.demo.thalisson.service.dto.CompraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compra} and its DTO {@link CompraDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompraMapper extends EntityMapper<CompraDTO, Compra> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompraDTO toDtoId(Compra compra);
}
