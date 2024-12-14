package com.aiep.proy2.web.rest;

import com.aiep.proy2.domain.EmpleadoDepartamento;
import com.aiep.proy2.repository.EmpleadoDepartamentoRepository;
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
 * REST controller for managing {@link com.aiep.proy2.domain.EmpleadoDepartamento}.
 */
@RestController
@RequestMapping("/api/empleado-departamentos")
@Transactional
public class EmpleadoDepartamentoResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmpleadoDepartamentoResource.class);

    private static final String ENTITY_NAME = "empleadoDepartamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpleadoDepartamentoRepository empleadoDepartamentoRepository;

    public EmpleadoDepartamentoResource(EmpleadoDepartamentoRepository empleadoDepartamentoRepository) {
        this.empleadoDepartamentoRepository = empleadoDepartamentoRepository;
    }

    /**
     * {@code POST  /empleado-departamentos} : Create a new empleadoDepartamento.
     *
     * @param empleadoDepartamento the empleadoDepartamento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empleadoDepartamento, or with status {@code 400 (Bad Request)} if the empleadoDepartamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmpleadoDepartamento> createEmpleadoDepartamento(@Valid @RequestBody EmpleadoDepartamento empleadoDepartamento)
        throws URISyntaxException {
        LOG.debug("REST request to save EmpleadoDepartamento : {}", empleadoDepartamento);
        if (empleadoDepartamento.getId() != null) {
            throw new BadRequestAlertException("A new empleadoDepartamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        empleadoDepartamento = empleadoDepartamentoRepository.save(empleadoDepartamento);
        return ResponseEntity.created(new URI("/api/empleado-departamentos/" + empleadoDepartamento.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, empleadoDepartamento.getId().toString()))
            .body(empleadoDepartamento);
    }

    /**
     * {@code PUT  /empleado-departamentos/:id} : Updates an existing empleadoDepartamento.
     *
     * @param id the id of the empleadoDepartamento to save.
     * @param empleadoDepartamento the empleadoDepartamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleadoDepartamento,
     * or with status {@code 400 (Bad Request)} if the empleadoDepartamento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empleadoDepartamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDepartamento> updateEmpleadoDepartamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmpleadoDepartamento empleadoDepartamento
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmpleadoDepartamento : {}, {}", id, empleadoDepartamento);
        if (empleadoDepartamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleadoDepartamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empleadoDepartamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        empleadoDepartamento = empleadoDepartamentoRepository.save(empleadoDepartamento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, empleadoDepartamento.getId().toString()))
            .body(empleadoDepartamento);
    }

    /**
     * {@code PATCH  /empleado-departamentos/:id} : Partial updates given fields of an existing empleadoDepartamento, field will ignore if it is null
     *
     * @param id the id of the empleadoDepartamento to save.
     * @param empleadoDepartamento the empleadoDepartamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleadoDepartamento,
     * or with status {@code 400 (Bad Request)} if the empleadoDepartamento is not valid,
     * or with status {@code 404 (Not Found)} if the empleadoDepartamento is not found,
     * or with status {@code 500 (Internal Server Error)} if the empleadoDepartamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmpleadoDepartamento> partialUpdateEmpleadoDepartamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmpleadoDepartamento empleadoDepartamento
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmpleadoDepartamento partially : {}, {}", id, empleadoDepartamento);
        if (empleadoDepartamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleadoDepartamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empleadoDepartamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmpleadoDepartamento> result = empleadoDepartamentoRepository
            .findById(empleadoDepartamento.getId())
            .map(existingEmpleadoDepartamento -> {
                if (empleadoDepartamento.getIdEmpleado() != null) {
                    existingEmpleadoDepartamento.setIdEmpleado(empleadoDepartamento.getIdEmpleado());
                }
                if (empleadoDepartamento.getIdDepartamento() != null) {
                    existingEmpleadoDepartamento.setIdDepartamento(empleadoDepartamento.getIdDepartamento());
                }

                return existingEmpleadoDepartamento;
            })
            .map(empleadoDepartamentoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, empleadoDepartamento.getId().toString())
        );
    }

    /**
     * {@code GET  /empleado-departamentos} : get all the empleadoDepartamentos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empleadoDepartamentos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmpleadoDepartamento>> getAllEmpleadoDepartamentos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of EmpleadoDepartamentos");
        Page<EmpleadoDepartamento> page;
        if (eagerload) {
            page = empleadoDepartamentoRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = empleadoDepartamentoRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /empleado-departamentos/:id} : get the "id" empleadoDepartamento.
     *
     * @param id the id of the empleadoDepartamento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empleadoDepartamento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDepartamento> getEmpleadoDepartamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmpleadoDepartamento : {}", id);
        Optional<EmpleadoDepartamento> empleadoDepartamento = empleadoDepartamentoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(empleadoDepartamento);
    }

    /**
     * {@code DELETE  /empleado-departamentos/:id} : delete the "id" empleadoDepartamento.
     *
     * @param id the id of the empleadoDepartamento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleadoDepartamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmpleadoDepartamento : {}", id);
        empleadoDepartamentoRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
