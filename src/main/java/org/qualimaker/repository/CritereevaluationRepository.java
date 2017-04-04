package org.qualimaker.repository;

import org.qualimaker.domain.Critereevaluation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Critereevaluation entity.
 */
@SuppressWarnings("unused")
public interface CritereevaluationRepository extends JpaRepository<Critereevaluation,Long> {

}
