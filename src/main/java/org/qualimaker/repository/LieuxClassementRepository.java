package org.qualimaker.repository;

import org.qualimaker.domain.LieuxClassement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LieuxClassement entity.
 */
@SuppressWarnings("unused")
public interface LieuxClassementRepository extends JpaRepository<LieuxClassement,Long> {

}
