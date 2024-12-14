package com.aiep.proy2.domain;

import static com.aiep.proy2.domain.DepartamentoJefeTestSamples.*;
import static com.aiep.proy2.domain.DepartamentoTestSamples.*;
import static com.aiep.proy2.domain.JefeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aiep.proy2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepartamentoJefeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepartamentoJefe.class);
        DepartamentoJefe departamentoJefe1 = getDepartamentoJefeSample1();
        DepartamentoJefe departamentoJefe2 = new DepartamentoJefe();
        assertThat(departamentoJefe1).isNotEqualTo(departamentoJefe2);

        departamentoJefe2.setId(departamentoJefe1.getId());
        assertThat(departamentoJefe1).isEqualTo(departamentoJefe2);

        departamentoJefe2 = getDepartamentoJefeSample2();
        assertThat(departamentoJefe1).isNotEqualTo(departamentoJefe2);
    }

    @Test
    void departamentoTest() {
        DepartamentoJefe departamentoJefe = getDepartamentoJefeRandomSampleGenerator();
        Departamento departamentoBack = getDepartamentoRandomSampleGenerator();

        departamentoJefe.setDepartamento(departamentoBack);
        assertThat(departamentoJefe.getDepartamento()).isEqualTo(departamentoBack);

        departamentoJefe.departamento(null);
        assertThat(departamentoJefe.getDepartamento()).isNull();
    }

    @Test
    void jefeTest() {
        DepartamentoJefe departamentoJefe = getDepartamentoJefeRandomSampleGenerator();
        Jefe jefeBack = getJefeRandomSampleGenerator();

        departamentoJefe.setJefe(jefeBack);
        assertThat(departamentoJefe.getJefe()).isEqualTo(jefeBack);

        departamentoJefe.jefe(null);
        assertThat(departamentoJefe.getJefe()).isNull();
    }
}
