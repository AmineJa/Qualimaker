package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.LieuxClassement;

import org.qualimaker.repository.LieuxClassementRepository;
import org.qualimaker.repository.search.LieuxClassementSearchRepository;
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
 * REST controller for managing LieuxClassement.
 */
@RestController
@RequestMapping("/api")
public class LieuxClassementResource {

    private final Logger log = LoggerFactory.getLogger(LieuxClassementResource.class);

    private static final String ENTITY_NAME = "lieuxClassement";
        
    private final LieuxClassementRepository lieuxClassementRepository;

    private final LieuxClassementSearchRepository lieuxClassementSearchRepository;

    public LieuxClassementResource(LieuxClassementRepository lieuxClassementRepository, LieuxClassementSearchRepository lieuxClassementSearchRepository) {
        this.lieuxClassementRepository = lieuxClassementRepository;
        this.lieuxClassementSearchRepository = lieuxClassementSearchRepository;
    }

    /**
     * POST  /lieux-classements : Create a new lieuxClassement.
     *
     * @param lieuxClassement the lieuxClassement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lieuxClassement, or with status 400 (Bad Request) if the lieuxClassement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lieux-classements")
    @Timed
    public ResponseEntity<LieuxClassement> createLieuxClassement(@RequestBody LieuxClassement lieuxClassement) throws URISyntaxException {
        log.debug("REST request to save LieuxClassement : {}", lieuxClassement);
        if (lieuxClassement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new lieuxClassement cannot already have an ID")).body(null);
        }
        LieuxClassement result = lieuxClassementRepository.save(lieuxClassement);
        lieuxClassementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/lieux-classements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lieux-classements : Updates an existing lieuxClassement.
     *
     * @param lieuxClassement the lieuxClassement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lieuxClassement,
     * or with status 400 (Bad Request) if the lieuxClassement is not valid,
     * or with status 500 (Internal Server Error) if the lieuxClassement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lieux-classements")
    @Timed
    public ResponseEntity<LieuxClassement> updateLieuxClassement(@RequestBody LieuxClassement lieuxClassement) throws URISyntaxException {
        log.debug("REST request to update LieuxClassement : {}", lieuxClassement);
        if (lieuxClassement.getId() == null) {
            return createLieuxClassement(lieuxClassement);
        }
        LieuxClassement result = lieuxClassementRepository.save(lieuxClassement);
        lieuxClassementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lieuxClassement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lieux-classements : get all the lieuxClassements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lieuxClassements in body
     */
    @GetMapping("/lieux-classements")
    @Timed
    public ResponseEntity<List<LieuxClassement>> getAllLieuxClassements(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of LieuxClassements");
        Page<LieuxClassement> page = lieuxClassementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lieux-classements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lieux-classements/:id : get the "id" lieuxClassement.
     *
     * @param id the id of the lieuxClassement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lieuxClassement, or with status 404 (Not Found)
     */
    @GetMapping("/lieux-classements/{id}")
    @Timed
    public ResponseEntity<LieuxClassement> getLieuxClassement(@PathVariable Long id) {
        log.debug("REST request to get LieuxClassement : {}", id);
        LieuxClassement lieuxClassement = lieuxClassementRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(lieuxClassement));
    }

    /**
     * DELETE  /lieux-classements/:id : delete the "id" lieuxClassement.
     *
     * @param id the id of the lieuxClassement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lieux-classements/{id}")
    @Timed
    public ResponseEntity<Void> deleteLieuxClassement(@PathVariable Long id) {
        log.debug("REST request to delete LieuxClassement : {}", id);
        lieuxClassementRepository.delete(id);
        lieuxClassementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/lieux-classements?query=:query : search for the lieuxClassement corresponding
     * to the query.
     *
     * @param query the query of the lieuxClassement search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/lieux-classements")
    @Timed
    public ResponseEntity<List<LieuxClassement>> searchLieuxClassements(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of LieuxClassements for query {}", query);
        Page<LieuxClassement> page = lieuxClassementSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/lieux-classements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
