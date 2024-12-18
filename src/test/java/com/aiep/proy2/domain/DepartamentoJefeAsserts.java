package com.aiep.proy2.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DepartamentoJefeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDepartamentoJefeAllPropertiesEquals(DepartamentoJefe expected, DepartamentoJefe actual) {
        assertDepartamentoJefeAutoGeneratedPropertiesEquals(expected, actual);
        assertDepartamentoJefeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDepartamentoJefeAllUpdatablePropertiesEquals(DepartamentoJefe expected, DepartamentoJefe actual) {
        assertDepartamentoJefeUpdatableFieldsEquals(expected, actual);
        assertDepartamentoJefeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDepartamentoJefeAutoGeneratedPropertiesEquals(DepartamentoJefe expected, DepartamentoJefe actual) {
        assertThat(expected)
            .as("Verify DepartamentoJefe auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDepartamentoJefeUpdatableFieldsEquals(DepartamentoJefe expected, DepartamentoJefe actual) {
        assertThat(expected)
            .as("Verify DepartamentoJefe relevant properties")
            .satisfies(e -> assertThat(e.getIdDepartamento()).as("check idDepartamento").isEqualTo(actual.getIdDepartamento()))
            .satisfies(e -> assertThat(e.getIdJefe()).as("check idJefe").isEqualTo(actual.getIdJefe()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDepartamentoJefeUpdatableRelationshipsEquals(DepartamentoJefe expected, DepartamentoJefe actual) {
        assertThat(expected)
            .as("Verify DepartamentoJefe relationships")
            .satisfies(e -> assertThat(e.getDepartamento()).as("check departamento").isEqualTo(actual.getDepartamento()))
            .satisfies(e -> assertThat(e.getJefe()).as("check jefe").isEqualTo(actual.getJefe()));
    }
}
