package org.qualimaker.repository;

import org.qualimaker.domain.Natureformation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Natureformation entity.
 */
@SuppressWarnings("unused")
public interface NatureformationRepository extends JpaRepository<Natureformation,Long> {

}
