package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Critereevaluation;

import org.qualimaker.repository.CritereevaluationRepository;
import org.qualimaker.repository.search.CritereevaluationSearchRepository;
import org.qualimaker.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Critereevaluation.
 */
@RestController
@RequestMapping("/api")
public class CritereevaluationResource {

    private final Logger log = LoggerFactory.getLogger(CritereevaluationResource.class);

    private static final String ENTITY_NAME = "critereevaluation";
        
    private final CritereevaluationRepository critereevaluationRepository;

    private final CritereevaluationSearchRepository critereevaluationSearchRepository;

    public CritereevaluationResource(CritereevaluationRepository critereevaluationRepository, CritereevaluationSearchRepository critereevaluationSearchRepository) {
        this.critereevaluationRepository = critereevaluationRepository;
        this.critereevaluationSearchRepository = critereevaluationSearchRepository;
    }

    /**
     * POST  /critereevaluations : Create a new critereevaluation.
     *
     * @param critereevaluation the critereevaluation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new critereevaluation, or with status 400 (Bad Request) if the critereevaluation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/critereevaluations")
    @Timed
    public ResponseEntity<Critereevaluation> createCritereevaluation(@RequestBody Critereevaluation critereevaluation) throws URISyntaxException {
        log.debug("REST request to save Critereevaluation : {}", critereevaluation);
        if (critereevaluation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new critereevaluation cannot already have an ID")).body(null);
        }
        Critereevaluation result = critereevaluationRepository.save(critereevaluation);
        critereevaluationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/critereevaluations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /critereevaluations : Updates an existing critereevaluation.
     *
     * @param critereevaluation the critereevaluation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated critereevaluation,
     * or with status 400 (Bad Request) if the critereevaluation is not valid,
     * or with status 500 (Internal Server Error) if the critereevaluation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/critereevaluations")
    @Timed
    public ResponseEntity<Critereevaluation> updateCritereevaluation(@RequestBody Critereevaluation critereevaluation) throws URISyntaxException {
        log.debug("REST request to update Critereevaluation : {}", critereevaluation);
        if (critereevaluation.getId() == null) {
            return createCritereevaluation(critereevaluation);
        }
        Critereevaluation result = critereevaluationRepository.save(critereevaluation);
        critereevaluationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, critereevaluation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /critereevaluations : get all the critereevaluations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of critereevaluations in body
     */
    @GetMapping("/critereevaluations")
    @Timed
    public List<Critereevaluation> getAllCritereevaluations() {
        log.debug("REST request to get all Critereevaluations");
        List<Critereevaluation> critereevaluations = critereevaluationRepository.findAll();
        return critereevaluations;
    }

    /**
     * GET  /critereevaluations/:id : get the "id" critereevaluation.
     *
     * @param id the id of the critereevaluation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the critereevaluation, or with status 404 (Not Found)
     */
    @GetMapping("/critereevaluations/{id}")
    @Timed
    public ResponseEntity<Critereevaluation> getCritereevaluation(@PathVariable Long id) {
        log.debug("REST request to get Critereevaluation : {}", id);
        Critereevaluation critereevaluation = critereevaluationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(critereevaluation));
    }

    /**
     * DELETE  /critereevaluations/:id : delete the "id" critereevaluation.
     *
     * @param id the id of the critereevaluation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/critereevaluations/{id}")
    @Timed
    public ResponseEntity<Void> deleteCritereevaluation(@PathVariable Long id) {
        log.debug("REST request to delete Critereevaluation : {}", id);
        critereevaluationRepository.delete(id);
        critereevaluationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/critereevaluations?query=:query : search for the critereevaluation corresponding
     * to the query.
     *
     * @param query the query of the critereevaluation search 
     * @return the result of the search
     */
    @GetMapping("/_search/critereevaluations")
    @Timed
    public List<Critereevaluation> searchCritereevaluations(@RequestParam String query) {
        log.debug("REST request to search Critereevaluations for query {}", query);
        return StreamSupport
            .stream(critereevaluationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
