package org.qualimaker.repository;

import org.qualimaker.domain.Calendrier;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Calendrier entity.
 */
@SuppressWarnings("unused")
public interface CalendrierRepository extends JpaRepository<Calendrier,Long> {

}
