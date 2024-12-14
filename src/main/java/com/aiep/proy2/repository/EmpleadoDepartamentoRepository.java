package com.aiep.proy2.repository;

import com.aiep.proy2.domain.EmpleadoDepartamento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmpleadoDepartamento entity.
 */
@Repository
public interface EmpleadoDepartamentoRepository extends JpaRepository<EmpleadoDepartamento, Long> {
    default Optional<EmpleadoDepartamento> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EmpleadoDepartamento> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EmpleadoDepartamento> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select empleadoDepartamento from EmpleadoDepartamento empleadoDepartamento left join fetch empleadoDepartamento.empleado left join fetch empleadoDepartamento.departamento",
        countQuery = "select count(empleadoDepartamento) from EmpleadoDepartamento empleadoDepartamento"
    )
    Page<EmpleadoDepartamento> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select empleadoDepartamento from EmpleadoDepartamento empleadoDepartamento left join fetch empleadoDepartamento.empleado left join fetch empleadoDepartamento.departamento"
    )
    List<EmpleadoDepartamento> findAllWithToOneRelationships();

    @Query(
        "select empleadoDepartamento from EmpleadoDepartamento empleadoDepartamento left join fetch empleadoDepartamento.empleado left join fetch empleadoDepartamento.departamento where empleadoDepartamento.id =:id"
    )
    Optional<EmpleadoDepartamento> findOneWithToOneRelationships(@Param("id") Long id);
}
