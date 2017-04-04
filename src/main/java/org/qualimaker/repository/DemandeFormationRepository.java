package org.qualimaker.repository;

import org.qualimaker.domain.DemandeFormation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DemandeFormation entity.
 */
@SuppressWarnings("unused")
public interface DemandeFormationRepository extends JpaRepository<DemandeFormation,Long> {

}
