package org.qualimaker.repository;

import org.qualimaker.domain.Competences;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Competences entity.
 */
@SuppressWarnings("unused")
public interface CompetencesRepository extends JpaRepository<Competences,Long> {

}
