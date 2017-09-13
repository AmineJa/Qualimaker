package org.qualimaker.repository;

import org.qualimaker.domain.Profilsfonction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Profilsfonction entity.
 */
@SuppressWarnings("unused")
public interface ProfilsfonctionRepository extends JpaRepository<Profilsfonction,Long> {

}
