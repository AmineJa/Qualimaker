package org.qualimaker.repository;

import org.qualimaker.domain.Processus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Processus entity.
 */
@SuppressWarnings("unused")
public interface ProcessusRepository extends JpaRepository<Processus,Long> {

}
