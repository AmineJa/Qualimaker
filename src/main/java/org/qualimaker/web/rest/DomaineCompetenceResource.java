package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.qualimaker.repository.DomaineCompetenceRepository;
import org.qualimaker.repository.search.DomaineCompetenceSearchRepository;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing DomaineCompetence.
 */
@RestController
@RequestMapping("/api")
public class DomaineCompetenceResource {

    private final Logger log = LoggerFactory.getLogger(DomaineCompetenceResource.class);

    private static final String ENTITY_NAME = "domaineCompetence";

    private final DomaineCompetenceRepository domaineCompetenceRepository;

    private final DomaineCompetenceSearchRepository domaineCompetenceSearchRepository;

    public DomaineCompetenceResource(DomaineCompetenceRepository domaineCompetenceRepository, DomaineCompetenceSearchRepository domaineCompetenceSearchRepository) {
        this.domaineCompetenceRepository = domaineCompetenceRepository;
        this.domaineCompetenceSearchRepository = domaineCompetenceSearchRepository;
    }

    /**
     * POST  /domaine-competences : Create a new domaineCompetence.
     *
     * @param domaineCompetence the domaineCompetence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new domaineCompetence, or with status 400 (Bad Request) if the domaineCompetence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/domaine-competences")
    @Timed
    public ResponseEntity<DomaineCompetence> createDomaineCompetence(@RequestBody DomaineCompetence domaineCompetence) throws URISyntaxException {
        log.debug("REST request to save DomaineCompetence : {}", domaineCompetence);
        if (domaineCompetence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new domaineCompetence cannot already have an ID")).body(null);
        }
        DomaineCompetence result = domaineCompetenceRepository.save(domaineCompetence);
        domaineCompetenceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/domaine-competences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /domaine-competences : Updates an existing domaineCompetence.
     *
     * @param domaineCompetence the domaineCompetence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated domaineCompetence,
     * or with status 400 (Bad Request) if the domaineCompetence is not valid,
     * or with status 500 (Internal Server Error) if the domaineCompetence couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/domaine-competences")
    @Timed
    public ResponseEntity<DomaineCompetence> updateDomaineCompetence(@RequestBody DomaineCompetence domaineCompetence) throws URISyntaxException {
        log.debug("REST request to update DomaineCompetence : {}", domaineCompetence);
        if (domaineCompetence.getId() == null) {
            return createDomaineCompetence(domaineCompetence);
        }
        DomaineCompetence result = domaineCompetenceRepository.save(domaineCompetence);
        domaineCompetenceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, domaineCompetence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /domaine-competences : get all the domaineCompetences.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of domaineCompetences in body
     */
    @GetMapping("/domaine-competences")
    @Timed
    public ResponseEntity<List<DomaineCompetence>> getAllDomaineCompetences(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DomaineCompetences");
        Page<DomaineCompetence> page = domaineCompetenceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/domaine-competences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /domaine-competences/:id : get the "id" domaineCompetence.
     *
     * @param id the id of the domaineCompetence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the domaineCompetence, or with status 404 (Not Found)
     */
    @GetMapping("/domaine-competences/{id}")
    @Timed
    public ResponseEntity<DomaineCompetence> getDomaineCompetence(@PathVariable Long id) {
        log.debug("REST request to get DomaineCompetence : {}", id);
        DomaineCompetence domaineCompetence = domaineCompetenceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(domaineCompetence));
    }

    /**
     * DELETE  /domaine-competences/:id : delete the "id" domaineCompetence.
     *
     * @param id the id of the domaineCompetence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/domaine-competences/{id}")
    @Timed
    public ResponseEntity<Void> deleteDomaineCompetence(@PathVariable Long id) {
        log.debug("REST request to delete DomaineCompetence : {}", id);
        domaineCompetenceRepository.delete(id);
        domaineCompetenceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/domaine-competences?query=:query : search for the domaineCompetence corresponding
     * to the query.
     *
     * @param query the query of the domaineCompetence search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/domaine-competences")
    @Timed
    public ResponseEntity<List<DomaineCompetence>> searchDomaineCompetences(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of DomaineCompetences for query {}", query);
        Page<DomaineCompetence> page = domaineCompetenceSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/domaine-competences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
