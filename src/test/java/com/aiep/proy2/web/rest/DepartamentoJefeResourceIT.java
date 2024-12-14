package com.aiep.proy2.web.rest;

import static com.aiep.proy2.domain.DepartamentoJefeAsserts.*;
import static com.aiep.proy2.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aiep.proy2.IntegrationTest;
import com.aiep.proy2.domain.DepartamentoJefe;
import com.aiep.proy2.repository.DepartamentoJefeRepository;
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
 * Integration tests for the {@link DepartamentoJefeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepartamentoJefeResourceIT {

    private static final String DEFAULT_ID_DEPARTAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_ID_DEPARTAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_ID_JEFE = "AAAAAAAAAA";
    private static final String UPDATED_ID_JEFE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departamento-jefes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepartamentoJefeRepository departamentoJefeRepository;

    @Mock
    private DepartamentoJefeRepository departamentoJefeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartamentoJefeMockMvc;

    private DepartamentoJefe departamentoJefe;

    private DepartamentoJefe insertedDepartamentoJefe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartamentoJefe createEntity() {
        return new DepartamentoJefe().idDepartamento(DEFAULT_ID_DEPARTAMENTO).idJefe(DEFAULT_ID_JEFE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartamentoJefe createUpdatedEntity() {
        return new DepartamentoJefe().idDepartamento(UPDATED_ID_DEPARTAMENTO).idJefe(UPDATED_ID_JEFE);
    }

    @BeforeEach
    public void initTest() {
        departamentoJefe = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDepartamentoJefe != null) {
            departamentoJefeRepository.delete(insertedDepartamentoJefe);
            insertedDepartamentoJefe = null;
        }
    }

    @Test
    @Transactional
    void createDepartamentoJefe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DepartamentoJefe
        var returnedDepartamentoJefe = om.readValue(
            restDepartamentoJefeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departamentoJefe)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DepartamentoJefe.class
        );

        // Validate the DepartamentoJefe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDepartamentoJefeUpdatableFieldsEquals(returnedDepartamentoJefe, getPersistedDepartamentoJefe(returnedDepartamentoJefe));

        insertedDepartamentoJefe = returnedDepartamentoJefe;
    }

    @Test
    @Transactional
    void createDepartamentoJefeWithExistingId() throws Exception {
        // Create the DepartamentoJefe with an existing ID
        departamentoJefe.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartamentoJefeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departamentoJefe)))
            .andExpect(status().isBadRequest());

        // Validate the DepartamentoJefe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdDepartamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        departamentoJefe.setIdDepartamento(null);

        // Create the DepartamentoJefe, which fails.

        restDepartamentoJefeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departamentoJefe)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdJefeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        departamentoJefe.setIdJefe(null);

        // Create the DepartamentoJefe, which fails.

        restDepartamentoJefeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departamentoJefe)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepartamentoJefes() throws Exception {
        // Initialize the database
        insertedDepartamentoJefe = departamentoJefeRepository.saveAndFlush(departamentoJefe);

        // Get all the departamentoJefeList
        restDepartamentoJefeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departamentoJefe.getId().intValue())))
            .andExpect(jsonPath("$.[*].idDepartamento").value(hasItem(DEFAULT_ID_DEPARTAMENTO)))
            .andExpect(jsonPath("$.[*].idJefe").value(hasItem(DEFAULT_ID_JEFE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartamentoJefesWithEagerRelationshipsIsEnabled() throws Exception {
        when(departamentoJefeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartamentoJefeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(departamentoJefeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartamentoJefesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(departamentoJefeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartamentoJefeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(departamentoJefeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDepartamentoJefe() throws Exception {
        // Initialize the database
        insertedDepartamentoJefe = departamentoJefeRepository.saveAndFlush(departamentoJefe);

        // Get the departamentoJefe
        restDepartamentoJefeMockMvc
            .perform(get(ENTITY_API_URL_ID, departamentoJefe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departamentoJefe.getId().intValue()))
            .andExpect(jsonPath("$.idDepartamento").value(DEFAULT_ID_DEPARTAMENTO))
            .andExpect(jsonPath("$.idJefe").value(DEFAULT_ID_JEFE));
    }

    @Test
    @Transactional
    void getNonExistingDepartamentoJefe() throws Exception {
        // Get the departamentoJefe
        restDepartamentoJefeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartamentoJefe() throws Exception {
        // Initialize the database
        insertedDepartamentoJefe = departamentoJefeRepository.saveAndFlush(departamentoJefe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departamentoJefe
        DepartamentoJefe updatedDepartamentoJefe = departamentoJefeRepository.findById(departamentoJefe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDepartamentoJefe are not directly saved in db
        em.detach(updatedDepartamentoJefe);
        updatedDepartamentoJefe.idDepartamento(UPDATED_ID_DEPARTAMENTO).idJefe(UPDATED_ID_JEFE);

        restDepartamentoJefeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDepartamentoJefe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDepartamentoJefe))
            )
            .andExpect(status().isOk());

        // Validate the DepartamentoJefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepartamentoJefeToMatchAllProperties(updatedDepartamentoJefe);
    }

    @Test
    @Transactional
    void putNonExistingDepartamentoJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentoJefe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartamentoJefeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departamentoJefe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departamentoJefe))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartamentoJefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartamentoJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentoJefe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoJefeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departamentoJefe))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartamentoJefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartamentoJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentoJefe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoJefeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departamentoJefe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepartamentoJefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartamentoJefeWithPatch() throws Exception {
        // Initialize the database
        insertedDepartamentoJefe = departamentoJefeRepository.saveAndFlush(departamentoJefe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departamentoJefe using partial update
        DepartamentoJefe partialUpdatedDepartamentoJefe = new DepartamentoJefe();
        partialUpdatedDepartamentoJefe.setId(departamentoJefe.getId());

        partialUpdatedDepartamentoJefe.idDepartamento(UPDATED_ID_DEPARTAMENTO).idJefe(UPDATED_ID_JEFE);

        restDepartamentoJefeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartamentoJefe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartamentoJefe))
            )
            .andExpect(status().isOk());

        // Validate the DepartamentoJefe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartamentoJefeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepartamentoJefe, departamentoJefe),
            getPersistedDepartamentoJefe(departamentoJefe)
        );
    }

    @Test
    @Transactional
    void fullUpdateDepartamentoJefeWithPatch() throws Exception {
        // Initialize the database
        insertedDepartamentoJefe = departamentoJefeRepository.saveAndFlush(departamentoJefe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departamentoJefe using partial update
        DepartamentoJefe partialUpdatedDepartamentoJefe = new DepartamentoJefe();
        partialUpdatedDepartamentoJefe.setId(departamentoJefe.getId());

        partialUpdatedDepartamentoJefe.idDepartamento(UPDATED_ID_DEPARTAMENTO).idJefe(UPDATED_ID_JEFE);

        restDepartamentoJefeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartamentoJefe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartamentoJefe))
            )
            .andExpect(status().isOk());

        // Validate the DepartamentoJefe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartamentoJefeUpdatableFieldsEquals(
            partialUpdatedDepartamentoJefe,
            getPersistedDepartamentoJefe(partialUpdatedDepartamentoJefe)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDepartamentoJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentoJefe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartamentoJefeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departamentoJefe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departamentoJefe))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartamentoJefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartamentoJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentoJefe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoJefeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departamentoJefe))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartamentoJefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartamentoJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentoJefe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoJefeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(departamentoJefe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepartamentoJefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartamentoJefe() throws Exception {
        // Initialize the database
        insertedDepartamentoJefe = departamentoJefeRepository.saveAndFlush(departamentoJefe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the departamentoJefe
        restDepartamentoJefeMockMvc
            .perform(delete(ENTITY_API_URL_ID, departamentoJefe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return departamentoJefeRepository.count();
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

    protected DepartamentoJefe getPersistedDepartamentoJefe(DepartamentoJefe departamentoJefe) {
        return departamentoJefeRepository.findById(departamentoJefe.getId()).orElseThrow();
    }

    protected void assertPersistedDepartamentoJefeToMatchAllProperties(DepartamentoJefe expectedDepartamentoJefe) {
        assertDepartamentoJefeAllPropertiesEquals(expectedDepartamentoJefe, getPersistedDepartamentoJefe(expectedDepartamentoJefe));
    }

    protected void assertPersistedDepartamentoJefeToMatchUpdatableProperties(DepartamentoJefe expectedDepartamentoJefe) {
        assertDepartamentoJefeAllUpdatablePropertiesEquals(
            expectedDepartamentoJefe,
            getPersistedDepartamentoJefe(expectedDepartamentoJefe)
        );
    }
}
