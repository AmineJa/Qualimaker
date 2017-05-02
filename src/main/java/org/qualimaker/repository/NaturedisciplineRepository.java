package org.qualimaker.repository;

import org.qualimaker.domain.Naturediscipline;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Naturediscipline entity.
 */
@SuppressWarnings("unused")
public interface NaturedisciplineRepository extends JpaRepository<Naturediscipline,Long> {

}
