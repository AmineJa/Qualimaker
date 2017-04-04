package org.qualimaker.repository;

import org.qualimaker.domain.Formateur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Formateur entity.
 */
@SuppressWarnings("unused")
public interface FormateurRepository extends JpaRepository<Formateur,Long> {

}
