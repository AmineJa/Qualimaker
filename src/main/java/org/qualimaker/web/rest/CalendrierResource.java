package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Calendrier;

import org.qualimaker.repository.CalendrierRepository;
import org.qualimaker.repository.search.CalendrierSearchRepository;
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
 * REST controller for managing Calendrier.
 */
@RestController
@RequestMapping("/api")
public class CalendrierResource {

    private final Logger log = LoggerFactory.getLogger(CalendrierResource.class);

    private static final String ENTITY_NAME = "calendrier";
        
    private final CalendrierRepository calendrierRepository;

    private final CalendrierSearchRepository calendrierSearchRepository;

    public CalendrierResource(CalendrierRepository calendrierRepository, CalendrierSearchRepository calendrierSearchRepository) {
        this.calendrierRepository = calendrierRepository;
        this.calendrierSearchRepository = calendrierSearchRepository;
    }

    /**
     * POST  /calendriers : Create a new calendrier.
     *
     * @param calendrier the calendrier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new calendrier, or with status 400 (Bad Request) if the calendrier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/calendriers")
    @Timed
    public ResponseEntity<Calendrier> createCalendrier(@RequestBody Calendrier calendrier) throws URISyntaxException {
        log.debug("REST request to save Calendrier : {}", calendrier);
        if (calendrier.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new calendrier cannot already have an ID")).body(null);
        }
        Calendrier result = calendrierRepository.save(calendrier);
        calendrierSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/calendriers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /calendriers : Updates an existing calendrier.
     *
     * @param calendrier the calendrier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated calendrier,
     * or with status 400 (Bad Request) if the calendrier is not valid,
     * or with status 500 (Internal Server Error) if the calendrier couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/calendriers")
    @Timed
    public ResponseEntity<Calendrier> updateCalendrier(@RequestBody Calendrier calendrier) throws URISyntaxException {
        log.debug("REST request to update Calendrier : {}", calendrier);
        if (calendrier.getId() == null) {
            return createCalendrier(calendrier);
        }
        Calendrier result = calendrierRepository.save(calendrier);
        calendrierSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, calendrier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /calendriers : get all the calendriers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of calendriers in body
     */
    @GetMapping("/calendriers")
    @Timed
    public ResponseEntity<List<Calendrier>> getAllCalendriers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Calendriers");
        Page<Calendrier> page = calendrierRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/calendriers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /calendriers/:id : get the "id" calendrier.
     *
     * @param id the id of the calendrier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the calendrier, or with status 404 (Not Found)
     */
    @GetMapping("/calendriers/{id}")
    @Timed
    public ResponseEntity<Calendrier> getCalendrier(@PathVariable Long id) {
        log.debug("REST request to get Calendrier : {}", id);
        Calendrier calendrier = calendrierRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(calendrier));
    }

    /**
     * DELETE  /calendriers/:id : delete the "id" calendrier.
     *
     * @param id the id of the calendrier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/calendriers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCalendrier(@PathVariable Long id) {
        log.debug("REST request to delete Calendrier : {}", id);
        calendrierRepository.delete(id);
        calendrierSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/calendriers?query=:query : search for the calendrier corresponding
     * to the query.
     *
     * @param query the query of the calendrier search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/calendriers")
    @Timed
    public ResponseEntity<List<Calendrier>> searchCalendriers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Calendriers for query {}", query);
        Page<Calendrier> page = calendrierSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/calendriers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
