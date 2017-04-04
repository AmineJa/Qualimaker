package org.qualimaker.repository;

import org.qualimaker.domain.Integre;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Integre entity.
 */
@SuppressWarnings("unused")
public interface IntegreRepository extends JpaRepository<Integre,Long> {

}
