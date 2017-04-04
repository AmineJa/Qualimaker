package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Conge;

import org.qualimaker.repository.CongeRepository;
import org.qualimaker.repository.search.CongeSearchRepository;
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
 * REST controller for managing Conge.
 */
@RestController
@RequestMapping("/api")
public class CongeResource {

    private final Logger log = LoggerFactory.getLogger(CongeResource.class);

    private static final String ENTITY_NAME = "conge";
        
    private final CongeRepository congeRepository;

    private final CongeSearchRepository congeSearchRepository;

    public CongeResource(CongeRepository congeRepository, CongeSearchRepository congeSearchRepository) {
        this.congeRepository = congeRepository;
        this.congeSearchRepository = congeSearchRepository;
    }

    /**
     * POST  /conges : Create a new conge.
     *
     * @param conge the conge to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conge, or with status 400 (Bad Request) if the conge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/conges")
    @Timed
    public ResponseEntity<Conge> createConge(@RequestBody Conge conge) throws URISyntaxException {
        log.debug("REST request to save Conge : {}", conge);
        if (conge.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new conge cannot already have an ID")).body(null);
        }
        Conge result = congeRepository.save(conge);
        congeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/conges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /conges : Updates an existing conge.
     *
     * @param conge the conge to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated conge,
     * or with status 400 (Bad Request) if the conge is not valid,
     * or with status 500 (Internal Server Error) if the conge couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/conges")
    @Timed
    public ResponseEntity<Conge> updateConge(@RequestBody Conge conge) throws URISyntaxException {
        log.debug("REST request to update Conge : {}", conge);
        if (conge.getId() == null) {
            return createConge(conge);
        }
        Conge result = congeRepository.save(conge);
        congeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, conge.getId().toString()))
            .body(result);
    }

    /**
     * GET  /conges : get all the conges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of conges in body
     */
    @GetMapping("/conges")
    @Timed
    public ResponseEntity<List<Conge>> getAllConges(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Conges");
        Page<Conge> page = congeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/conges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /conges/:id : get the "id" conge.
     *
     * @param id the id of the conge to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the conge, or with status 404 (Not Found)
     */
    @GetMapping("/conges/{id}")
    @Timed
    public ResponseEntity<Conge> getConge(@PathVariable Long id) {
        log.debug("REST request to get Conge : {}", id);
        Conge conge = congeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(conge));
    }

    /**
     * DELETE  /conges/:id : delete the "id" conge.
     *
     * @param id the id of the conge to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/conges/{id}")
    @Timed
    public ResponseEntity<Void> deleteConge(@PathVariable Long id) {
        log.debug("REST request to delete Conge : {}", id);
        congeRepository.delete(id);
        congeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/conges?query=:query : search for the conge corresponding
     * to the query.
     *
     * @param query the query of the conge search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/conges")
    @Timed
    public ResponseEntity<List<Conge>> searchConges(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Conges for query {}", query);
        Page<Conge> page = congeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/conges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
