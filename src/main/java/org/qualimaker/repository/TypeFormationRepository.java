package org.qualimaker.repository;

import org.qualimaker.domain.TypeFormation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeFormation entity.
 */
@SuppressWarnings("unused")
public interface TypeFormationRepository extends JpaRepository<TypeFormation,Long> {

}
