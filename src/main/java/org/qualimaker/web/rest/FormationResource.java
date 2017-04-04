package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Formation;

import org.qualimaker.repository.FormationRepository;
import org.qualimaker.repository.search.FormationSearchRepository;
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
 * REST controller for managing Formation.
 */
@RestController
@RequestMapping("/api")
public class FormationResource {

    private final Logger log = LoggerFactory.getLogger(FormationResource.class);

    private static final String ENTITY_NAME = "formation";
        
    private final FormationRepository formationRepository;

    private final FormationSearchRepository formationSearchRepository;

    public FormationResource(FormationRepository formationRepository, FormationSearchRepository formationSearchRepository) {
        this.formationRepository = formationRepository;
        this.formationSearchRepository = formationSearchRepository;
    }

    /**
     * POST  /formations : Create a new formation.
     *
     * @param formation the formation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formation, or with status 400 (Bad Request) if the formation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/formations")
    @Timed
    public ResponseEntity<Formation> createFormation(@Valid @RequestBody Formation formation) throws URISyntaxException {
        log.debug("REST request to save Formation : {}", formation);
        if (formation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new formation cannot already have an ID")).body(null);
        }
        Formation result = formationRepository.save(formation);
        formationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/formations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /formations : Updates an existing formation.
     *
     * @param formation the formation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formation,
     * or with status 400 (Bad Request) if the formation is not valid,
     * or with status 500 (Internal Server Error) if the formation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/formations")
    @Timed
    public ResponseEntity<Formation> updateFormation(@Valid @RequestBody Formation formation) throws URISyntaxException {
        log.debug("REST request to update Formation : {}", formation);
        if (formation.getId() == null) {
            return createFormation(formation);
        }
        Formation result = formationRepository.save(formation);
        formationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /formations : get all the formations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of formations in body
     */
    @GetMapping("/formations")
    @Timed
    public ResponseEntity<List<Formation>> getAllFormations(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Formations");
        Page<Formation> page = formationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/formations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /formations/:id : get the "id" formation.
     *
     * @param id the id of the formation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formation, or with status 404 (Not Found)
     */
    @GetMapping("/formations/{id}")
    @Timed
    public ResponseEntity<Formation> getFormation(@PathVariable Long id) {
        log.debug("REST request to get Formation : {}", id);
        Formation formation = formationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(formation));
    }

    /**
     * DELETE  /formations/:id : delete the "id" formation.
     *
     * @param id the id of the formation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/formations/{id}")
    @Timed
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        log.debug("REST request to delete Formation : {}", id);
        formationRepository.delete(id);
        formationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/formations?query=:query : search for the formation corresponding
     * to the query.
     *
     * @param query the query of the formation search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/formations")
    @Timed
    public ResponseEntity<List<Formation>> searchFormations(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Formations for query {}", query);
        Page<Formation> page = formationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/formations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
