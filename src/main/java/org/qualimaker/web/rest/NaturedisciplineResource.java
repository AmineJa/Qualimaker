package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Naturediscipline;

import org.qualimaker.repository.NaturedisciplineRepository;
import org.qualimaker.repository.search.NaturedisciplineSearchRepository;
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
 * REST controller for managing Naturediscipline.
 */
@RestController
@RequestMapping("/api")
public class NaturedisciplineResource {

    private final Logger log = LoggerFactory.getLogger(NaturedisciplineResource.class);

    private static final String ENTITY_NAME = "naturediscipline";
        
    private final NaturedisciplineRepository naturedisciplineRepository;

    private final NaturedisciplineSearchRepository naturedisciplineSearchRepository;

    public NaturedisciplineResource(NaturedisciplineRepository naturedisciplineRepository, NaturedisciplineSearchRepository naturedisciplineSearchRepository) {
        this.naturedisciplineRepository = naturedisciplineRepository;
        this.naturedisciplineSearchRepository = naturedisciplineSearchRepository;
    }

    /**
     * POST  /naturedisciplines : Create a new naturediscipline.
     *
     * @param naturediscipline the naturediscipline to create
     * @return the ResponseEntity with status 201 (Created) and with body the new naturediscipline, or with status 400 (Bad Request) if the naturediscipline has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/naturedisciplines")
    @Timed
    public ResponseEntity<Naturediscipline> createNaturediscipline(@RequestBody Naturediscipline naturediscipline) throws URISyntaxException {
        log.debug("REST request to save Naturediscipline : {}", naturediscipline);
        if (naturediscipline.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new naturediscipline cannot already have an ID")).body(null);
        }
        Naturediscipline result = naturedisciplineRepository.save(naturediscipline);
        naturedisciplineSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/naturedisciplines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /naturedisciplines : Updates an existing naturediscipline.
     *
     * @param naturediscipline the naturediscipline to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated naturediscipline,
     * or with status 400 (Bad Request) if the naturediscipline is not valid,
     * or with status 500 (Internal Server Error) if the naturediscipline couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/naturedisciplines")
    @Timed
    public ResponseEntity<Naturediscipline> updateNaturediscipline(@RequestBody Naturediscipline naturediscipline) throws URISyntaxException {
        log.debug("REST request to update Naturediscipline : {}", naturediscipline);
        if (naturediscipline.getId() == null) {
            return createNaturediscipline(naturediscipline);
        }
        Naturediscipline result = naturedisciplineRepository.save(naturediscipline);
        naturedisciplineSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, naturediscipline.getId().toString()))
            .body(result);
    }

    /**
     * GET  /naturedisciplines : get all the naturedisciplines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of naturedisciplines in body
     */
    @GetMapping("/naturedisciplines")
    @Timed
    public ResponseEntity<List<Naturediscipline>> getAllNaturedisciplines(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Naturedisciplines");
        Page<Naturediscipline> page = naturedisciplineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/naturedisciplines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /naturedisciplines/:id : get the "id" naturediscipline.
     *
     * @param id the id of the naturediscipline to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the naturediscipline, or with status 404 (Not Found)
     */
    @GetMapping("/naturedisciplines/{id}")
    @Timed
    public ResponseEntity<Naturediscipline> getNaturediscipline(@PathVariable Long id) {
        log.debug("REST request to get Naturediscipline : {}", id);
        Naturediscipline naturediscipline = naturedisciplineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(naturediscipline));
    }

    /**
     * DELETE  /naturedisciplines/:id : delete the "id" naturediscipline.
     *
     * @param id the id of the naturediscipline to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/naturedisciplines/{id}")
    @Timed
    public ResponseEntity<Void> deleteNaturediscipline(@PathVariable Long id) {
        log.debug("REST request to delete Naturediscipline : {}", id);
        naturedisciplineRepository.delete(id);
        naturedisciplineSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/naturedisciplines?query=:query : search for the naturediscipline corresponding
     * to the query.
     *
     * @param query the query of the naturediscipline search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/naturedisciplines")
    @Timed
    public ResponseEntity<List<Naturediscipline>> searchNaturedisciplines(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Naturedisciplines for query {}", query);
        Page<Naturediscipline> page = naturedisciplineSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/naturedisciplines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
