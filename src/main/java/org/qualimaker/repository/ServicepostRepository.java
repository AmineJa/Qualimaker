package org.qualimaker.repository;

import org.qualimaker.domain.Servicepost;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Servicepost entity.
 */
@SuppressWarnings("unused")
public interface ServicepostRepository extends JpaRepository<Servicepost,Long> {

    @Query("select distinct servicepost from Servicepost servicepost left join fetch servicepost.postes")
    List<Servicepost> findAllWithEagerRelationships();

    @Query("select servicepost from Servicepost servicepost left join fetch servicepost.postes where servicepost.id =:id")
    Servicepost findOneWithEagerRelationships(@Param("id") Long id);

}
