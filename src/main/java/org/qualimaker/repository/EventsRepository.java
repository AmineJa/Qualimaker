package org.qualimaker.repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Events entity.
 */
@SuppressWarnings("unused")
public interface EventsRepository extends JpaRepository<Events,Long> {

}
