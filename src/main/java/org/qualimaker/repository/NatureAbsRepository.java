package org.qualimaker.repository;

import org.qualimaker.domain.NatureAbs;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NatureAbs entity.
 */
@SuppressWarnings("unused")
public interface NatureAbsRepository extends JpaRepository<NatureAbs,Long> {

}
