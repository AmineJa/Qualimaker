package org.qualimaker.repository;

import org.qualimaker.domain.Abscence;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Abscence entity.
 */
@SuppressWarnings("unused")
public interface AbscenceRepository extends JpaRepository<Abscence,Long> {

}
