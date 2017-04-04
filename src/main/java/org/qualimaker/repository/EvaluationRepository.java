package org.qualimaker.repository;

import org.qualimaker.domain.Evaluation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Evaluation entity.
 */
@SuppressWarnings("unused")
public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {

}
