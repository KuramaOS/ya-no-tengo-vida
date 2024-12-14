package com.aiep.proy2.repository;

import com.aiep.proy2.domain.Jefe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Jefe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JefeRepository extends JpaRepository<Jefe, Long> {}
