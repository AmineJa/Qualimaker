package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Abscence;

import org.qualimaker.repository.AbscenceRepository;
import org.qualimaker.repository.search.AbscenceSearchRepository;
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
 * REST controller for managing Abscence.
 */
@RestController
@RequestMapping("/api")
public class AbscenceResource {

    private final Logger log = LoggerFactory.getLogger(AbscenceResource.class);

    private static final String ENTITY_NAME = "abscence";
        
    private final AbscenceRepository abscenceRepository;

    private final AbscenceSearchRepository abscenceSearchRepository;

    public AbscenceResource(AbscenceRepository abscenceRepository, AbscenceSearchRepository abscenceSearchRepository) {
        this.abscenceRepository = abscenceRepository;
        this.abscenceSearchRepository = abscenceSearchRepository;
    }

    /**
     * POST  /abscences : Create a new abscence.
     *
     * @param abscence the abscence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new abscence, or with status 400 (Bad Request) if the abscence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/abscences")
    @Timed
    public ResponseEntity<Abscence> createAbscence(@RequestBody Abscence abscence) throws URISyntaxException {
        log.debug("REST request to save Abscence : {}", abscence);
        if (abscence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new abscence cannot already have an ID")).body(null);
        }
        Abscence result = abscenceRepository.save(abscence);
        abscenceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/abscences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /abscences : Updates an existing abscence.
     *
     * @param abscence the abscence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated abscence,
     * or with status 400 (Bad Request) if the abscence is not valid,
     * or with status 500 (Internal Server Error) if the abscence couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/abscences")
    @Timed
    public ResponseEntity<Abscence> updateAbscence(@RequestBody Abscence abscence) throws URISyntaxException {
        log.debug("REST request to update Abscence : {}", abscence);
        if (abscence.getId() == null) {
            return createAbscence(abscence);
        }
        Abscence result = abscenceRepository.save(abscence);
        abscenceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, abscence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /abscences : get all the abscences.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of abscences in body
     */
    @GetMapping("/abscences")
    @Timed
    public ResponseEntity<List<Abscence>> getAllAbscences(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Abscences");
        Page<Abscence> page = abscenceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/abscences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /abscences/:id : get the "id" abscence.
     *
     * @param id the id of the abscence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the abscence, or with status 404 (Not Found)
     */
    @GetMapping("/abscences/{id}")
    @Timed
    public ResponseEntity<Abscence> getAbscence(@PathVariable Long id) {
        log.debug("REST request to get Abscence : {}", id);
        Abscence abscence = abscenceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(abscence));
    }

    /**
     * DELETE  /abscences/:id : delete the "id" abscence.
     *
     * @param id the id of the abscence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/abscences/{id}")
    @Timed
    public ResponseEntity<Void> deleteAbscence(@PathVariable Long id) {
        log.debug("REST request to delete Abscence : {}", id);
        abscenceRepository.delete(id);
        abscenceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/abscences?query=:query : search for the abscence corresponding
     * to the query.
     *
     * @param query the query of the abscence search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/abscences")
    @Timed
    public ResponseEntity<List<Abscence>> searchAbscences(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Abscences for query {}", query);
        Page<Abscence> page = abscenceSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/abscences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
