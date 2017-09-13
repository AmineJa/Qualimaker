package org.qualimaker.repository;

import org.qualimaker.domain.DroitaccesDocument;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the DroitaccesDocument entity.
 */
@SuppressWarnings("unused")
public interface DroitaccesDocumentRepository extends JpaRepository<DroitaccesDocument,Long> {

    @Query("select distinct droitaccesDocument from DroitaccesDocument droitaccesDocument left join fetch droitaccesDocument.approubateurs")
    List<DroitaccesDocument> findAllWithEagerRelationships();

    @Query("select droitaccesDocument from DroitaccesDocument droitaccesDocument left join fetch droitaccesDocument.approubateurs where droitaccesDocument.id =:id")
    DroitaccesDocument findOneWithEagerRelationships(@Param("id") Long id);

}
