package org.qualimaker.repository;

import org.qualimaker.domain.Groupe;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Groupe entity.
 */
@SuppressWarnings("unused")
public interface GroupeRepository extends JpaRepository<Groupe,Long> {

}
