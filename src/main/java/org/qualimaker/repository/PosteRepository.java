package org.qualimaker.repository;

import org.qualimaker.domain.Poste;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Poste entity.
 */
@SuppressWarnings("unused")
public interface PosteRepository extends JpaRepository<Poste,Long> {

}
