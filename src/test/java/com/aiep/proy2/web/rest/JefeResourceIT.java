package com.aiep.proy2.web.rest;

import static com.aiep.proy2.domain.JefeAsserts.*;
import static com.aiep.proy2.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aiep.proy2.IntegrationTest;
import com.aiep.proy2.domain.Jefe;
import com.aiep.proy2.repository.JefeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JefeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JefeResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/jefes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JefeRepository jefeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJefeMockMvc;

    private Jefe jefe;

    private Jefe insertedJefe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jefe createEntity() {
        return new Jefe().nombre(DEFAULT_NOMBRE).telefono(DEFAULT_TELEFONO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jefe createUpdatedEntity() {
        return new Jefe().nombre(UPDATED_NOMBRE).telefono(UPDATED_TELEFONO);
    }

    @BeforeEach
    public void initTest() {
        jefe = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedJefe != null) {
            jefeRepository.delete(insertedJefe);
            insertedJefe = null;
        }
    }

    @Test
    @Transactional
    void createJefe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Jefe
        var returnedJefe = om.readValue(
            restJefeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jefe)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Jefe.class
        );

        // Validate the Jefe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertJefeUpdatableFieldsEquals(returnedJefe, getPersistedJefe(returnedJefe));

        insertedJefe = returnedJefe;
    }

    @Test
    @Transactional
    void createJefeWithExistingId() throws Exception {
        // Create the Jefe with an existing ID
        jefe.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJefeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jefe)))
            .andExpect(status().isBadRequest());

        // Validate the Jefe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jefe.setNombre(null);

        // Create the Jefe, which fails.

        restJefeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jefe)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jefe.setTelefono(null);

        // Create the Jefe, which fails.

        restJefeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jefe)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJefes() throws Exception {
        // Initialize the database
        insertedJefe = jefeRepository.saveAndFlush(jefe);

        // Get all the jefeList
        restJefeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jefe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }

    @Test
    @Transactional
    void getJefe() throws Exception {
        // Initialize the database
        insertedJefe = jefeRepository.saveAndFlush(jefe);

        // Get the jefe
        restJefeMockMvc
            .perform(get(ENTITY_API_URL_ID, jefe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jefe.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    void getNonExistingJefe() throws Exception {
        // Get the jefe
        restJefeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJefe() throws Exception {
        // Initialize the database
        insertedJefe = jefeRepository.saveAndFlush(jefe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jefe
        Jefe updatedJefe = jefeRepository.findById(jefe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJefe are not directly saved in db
        em.detach(updatedJefe);
        updatedJefe.nombre(UPDATED_NOMBRE).telefono(UPDATED_TELEFONO);

        restJefeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJefe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedJefe))
            )
            .andExpect(status().isOk());

        // Validate the Jefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJefeToMatchAllProperties(updatedJefe);
    }

    @Test
    @Transactional
    void putNonExistingJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJefeMockMvc
            .perform(put(ENTITY_API_URL_ID, jefe.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jefe)))
            .andExpect(status().isBadRequest());

        // Validate the Jefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJefeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jefe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJefeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jefe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Jefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJefeWithPatch() throws Exception {
        // Initialize the database
        insertedJefe = jefeRepository.saveAndFlush(jefe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jefe using partial update
        Jefe partialUpdatedJefe = new Jefe();
        partialUpdatedJefe.setId(jefe.getId());

        partialUpdatedJefe.nombre(UPDATED_NOMBRE);

        restJefeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJefe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJefe))
            )
            .andExpect(status().isOk());

        // Validate the Jefe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJefeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedJefe, jefe), getPersistedJefe(jefe));
    }

    @Test
    @Transactional
    void fullUpdateJefeWithPatch() throws Exception {
        // Initialize the database
        insertedJefe = jefeRepository.saveAndFlush(jefe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jefe using partial update
        Jefe partialUpdatedJefe = new Jefe();
        partialUpdatedJefe.setId(jefe.getId());

        partialUpdatedJefe.nombre(UPDATED_NOMBRE).telefono(UPDATED_TELEFONO);

        restJefeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJefe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJefe))
            )
            .andExpect(status().isOk());

        // Validate the Jefe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJefeUpdatableFieldsEquals(partialUpdatedJefe, getPersistedJefe(partialUpdatedJefe));
    }

    @Test
    @Transactional
    void patchNonExistingJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJefeMockMvc
            .perform(patch(ENTITY_API_URL_ID, jefe.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(jefe)))
            .andExpect(status().isBadRequest());

        // Validate the Jefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJefeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jefe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJefe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJefeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(jefe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Jefe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJefe() throws Exception {
        // Initialize the database
        insertedJefe = jefeRepository.saveAndFlush(jefe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the jefe
        restJefeMockMvc
            .perform(delete(ENTITY_API_URL_ID, jefe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return jefeRepository.count();
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

    protected Jefe getPersistedJefe(Jefe jefe) {
        return jefeRepository.findById(jefe.getId()).orElseThrow();
    }

    protected void assertPersistedJefeToMatchAllProperties(Jefe expectedJefe) {
        assertJefeAllPropertiesEquals(expectedJefe, getPersistedJefe(expectedJefe));
    }

    protected void assertPersistedJefeToMatchUpdatableProperties(Jefe expectedJefe) {
        assertJefeAllUpdatablePropertiesEquals(expectedJefe, getPersistedJefe(expectedJefe));
    }
}
