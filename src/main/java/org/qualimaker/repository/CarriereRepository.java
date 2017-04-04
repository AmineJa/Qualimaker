package org.qualimaker.repository;

import org.qualimaker.domain.Carriere;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Carriere entity.
 */
@SuppressWarnings("unused")
public interface CarriereRepository extends JpaRepository<Carriere,Long> {

}
