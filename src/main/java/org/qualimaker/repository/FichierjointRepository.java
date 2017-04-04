package org.qualimaker.repository;

import org.qualimaker.domain.Fichierjoint;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fichierjoint entity.
 */
@SuppressWarnings("unused")
public interface FichierjointRepository extends JpaRepository<Fichierjoint,Long> {

}
