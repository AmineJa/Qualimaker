package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Events;

import org.qualimaker.repository.EventsRepository;
import org.qualimaker.repository.search.EventsSearchRepository;
import org.qualimaker.web.rest.util.HeaderUtil;
import org.qualimaker.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Events.
 */
@RestController
@RequestMapping("/api")
public class EventsResource {

    private final Logger log = LoggerFactory.getLogger(EventsResource.class);

    private static final String ENTITY_NAME = "events";
        
    private final EventsRepository eventsRepository;

    private final EventsSearchRepository eventsSearchRepository;

    public EventsResource(EventsRepository eventsRepository, EventsSearchRepository eventsSearchRepository) {
        this.eventsRepository = eventsRepository;
        this.eventsSearchRepository = eventsSearchRepository;
    }

    /**
     * POST  /events : Create a new events.
     *
     * @param events the events to create
     * @return the ResponseEntity with status 201 (Created) and with body the new events, or with status 400 (Bad Request) if the events has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/events")
    @Timed
    public ResponseEntity<Events> createEvents(@RequestBody Events events) throws URISyntaxException {
        log.debug("REST request to save Events : {}", events);
        if (events.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new events cannot already have an ID")).body(null);
        }
        Events result = eventsRepository.save(events);
        eventsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /events : Updates an existing events.
     *
     * @param events the events to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated events,
     * or with status 400 (Bad Request) if the events is not valid,
     * or with status 500 (Internal Server Error) if the events couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/events")
    @Timed
    public ResponseEntity<Events> updateEvents(@RequestBody Events events) throws URISyntaxException {
        log.debug("REST request to update Events : {}", events);
        if (events.getId() == null) {
            return createEvents(events);
        }
        Events result = eventsRepository.save(events);
        eventsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, events.getId().toString()))
            .body(result);
    }

    /**
     * GET  /events : get all the events.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of events in body
     */
    @GetMapping("/events")
    @Timed
    public ResponseEntity<List<Events>> getAllEvents(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Events");
        Page<Events> page = eventsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /events/:id : get the "id" events.
     *
     * @param id the id of the events to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the events, or with status 404 (Not Found)
     */
    @GetMapping("/events/{id}")
    @Timed
    public ResponseEntity<Events> getEvents(@PathVariable Long id) {
        log.debug("REST request to get Events : {}", id);
        Events events = eventsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(events));
    }

    /**
     * DELETE  /events/:id : delete the "id" events.
     *
     * @param id the id of the events to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/events/{id}")
    @Timed
    public ResponseEntity<Void> deleteEvents(@PathVariable Long id) {
        log.debug("REST request to delete Events : {}", id);
        eventsRepository.delete(id);
        eventsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/events?query=:query : search for the events corresponding
     * to the query.
     *
     * @param query the query of the events search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/events")
    @Timed
    public ResponseEntity<List<Events>> searchEvents(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Events for query {}", query);
        Page<Events> page = eventsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
