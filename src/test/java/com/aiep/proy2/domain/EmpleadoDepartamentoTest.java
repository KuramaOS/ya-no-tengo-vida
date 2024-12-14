package com.aiep.proy2.domain;

import static com.aiep.proy2.domain.DepartamentoTestSamples.*;
import static com.aiep.proy2.domain.EmpleadoDepartamentoTestSamples.*;
import static com.aiep.proy2.domain.EmpleadoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aiep.proy2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpleadoDepartamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmpleadoDepartamento.class);
        EmpleadoDepartamento empleadoDepartamento1 = getEmpleadoDepartamentoSample1();
        EmpleadoDepartamento empleadoDepartamento2 = new EmpleadoDepartamento();
        assertThat(empleadoDepartamento1).isNotEqualTo(empleadoDepartamento2);

        empleadoDepartamento2.setId(empleadoDepartamento1.getId());
        assertThat(empleadoDepartamento1).isEqualTo(empleadoDepartamento2);

        empleadoDepartamento2 = getEmpleadoDepartamentoSample2();
        assertThat(empleadoDepartamento1).isNotEqualTo(empleadoDepartamento2);
    }

    @Test
    void empleadoTest() {
        EmpleadoDepartamento empleadoDepartamento = getEmpleadoDepartamentoRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        empleadoDepartamento.setEmpleado(empleadoBack);
        assertThat(empleadoDepartamento.getEmpleado()).isEqualTo(empleadoBack);

        empleadoDepartamento.empleado(null);
        assertThat(empleadoDepartamento.getEmpleado()).isNull();
    }

    @Test
    void departamentoTest() {
        EmpleadoDepartamento empleadoDepartamento = getEmpleadoDepartamentoRandomSampleGenerator();
        Departamento departamentoBack = getDepartamentoRandomSampleGenerator();

        empleadoDepartamento.setDepartamento(departamentoBack);
        assertThat(empleadoDepartamento.getDepartamento()).isEqualTo(departamentoBack);

        empleadoDepartamento.departamento(null);
        assertThat(empleadoDepartamento.getDepartamento()).isNull();
    }
}
