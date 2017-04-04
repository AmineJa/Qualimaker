package org.qualimaker.repository;

import org.qualimaker.domain.TypeFormateur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeFormateur entity.
 */
@SuppressWarnings("unused")
public interface TypeFormateurRepository extends JpaRepository<TypeFormateur,Long> {

}
