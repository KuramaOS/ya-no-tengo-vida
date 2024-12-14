package com.aiep.proy2.web.rest;

import static com.aiep.proy2.domain.EmpleadoDepartamentoAsserts.*;
import static com.aiep.proy2.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aiep.proy2.IntegrationTest;
import com.aiep.proy2.domain.EmpleadoDepartamento;
import com.aiep.proy2.repository.EmpleadoDepartamentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmpleadoDepartamentoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmpleadoDepartamentoResourceIT {

    private static final String DEFAULT_ID_EMPLEADO = "AAAAAAAAAA";
    private static final String UPDATED_ID_EMPLEADO = "BBBBBBBBBB";

    private static final String DEFAULT_ID_DEPARTAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_ID_DEPARTAMENTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/empleado-departamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmpleadoDepartamentoRepository empleadoDepartamentoRepository;

    @Mock
    private EmpleadoDepartamentoRepository empleadoDepartamentoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpleadoDepartamentoMockMvc;

    private EmpleadoDepartamento empleadoDepartamento;

    private EmpleadoDepartamento insertedEmpleadoDepartamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmpleadoDepartamento createEntity() {
        return new EmpleadoDepartamento().idEmpleado(DEFAULT_ID_EMPLEADO).idDepartamento(DEFAULT_ID_DEPARTAMENTO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmpleadoDepartamento createUpdatedEntity() {
        return new EmpleadoDepartamento().idEmpleado(UPDATED_ID_EMPLEADO).idDepartamento(UPDATED_ID_DEPARTAMENTO);
    }

    @BeforeEach
    public void initTest() {
        empleadoDepartamento = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmpleadoDepartamento != null) {
            empleadoDepartamentoRepository.delete(insertedEmpleadoDepartamento);
            insertedEmpleadoDepartamento = null;
        }
    }

    @Test
    @Transactional
    void createEmpleadoDepartamento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmpleadoDepartamento
        var returnedEmpleadoDepartamento = om.readValue(
            restEmpleadoDepartamentoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDepartamento)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmpleadoDepartamento.class
        );

        // Validate the EmpleadoDepartamento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmpleadoDepartamentoUpdatableFieldsEquals(
            returnedEmpleadoDepartamento,
            getPersistedEmpleadoDepartamento(returnedEmpleadoDepartamento)
        );

        insertedEmpleadoDepartamento = returnedEmpleadoDepartamento;
    }

    @Test
    @Transactional
    void createEmpleadoDepartamentoWithExistingId() throws Exception {
        // Create the EmpleadoDepartamento with an existing ID
        empleadoDepartamento.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpleadoDepartamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDepartamento)))
            .andExpect(status().isBadRequest());

        // Validate the EmpleadoDepartamento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdEmpleadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleadoDepartamento.setIdEmpleado(null);

        // Create the EmpleadoDepartamento, which fails.

        restEmpleadoDepartamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDepartamento)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdDepartamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleadoDepartamento.setIdDepartamento(null);

        // Create the EmpleadoDepartamento, which fails.

        restEmpleadoDepartamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDepartamento)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmpleadoDepartamentos() throws Exception {
        // Initialize the database
        insertedEmpleadoDepartamento = empleadoDepartamentoRepository.saveAndFlush(empleadoDepartamento);

        // Get all the empleadoDepartamentoList
        restEmpleadoDepartamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleadoDepartamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].idEmpleado").value(hasItem(DEFAULT_ID_EMPLEADO)))
            .andExpect(jsonPath("$.[*].idDepartamento").value(hasItem(DEFAULT_ID_DEPARTAMENTO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpleadoDepartamentosWithEagerRelationshipsIsEnabled() throws Exception {
        when(empleadoDepartamentoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpleadoDepartamentoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(empleadoDepartamentoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpleadoDepartamentosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(empleadoDepartamentoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpleadoDepartamentoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(empleadoDepartamentoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmpleadoDepartamento() throws Exception {
        // Initialize the database
        insertedEmpleadoDepartamento = empleadoDepartamentoRepository.saveAndFlush(empleadoDepartamento);

        // Get the empleadoDepartamento
        restEmpleadoDepartamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, empleadoDepartamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empleadoDepartamento.getId().intValue()))
            .andExpect(jsonPath("$.idEmpleado").value(DEFAULT_ID_EMPLEADO))
            .andExpect(jsonPath("$.idDepartamento").value(DEFAULT_ID_DEPARTAMENTO));
    }

    @Test
    @Transactional
    void getNonExistingEmpleadoDepartamento() throws Exception {
        // Get the empleadoDepartamento
        restEmpleadoDepartamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmpleadoDepartamento() throws Exception {
        // Initialize the database
        insertedEmpleadoDepartamento = empleadoDepartamentoRepository.saveAndFlush(empleadoDepartamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleadoDepartamento
        EmpleadoDepartamento updatedEmpleadoDepartamento = empleadoDepartamentoRepository
            .findById(empleadoDepartamento.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedEmpleadoDepartamento are not directly saved in db
        em.detach(updatedEmpleadoDepartamento);
        updatedEmpleadoDepartamento.idEmpleado(UPDATED_ID_EMPLEADO).idDepartamento(UPDATED_ID_DEPARTAMENTO);

        restEmpleadoDepartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmpleadoDepartamento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmpleadoDepartamento))
            )
            .andExpect(status().isOk());

        // Validate the EmpleadoDepartamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmpleadoDepartamentoToMatchAllProperties(updatedEmpleadoDepartamento);
    }

    @Test
    @Transactional
    void putNonExistingEmpleadoDepartamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoDepartamento.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoDepartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDepartamento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDepartamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmpleadoDepartamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpleadoDepartamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoDepartamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoDepartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDepartamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmpleadoDepartamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpleadoDepartamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoDepartamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoDepartamentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDepartamento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmpleadoDepartamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpleadoDepartamentoWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleadoDepartamento = empleadoDepartamentoRepository.saveAndFlush(empleadoDepartamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleadoDepartamento using partial update
        EmpleadoDepartamento partialUpdatedEmpleadoDepartamento = new EmpleadoDepartamento();
        partialUpdatedEmpleadoDepartamento.setId(empleadoDepartamento.getId());

        restEmpleadoDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleadoDepartamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpleadoDepartamento))
            )
            .andExpect(status().isOk());

        // Validate the EmpleadoDepartamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadoDepartamentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmpleadoDepartamento, empleadoDepartamento),
            getPersistedEmpleadoDepartamento(empleadoDepartamento)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmpleadoDepartamentoWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleadoDepartamento = empleadoDepartamentoRepository.saveAndFlush(empleadoDepartamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleadoDepartamento using partial update
        EmpleadoDepartamento partialUpdatedEmpleadoDepartamento = new EmpleadoDepartamento();
        partialUpdatedEmpleadoDepartamento.setId(empleadoDepartamento.getId());

        partialUpdatedEmpleadoDepartamento.idEmpleado(UPDATED_ID_EMPLEADO).idDepartamento(UPDATED_ID_DEPARTAMENTO);

        restEmpleadoDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleadoDepartamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpleadoDepartamento))
            )
            .andExpect(status().isOk());

        // Validate the EmpleadoDepartamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadoDepartamentoUpdatableFieldsEquals(
            partialUpdatedEmpleadoDepartamento,
            getPersistedEmpleadoDepartamento(partialUpdatedEmpleadoDepartamento)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEmpleadoDepartamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoDepartamento.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empleadoDepartamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empleadoDepartamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmpleadoDepartamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpleadoDepartamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoDepartamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empleadoDepartamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmpleadoDepartamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpleadoDepartamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoDepartamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoDepartamentoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(empleadoDepartamento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmpleadoDepartamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpleadoDepartamento() throws Exception {
        // Initialize the database
        insertedEmpleadoDepartamento = empleadoDepartamentoRepository.saveAndFlush(empleadoDepartamento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the empleadoDepartamento
        restEmpleadoDepartamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, empleadoDepartamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return empleadoDepartamentoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected EmpleadoDepartamento getPersistedEmpleadoDepartamento(EmpleadoDepartamento empleadoDepartamento) {
        return empleadoDepartamentoRepository.findById(empleadoDepartamento.getId()).orElseThrow();
    }

    protected void assertPersistedEmpleadoDepartamentoToMatchAllProperties(EmpleadoDepartamento expectedEmpleadoDepartamento) {
        assertEmpleadoDepartamentoAllPropertiesEquals(
            expectedEmpleadoDepartamento,
            getPersistedEmpleadoDepartamento(expectedEmpleadoDepartamento)
        );
    }

    protected void assertPersistedEmpleadoDepartamentoToMatchUpdatableProperties(EmpleadoDepartamento expectedEmpleadoDepartamento) {
        assertEmpleadoDepartamentoAllUpdatablePropertiesEquals(
            expectedEmpleadoDepartamento,
            getPersistedEmpleadoDepartamento(expectedEmpleadoDepartamento)
        );
    }
}
