package org.qualimaker.repository;

import org.qualimaker.domain.TypeContrat;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeContrat entity.
 */
@SuppressWarnings("unused")
public interface TypeContratRepository extends JpaRepository<TypeContrat,Long> {

}
