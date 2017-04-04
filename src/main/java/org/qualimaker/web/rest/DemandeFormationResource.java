package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.DemandeFormation;

import org.qualimaker.repository.DemandeFormationRepository;
import org.qualimaker.repository.search.DemandeFormationSearchRepository;
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
 * REST controller for managing DemandeFormation.
 */
@RestController
@RequestMapping("/api")
public class DemandeFormationResource {

    private final Logger log = LoggerFactory.getLogger(DemandeFormationResource.class);

    private static final String ENTITY_NAME = "demandeFormation";
        
    private final DemandeFormationRepository demandeFormationRepository;

    private final DemandeFormationSearchRepository demandeFormationSearchRepository;

    public DemandeFormationResource(DemandeFormationRepository demandeFormationRepository, DemandeFormationSearchRepository demandeFormationSearchRepository) {
        this.demandeFormationRepository = demandeFormationRepository;
        this.demandeFormationSearchRepository = demandeFormationSearchRepository;
    }

    /**
     * POST  /demande-formations : Create a new demandeFormation.
     *
     * @param demandeFormation the demandeFormation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new demandeFormation, or with status 400 (Bad Request) if the demandeFormation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/demande-formations")
    @Timed
    public ResponseEntity<DemandeFormation> createDemandeFormation(@RequestBody DemandeFormation demandeFormation) throws URISyntaxException {
        log.debug("REST request to save DemandeFormation : {}", demandeFormation);
        if (demandeFormation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new demandeFormation cannot already have an ID")).body(null);
        }
        DemandeFormation result = demandeFormationRepository.save(demandeFormation);
        demandeFormationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/demande-formations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /demande-formations : Updates an existing demandeFormation.
     *
     * @param demandeFormation the demandeFormation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated demandeFormation,
     * or with status 400 (Bad Request) if the demandeFormation is not valid,
     * or with status 500 (Internal Server Error) if the demandeFormation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/demande-formations")
    @Timed
    public ResponseEntity<DemandeFormation> updateDemandeFormation(@RequestBody DemandeFormation demandeFormation) throws URISyntaxException {
        log.debug("REST request to update DemandeFormation : {}", demandeFormation);
        if (demandeFormation.getId() == null) {
            return createDemandeFormation(demandeFormation);
        }
        DemandeFormation result = demandeFormationRepository.save(demandeFormation);
        demandeFormationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, demandeFormation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /demande-formations : get all the demandeFormations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of demandeFormations in body
     */
    @GetMapping("/demande-formations")
    @Timed
    public ResponseEntity<List<DemandeFormation>> getAllDemandeFormations(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DemandeFormations");
        Page<DemandeFormation> page = demandeFormationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/demande-formations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /demande-formations/:id : get the "id" demandeFormation.
     *
     * @param id the id of the demandeFormation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the demandeFormation, or with status 404 (Not Found)
     */
    @GetMapping("/demande-formations/{id}")
    @Timed
    public ResponseEntity<DemandeFormation> getDemandeFormation(@PathVariable Long id) {
        log.debug("REST request to get DemandeFormation : {}", id);
        DemandeFormation demandeFormation = demandeFormationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(demandeFormation));
    }

    /**
     * DELETE  /demande-formations/:id : delete the "id" demandeFormation.
     *
     * @param id the id of the demandeFormation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/demande-formations/{id}")
    @Timed
    public ResponseEntity<Void> deleteDemandeFormation(@PathVariable Long id) {
        log.debug("REST request to delete DemandeFormation : {}", id);
        demandeFormationRepository.delete(id);
        demandeFormationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/demande-formations?query=:query : search for the demandeFormation corresponding
     * to the query.
     *
     * @param query the query of the demandeFormation search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/demande-formations")
    @Timed
    public ResponseEntity<List<DemandeFormation>> searchDemandeFormations(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of DemandeFormations for query {}", query);
        Page<DemandeFormation> page = demandeFormationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/demande-formations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
