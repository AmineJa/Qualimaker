package org.qualimaker.repository;

import org.qualimaker.domain.Conge;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Conge entity.
 */
@SuppressWarnings("unused")
public interface CongeRepository extends JpaRepository<Conge,Long> {

}
