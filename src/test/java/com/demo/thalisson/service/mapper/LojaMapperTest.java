package com.demo.thalisson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LojaMapperTest {

    private LojaMapper lojaMapper;

    @BeforeEach
    public void setUp() {
        lojaMapper = new LojaMapperImpl();
    }
}
