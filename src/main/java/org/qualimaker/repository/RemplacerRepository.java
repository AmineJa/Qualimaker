package org.qualimaker.repository;

import org.qualimaker.domain.Remplacer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Remplacer entity.
 */
@SuppressWarnings("unused")
public interface RemplacerRepository extends JpaRepository<Remplacer,Long> {

}
