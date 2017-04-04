package org.qualimaker.repository;

import org.qualimaker.domain.TypeConge;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeConge entity.
 */
@SuppressWarnings("unused")
public interface TypeCongeRepository extends JpaRepository<TypeConge,Long> {

}
