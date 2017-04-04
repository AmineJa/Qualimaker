package org.qualimaker.repository;

import org.qualimaker.domain.Formation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Formation entity.
 */
@SuppressWarnings("unused")
public interface FormationRepository extends JpaRepository<Formation,Long> {

}
