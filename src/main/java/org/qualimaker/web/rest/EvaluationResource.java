package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Evaluation;

import org.qualimaker.repository.EvaluationRepository;
import org.qualimaker.repository.search.EvaluationSearchRepository;
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
 * REST controller for managing Evaluation.
 */
@RestController
@RequestMapping("/api")
public class EvaluationResource {

    private final Logger log = LoggerFactory.getLogger(EvaluationResource.class);

    private static final String ENTITY_NAME = "evaluation";
        
    private final EvaluationRepository evaluationRepository;

    private final EvaluationSearchRepository evaluationSearchRepository;

    public EvaluationResource(EvaluationRepository evaluationRepository, EvaluationSearchRepository evaluationSearchRepository) {
        this.evaluationRepository = evaluationRepository;
        this.evaluationSearchRepository = evaluationSearchRepository;
    }

    /**
     * POST  /evaluations : Create a new evaluation.
     *
     * @param evaluation the evaluation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new evaluation, or with status 400 (Bad Request) if the evaluation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/evaluations")
    @Timed
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody Evaluation evaluation) throws URISyntaxException {
        log.debug("REST request to save Evaluation : {}", evaluation);
        if (evaluation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new evaluation cannot already have an ID")).body(null);
        }
        Evaluation result = evaluationRepository.save(evaluation);
        evaluationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/evaluations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /evaluations : Updates an existing evaluation.
     *
     * @param evaluation the evaluation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated evaluation,
     * or with status 400 (Bad Request) if the evaluation is not valid,
     * or with status 500 (Internal Server Error) if the evaluation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/evaluations")
    @Timed
    public ResponseEntity<Evaluation> updateEvaluation(@RequestBody Evaluation evaluation) throws URISyntaxException {
        log.debug("REST request to update Evaluation : {}", evaluation);
        if (evaluation.getId() == null) {
            return createEvaluation(evaluation);
        }
        Evaluation result = evaluationRepository.save(evaluation);
        evaluationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, evaluation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /evaluations : get all the evaluations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of evaluations in body
     */
    @GetMapping("/evaluations")
    @Timed
    public List<Evaluation> getAllEvaluations() {
        log.debug("REST request to get all Evaluations");
        List<Evaluation> evaluations = evaluationRepository.findAll();
        return evaluations;
    }

    /**
     * GET  /evaluations/:id : get the "id" evaluation.
     *
     * @param id the id of the evaluation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the evaluation, or with status 404 (Not Found)
     */
    @GetMapping("/evaluations/{id}")
    @Timed
    public ResponseEntity<Evaluation> getEvaluation(@PathVariable Long id) {
        log.debug("REST request to get Evaluation : {}", id);
        Evaluation evaluation = evaluationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(evaluation));
    }

    /**
     * DELETE  /evaluations/:id : delete the "id" evaluation.
     *
     * @param id the id of the evaluation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/evaluations/{id}")
    @Timed
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        log.debug("REST request to delete Evaluation : {}", id);
        evaluationRepository.delete(id);
        evaluationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/evaluations?query=:query : search for the evaluation corresponding
     * to the query.
     *
     * @param query the query of the evaluation search 
     * @return the result of the search
     */
    @GetMapping("/_search/evaluations")
    @Timed
    public List<Evaluation> searchEvaluations(@RequestParam String query) {
        log.debug("REST request to search Evaluations for query {}", query);
        return StreamSupport
            .stream(evaluationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
