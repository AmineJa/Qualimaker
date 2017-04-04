package org.qualimaker.repository;

import org.qualimaker.domain.Sites;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sites entity.
 */
@SuppressWarnings("unused")
public interface SitesRepository extends JpaRepository<Sites,Long> {

}
