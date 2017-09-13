package org.qualimaker.repository;

import org.qualimaker.domain.TypeDocumentation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeDocumentation entity.
 */
@SuppressWarnings("unused")
public interface TypeDocumentationRepository extends JpaRepository<TypeDocumentation,Long> {

}
