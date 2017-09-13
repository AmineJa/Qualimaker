package org.qualimaker.repository;

import org.qualimaker.domain.DocumentInterne;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the DocumentInterne entity.
 */
@SuppressWarnings("unused")
public interface DocumentInterneRepository extends JpaRepository<DocumentInterne,Long> {

    @Query("select distinct documentInterne from DocumentInterne documentInterne left join fetch documentInterne.verificateurs left join fetch documentInterne.redacteurs left join fetch documentInterne.approubateurs")
    List<DocumentInterne> findAllWithEagerRelationships();

    @Query("select documentInterne from DocumentInterne documentInterne left join fetch documentInterne.verificateurs left join fetch documentInterne.redacteurs left join fetch documentInterne.approubateurs where documentInterne.id =:id")
    DocumentInterne findOneWithEagerRelationships(@Param("id") Long id);

}
