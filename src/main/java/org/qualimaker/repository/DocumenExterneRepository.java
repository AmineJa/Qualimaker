package org.qualimaker.repository;

import org.qualimaker.domain.DocumenExterne;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the DocumenExterne entity.
 */
@SuppressWarnings("unused")
public interface DocumenExterneRepository extends JpaRepository<DocumenExterne,Long> {

    @Query("select distinct documenExterne from DocumenExterne documenExterne left join fetch documenExterne.employes")
    List<DocumenExterne> findAllWithEagerRelationships();

    @Query("select documenExterne from DocumenExterne documenExterne left join fetch documenExterne.employes where documenExterne.id =:id")
    DocumenExterne findOneWithEagerRelationships(@Param("id") Long id);

}
