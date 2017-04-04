package org.qualimaker.repository;

import org.qualimaker.domain.FormationComp;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FormationComp entity.
 */
@SuppressWarnings("unused")
public interface FormationCompRepository extends JpaRepository<FormationComp,Long> {

}
