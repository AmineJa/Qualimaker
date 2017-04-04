package org.qualimaker.repository;

import org.qualimaker.domain.Etat;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Etat entity.
 */
@SuppressWarnings("unused")
public interface EtatRepository extends JpaRepository<Etat,Long> {

}
