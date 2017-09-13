package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Remplacer;

import org.qualimaker.repository.RemplacerRepository;
import org.qualimaker.repository.search.RemplacerSearchRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Remplacer.
 */
@RestController
@RequestMapping("/api")
public class RemplacerResource {

    private final Logger log = LoggerFactory.getLogger(RemplacerResource.class);

    private static final String ENTITY_NAME = "remplacer";
        
    private final RemplacerRepository remplacerRepository;

    private final RemplacerSearchRepository remplacerSearchRepository;

    public RemplacerResource(RemplacerRepository remplacerRepository, RemplacerSearchRepository remplacerSearchRepository) {
        this.remplacerRepository = remplacerRepository;
        this.remplacerSearchRepository = remplacerSearchRepository;
    }

    /**
     * POST  /remplacers : Create a new remplacer.
     *
     * @param remplacer the remplacer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new remplacer, or with status 400 (Bad Request) if the remplacer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/remplacers")
    @Timed
    public ResponseEntity<Remplacer> createRemplacer(@Valid @RequestBody Remplacer remplacer) throws URISyntaxException {
        log.debug("REST request to save Remplacer : {}", remplacer);
        if (remplacer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new remplacer cannot already have an ID")).body(null);
        }
        Remplacer result = remplacerRepository.save(remplacer);
        remplacerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/remplacers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /remplacers : Updates an existing remplacer.
     *
     * @param remplacer the remplacer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated remplacer,
     * or with status 400 (Bad Request) if the remplacer is not valid,
     * or with status 500 (Internal Server Error) if the remplacer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/remplacers")
    @Timed
    public ResponseEntity<Remplacer> updateRemplacer(@Valid @RequestBody Remplacer remplacer) throws URISyntaxException {
        log.debug("REST request to update Remplacer : {}", remplacer);
        if (remplacer.getId() == null) {
            return createRemplacer(remplacer);
        }
        Remplacer result = remplacerRepository.save(remplacer);
        remplacerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, remplacer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /remplacers : get all the remplacers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of remplacers in body
     */
    @GetMapping("/remplacers")
    @Timed
    public ResponseEntity<List<Remplacer>> getAllRemplacers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Remplacers");
        Page<Remplacer> page = remplacerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/remplacers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /remplacers/:id : get the "id" remplacer.
     *
     * @param id the id of the remplacer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the remplacer, or with status 404 (Not Found)
     */
    @GetMapping("/remplacers/{id}")
    @Timed
    public ResponseEntity<Remplacer> getRemplacer(@PathVariable Long id) {
        log.debug("REST request to get Remplacer : {}", id);
        Remplacer remplacer = remplacerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(remplacer));
    }

    /**
     * DELETE  /remplacers/:id : delete the "id" remplacer.
     *
     * @param id the id of the remplacer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/remplacers/{id}")
    @Timed
    public ResponseEntity<Void> deleteRemplacer(@PathVariable Long id) {
        log.debug("REST request to delete Remplacer : {}", id);
        remplacerRepository.delete(id);
        remplacerSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/remplacers?query=:query : search for the remplacer corresponding
     * to the query.
     *
     * @param query the query of the remplacer search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/remplacers")
    @Timed
    public ResponseEntity<List<Remplacer>> searchRemplacers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Remplacers for query {}", query);
        Page<Remplacer> page = remplacerSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/remplacers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
