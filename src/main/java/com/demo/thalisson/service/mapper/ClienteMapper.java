package com.demo.thalisson.service.mapper;

import com.demo.thalisson.domain.*;
import com.demo.thalisson.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompraMapper.class })
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Mapping(target = "compra", source = "compra", qualifiedByName = "id")
    ClienteDTO toDto(Cliente s);
}
