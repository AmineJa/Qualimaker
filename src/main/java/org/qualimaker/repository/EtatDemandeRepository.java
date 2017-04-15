package org.qualimaker.repository;

import org.qualimaker.domain.EtatDemande;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EtatDemande entity.
 */
@SuppressWarnings("unused")
public interface EtatDemandeRepository extends JpaRepository<EtatDemande,Long> {

}
