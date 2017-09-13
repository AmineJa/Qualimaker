package org.qualimaker.repository;

import org.qualimaker.domain.Origine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Origine entity.
 */
@SuppressWarnings("unused")
public interface OrigineRepository extends JpaRepository<Origine,Long> {

}
