package com.demo.thalisson.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.demo.thalisson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LojaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LojaDTO.class);
        LojaDTO lojaDTO1 = new LojaDTO();
        lojaDTO1.setId(1L);
        LojaDTO lojaDTO2 = new LojaDTO();
        assertThat(lojaDTO1).isNotEqualTo(lojaDTO2);
        lojaDTO2.setId(lojaDTO1.getId());
        assertThat(lojaDTO1).isEqualTo(lojaDTO2);
        lojaDTO2.setId(2L);
        assertThat(lojaDTO1).isNotEqualTo(lojaDTO2);
        lojaDTO1.setId(null);
        assertThat(lojaDTO1).isNotEqualTo(lojaDTO2);
    }
}
