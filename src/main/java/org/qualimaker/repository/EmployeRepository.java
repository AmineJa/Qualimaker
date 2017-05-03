package org.qualimaker.repository;

import org.qualimaker.domain.Employe;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Employe entity.
 */
@SuppressWarnings("unused")
public interface EmployeRepository extends JpaRepository<Employe,Long> {

    @Query("select distinct employe from Employe employe left join fetch employe.formations")
    List<Employe> findAllWithEagerRelationships();

    @Query("select employe from Employe employe left join fetch employe.formations where employe.id =:id")
    Employe findOneWithEagerRelationships(@Param("id") Long id);

}
