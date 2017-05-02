package org.qualimaker.repository;

import org.qualimaker.domain.Discipline;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Discipline entity.
 */
@SuppressWarnings("unused")
public interface DisciplineRepository extends JpaRepository<Discipline,Long> {

}
