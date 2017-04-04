package org.qualimaker.repository;

import org.qualimaker.domain.EvaluationFormation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EvaluationFormation entity.
 */
@SuppressWarnings("unused")
public interface EvaluationFormationRepository extends JpaRepository<EvaluationFormation,Long> {

}
