package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Origine;

import org.qualimaker.repository.OrigineRepository;
import org.qualimaker.repository.search.OrigineSearchRepository;
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
 * REST controller for managing Origine.
 */
@RestController
@RequestMapping("/api")
public class OrigineResource {

    private final Logger log = LoggerFactory.getLogger(OrigineResource.class);

    private static final String ENTITY_NAME = "origine";
        
    private final OrigineRepository origineRepository;

    private final OrigineSearchRepository origineSearchRepository;

    public OrigineResource(OrigineRepository origineRepository, OrigineSearchRepository origineSearchRepository) {
        this.origineRepository = origineRepository;
        this.origineSearchRepository = origineSearchRepository;
    }

    /**
     * POST  /origines : Create a new origine.
     *
     * @param origine the origine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new origine, or with status 400 (Bad Request) if the origine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/origines")
    @Timed
    public ResponseEntity<Origine> createOrigine(@RequestBody Origine origine) throws URISyntaxException {
        log.debug("REST request to save Origine : {}", origine);
        if (origine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new origine cannot already have an ID")).body(null);
        }
        Origine result = origineRepository.save(origine);
        origineSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/origines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /origines : Updates an existing origine.
     *
     * @param origine the origine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated origine,
     * or with status 400 (Bad Request) if the origine is not valid,
     * or with status 500 (Internal Server Error) if the origine couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/origines")
    @Timed
    public ResponseEntity<Origine> updateOrigine(@RequestBody Origine origine) throws URISyntaxException {
        log.debug("REST request to update Origine : {}", origine);
        if (origine.getId() == null) {
            return createOrigine(origine);
        }
        Origine result = origineRepository.save(origine);
        origineSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, origine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /origines : get all the origines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of origines in body
     */
    @GetMapping("/origines")
    @Timed
    public ResponseEntity<List<Origine>> getAllOrigines(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Origines");
        Page<Origine> page = origineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/origines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /origines/:id : get the "id" origine.
     *
     * @param id the id of the origine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the origine, or with status 404 (Not Found)
     */
    @GetMapping("/origines/{id}")
    @Timed
    public ResponseEntity<Origine> getOrigine(@PathVariable Long id) {
        log.debug("REST request to get Origine : {}", id);
        Origine origine = origineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(origine));
    }

    /**
     * DELETE  /origines/:id : delete the "id" origine.
     *
     * @param id the id of the origine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/origines/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrigine(@PathVariable Long id) {
        log.debug("REST request to delete Origine : {}", id);
        origineRepository.delete(id);
        origineSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/origines?query=:query : search for the origine corresponding
     * to the query.
     *
     * @param query the query of the origine search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/origines")
    @Timed
    public ResponseEntity<List<Origine>> searchOrigines(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Origines for query {}", query);
        Page<Origine> page = origineSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/origines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
