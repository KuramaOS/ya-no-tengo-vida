package com.aiep.proy2.repository;

import com.aiep.proy2.domain.DepartamentoJefe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DepartamentoJefe entity.
 */
@Repository
public interface DepartamentoJefeRepository extends JpaRepository<DepartamentoJefe, Long> {
    default Optional<DepartamentoJefe> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DepartamentoJefe> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DepartamentoJefe> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select departamentoJefe from DepartamentoJefe departamentoJefe left join fetch departamentoJefe.departamento left join fetch departamentoJefe.jefe",
        countQuery = "select count(departamentoJefe) from DepartamentoJefe departamentoJefe"
    )
    Page<DepartamentoJefe> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select departamentoJefe from DepartamentoJefe departamentoJefe left join fetch departamentoJefe.departamento left join fetch departamentoJefe.jefe"
    )
    List<DepartamentoJefe> findAllWithToOneRelationships();

    @Query(
        "select departamentoJefe from DepartamentoJefe departamentoJefe left join fetch departamentoJefe.departamento left join fetch departamentoJefe.jefe where departamentoJefe.id =:id"
    )
    Optional<DepartamentoJefe> findOneWithToOneRelationships(@Param("id") Long id);
}
