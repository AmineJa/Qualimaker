package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Competences;

import org.qualimaker.repository.CompetencesRepository;
import org.qualimaker.repository.search.CompetencesSearchRepository;
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
 * REST controller for managing Competences.
 */
@RestController
@RequestMapping("/api")
public class CompetencesResource {

    private final Logger log = LoggerFactory.getLogger(CompetencesResource.class);

    private static final String ENTITY_NAME = "competences";
        
    private final CompetencesRepository competencesRepository;

    private final CompetencesSearchRepository competencesSearchRepository;

    public CompetencesResource(CompetencesRepository competencesRepository, CompetencesSearchRepository competencesSearchRepository) {
        this.competencesRepository = competencesRepository;
        this.competencesSearchRepository = competencesSearchRepository;
    }

    /**
     * POST  /competences : Create a new competences.
     *
     * @param competences the competences to create
     * @return the ResponseEntity with status 201 (Created) and with body the new competences, or with status 400 (Bad Request) if the competences has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/competences")
    @Timed
    public ResponseEntity<Competences> createCompetences(@RequestBody Competences competences) throws URISyntaxException {
        log.debug("REST request to save Competences : {}", competences);
        if (competences.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new competences cannot already have an ID")).body(null);
        }
        Competences result = competencesRepository.save(competences);
        competencesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/competences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /competences : Updates an existing competences.
     *
     * @param competences the competences to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated competences,
     * or with status 400 (Bad Request) if the competences is not valid,
     * or with status 500 (Internal Server Error) if the competences couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/competences")
    @Timed
    public ResponseEntity<Competences> updateCompetences(@RequestBody Competences competences) throws URISyntaxException {
        log.debug("REST request to update Competences : {}", competences);
        if (competences.getId() == null) {
            return createCompetences(competences);
        }
        Competences result = competencesRepository.save(competences);
        competencesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, competences.getId().toString()))
            .body(result);
    }

    /**
     * GET  /competences : get all the competences.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of competences in body
     */
    @GetMapping("/competences")
    @Timed
    public ResponseEntity<List<Competences>> getAllCompetences(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Competences");
        Page<Competences> page = competencesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/competences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /competences/:id : get the "id" competences.
     *
     * @param id the id of the competences to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the competences, or with status 404 (Not Found)
     */
    @GetMapping("/competences/{id}")
    @Timed
    public ResponseEntity<Competences> getCompetences(@PathVariable Long id) {
        log.debug("REST request to get Competences : {}", id);
        Competences competences = competencesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(competences));
    }

    /**
     * DELETE  /competences/:id : delete the "id" competences.
     *
     * @param id the id of the competences to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/competences/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompetences(@PathVariable Long id) {
        log.debug("REST request to delete Competences : {}", id);
        competencesRepository.delete(id);
        competencesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/competences?query=:query : search for the competences corresponding
     * to the query.
     *
     * @param query the query of the competences search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/competences")
    @Timed
    public ResponseEntity<List<Competences>> searchCompetences(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Competences for query {}", query);
        Page<Competences> page = competencesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/competences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
