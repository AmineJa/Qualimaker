package org.qualimaker.repository;

import org.qualimaker.domain.Jour;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Jour entity.
 */
@SuppressWarnings("unused")
public interface JourRepository extends JpaRepository<Jour,Long> {

}
