package org.qualimaker.repository;

import org.qualimaker.domain.Serviice;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Serviice entity.
 */
@SuppressWarnings("unused")
public interface ServiiceRepository extends JpaRepository<Serviice,Long> {

}
