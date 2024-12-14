package com.aiep.proy2.web.rest;

import com.aiep.proy2.domain.DepartamentoJefe;
import com.aiep.proy2.repository.DepartamentoJefeRepository;
import com.aiep.proy2.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.aiep.proy2.domain.DepartamentoJefe}.
 */
@RestController
@RequestMapping("/api/departamento-jefes")
@Transactional
public class DepartamentoJefeResource {

    private static final Logger LOG = LoggerFactory.getLogger(DepartamentoJefeResource.class);

    private static final String ENTITY_NAME = "departamentoJefe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartamentoJefeRepository departamentoJefeRepository;

    public DepartamentoJefeResource(DepartamentoJefeRepository departamentoJefeRepository) {
        this.departamentoJefeRepository = departamentoJefeRepository;
    }

    /**
     * {@code POST  /departamento-jefes} : Create a new departamentoJefe.
     *
     * @param departamentoJefe the departamentoJefe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departamentoJefe, or with status {@code 400 (Bad Request)} if the departamentoJefe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DepartamentoJefe> createDepartamentoJefe(@Valid @RequestBody DepartamentoJefe departamentoJefe)
        throws URISyntaxException {
        LOG.debug("REST request to save DepartamentoJefe : {}", departamentoJefe);
        if (departamentoJefe.getId() != null) {
            throw new BadRequestAlertException("A new departamentoJefe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        departamentoJefe = departamentoJefeRepository.save(departamentoJefe);
        return ResponseEntity.created(new URI("/api/departamento-jefes/" + departamentoJefe.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, departamentoJefe.getId().toString()))
            .body(departamentoJefe);
    }

    /**
     * {@code PUT  /departamento-jefes/:id} : Updates an existing departamentoJefe.
     *
     * @param id the id of the departamentoJefe to save.
     * @param departamentoJefe the departamentoJefe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departamentoJefe,
     * or with status {@code 400 (Bad Request)} if the departamentoJefe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departamentoJefe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoJefe> updateDepartamentoJefe(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepartamentoJefe departamentoJefe
    ) throws URISyntaxException {
        LOG.debug("REST request to update DepartamentoJefe : {}, {}", id, departamentoJefe);
        if (departamentoJefe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departamentoJefe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departamentoJefeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        departamentoJefe = departamentoJefeRepository.save(departamentoJefe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, departamentoJefe.getId().toString()))
            .body(departamentoJefe);
    }

    /**
     * {@code PATCH  /departamento-jefes/:id} : Partial updates given fields of an existing departamentoJefe, field will ignore if it is null
     *
     * @param id the id of the departamentoJefe to save.
     * @param departamentoJefe the departamentoJefe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departamentoJefe,
     * or with status {@code 400 (Bad Request)} if the departamentoJefe is not valid,
     * or with status {@code 404 (Not Found)} if the departamentoJefe is not found,
     * or with status {@code 500 (Internal Server Error)} if the departamentoJefe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepartamentoJefe> partialUpdateDepartamentoJefe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepartamentoJefe departamentoJefe
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DepartamentoJefe partially : {}, {}", id, departamentoJefe);
        if (departamentoJefe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departamentoJefe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departamentoJefeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepartamentoJefe> result = departamentoJefeRepository
            .findById(departamentoJefe.getId())
            .map(existingDepartamentoJefe -> {
                if (departamentoJefe.getIdDepartamento() != null) {
                    existingDepartamentoJefe.setIdDepartamento(departamentoJefe.getIdDepartamento());
                }
                if (departamentoJefe.getIdJefe() != null) {
                    existingDepartamentoJefe.setIdJefe(departamentoJefe.getIdJefe());
                }

                return existingDepartamentoJefe;
            })
            .map(departamentoJefeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, departamentoJefe.getId().toString())
        );
    }

    /**
     * {@code GET  /departamento-jefes} : get all the departamentoJefes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departamentoJefes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DepartamentoJefe>> getAllDepartamentoJefes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of DepartamentoJefes");
        Page<DepartamentoJefe> page;
        if (eagerload) {
            page = departamentoJefeRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = departamentoJefeRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /departamento-jefes/:id} : get the "id" departamentoJefe.
     *
     * @param id the id of the departamentoJefe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departamentoJefe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoJefe> getDepartamentoJefe(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DepartamentoJefe : {}", id);
        Optional<DepartamentoJefe> departamentoJefe = departamentoJefeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(departamentoJefe);
    }

    /**
     * {@code DELETE  /departamento-jefes/:id} : delete the "id" departamentoJefe.
     *
     * @param id the id of the departamentoJefe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamentoJefe(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DepartamentoJefe : {}", id);
        departamentoJefeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
