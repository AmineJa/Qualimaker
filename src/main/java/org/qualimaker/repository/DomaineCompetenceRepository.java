package org.qualimaker.repository;

import org.qualimaker.domain.DomaineCompetence;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DomaineCompetence entity.
 */
@SuppressWarnings("unused")
public interface DomaineCompetenceRepository extends JpaRepository<DomaineCompetence,Long> {

}
