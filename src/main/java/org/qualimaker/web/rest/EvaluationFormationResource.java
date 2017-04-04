package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.EvaluationFormation;

import org.qualimaker.repository.EvaluationFormationRepository;
import org.qualimaker.repository.search.EvaluationFormationSearchRepository;
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
 * REST controller for managing EvaluationFormation.
 */
@RestController
@RequestMapping("/api")
public class EvaluationFormationResource {

    private final Logger log = LoggerFactory.getLogger(EvaluationFormationResource.class);

    private static final String ENTITY_NAME = "evaluationFormation";
        
    private final EvaluationFormationRepository evaluationFormationRepository;

    private final EvaluationFormationSearchRepository evaluationFormationSearchRepository;

    public EvaluationFormationResource(EvaluationFormationRepository evaluationFormationRepository, EvaluationFormationSearchRepository evaluationFormationSearchRepository) {
        this.evaluationFormationRepository = evaluationFormationRepository;
        this.evaluationFormationSearchRepository = evaluationFormationSearchRepository;
    }

    /**
     * POST  /evaluation-formations : Create a new evaluationFormation.
     *
     * @param evaluationFormation the evaluationFormation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new evaluationFormation, or with status 400 (Bad Request) if the evaluationFormation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/evaluation-formations")
    @Timed
    public ResponseEntity<EvaluationFormation> createEvaluationFormation(@RequestBody EvaluationFormation evaluationFormation) throws URISyntaxException {
        log.debug("REST request to save EvaluationFormation : {}", evaluationFormation);
        if (evaluationFormation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new evaluationFormation cannot already have an ID")).body(null);
        }
        EvaluationFormation result = evaluationFormationRepository.save(evaluationFormation);
        evaluationFormationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/evaluation-formations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /evaluation-formations : Updates an existing evaluationFormation.
     *
     * @param evaluationFormation the evaluationFormation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated evaluationFormation,
     * or with status 400 (Bad Request) if the evaluationFormation is not valid,
     * or with status 500 (Internal Server Error) if the evaluationFormation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/evaluation-formations")
    @Timed
    public ResponseEntity<EvaluationFormation> updateEvaluationFormation(@RequestBody EvaluationFormation evaluationFormation) throws URISyntaxException {
        log.debug("REST request to update EvaluationFormation : {}", evaluationFormation);
        if (evaluationFormation.getId() == null) {
            return createEvaluationFormation(evaluationFormation);
        }
        EvaluationFormation result = evaluationFormationRepository.save(evaluationFormation);
        evaluationFormationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, evaluationFormation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /evaluation-formations : get all the evaluationFormations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of evaluationFormations in body
     */
    @GetMapping("/evaluation-formations")
    @Timed
    public List<EvaluationFormation> getAllEvaluationFormations() {
        log.debug("REST request to get all EvaluationFormations");
        List<EvaluationFormation> evaluationFormations = evaluationFormationRepository.findAll();
        return evaluationFormations;
    }

    /**
     * GET  /evaluation-formations/:id : get the "id" evaluationFormation.
     *
     * @param id the id of the evaluationFormation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the evaluationFormation, or with status 404 (Not Found)
     */
    @GetMapping("/evaluation-formations/{id}")
    @Timed
    public ResponseEntity<EvaluationFormation> getEvaluationFormation(@PathVariable Long id) {
        log.debug("REST request to get EvaluationFormation : {}", id);
        EvaluationFormation evaluationFormation = evaluationFormationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(evaluationFormation));
    }

    /**
     * DELETE  /evaluation-formations/:id : delete the "id" evaluationFormation.
     *
     * @param id the id of the evaluationFormation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/evaluation-formations/{id}")
    @Timed
    public ResponseEntity<Void> deleteEvaluationFormation(@PathVariable Long id) {
        log.debug("REST request to delete EvaluationFormation : {}", id);
        evaluationFormationRepository.delete(id);
        evaluationFormationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/evaluation-formations?query=:query : search for the evaluationFormation corresponding
     * to the query.
     *
     * @param query the query of the evaluationFormation search 
     * @return the result of the search
     */
    @GetMapping("/_search/evaluation-formations")
    @Timed
    public List<EvaluationFormation> searchEvaluationFormations(@RequestParam String query) {
        log.debug("REST request to search EvaluationFormations for query {}", query);
        return StreamSupport
            .stream(evaluationFormationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
