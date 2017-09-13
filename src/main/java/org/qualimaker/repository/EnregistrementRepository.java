package org.qualimaker.repository;

import org.qualimaker.domain.Enregistrement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Enregistrement entity.
 */
@SuppressWarnings("unused")
public interface EnregistrementRepository extends JpaRepository<Enregistrement,Long> {

}
