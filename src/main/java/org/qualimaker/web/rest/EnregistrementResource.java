package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Enregistrement;

import org.qualimaker.repository.EnregistrementRepository;
import org.qualimaker.repository.search.EnregistrementSearchRepository;
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
 * REST controller for managing Enregistrement.
 */
@RestController
@RequestMapping("/api")
public class EnregistrementResource {

    private final Logger log = LoggerFactory.getLogger(EnregistrementResource.class);

    private static final String ENTITY_NAME = "enregistrement";
        
    private final EnregistrementRepository enregistrementRepository;

    private final EnregistrementSearchRepository enregistrementSearchRepository;

    public EnregistrementResource(EnregistrementRepository enregistrementRepository, EnregistrementSearchRepository enregistrementSearchRepository) {
        this.enregistrementRepository = enregistrementRepository;
        this.enregistrementSearchRepository = enregistrementSearchRepository;
    }

    /**
     * POST  /enregistrements : Create a new enregistrement.
     *
     * @param enregistrement the enregistrement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new enregistrement, or with status 400 (Bad Request) if the enregistrement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/enregistrements")
    @Timed
    public ResponseEntity<Enregistrement> createEnregistrement(@Valid @RequestBody Enregistrement enregistrement) throws URISyntaxException {
        log.debug("REST request to save Enregistrement : {}", enregistrement);
        if (enregistrement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new enregistrement cannot already have an ID")).body(null);
        }
        Enregistrement result = enregistrementRepository.save(enregistrement);
        enregistrementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/enregistrements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enregistrements : Updates an existing enregistrement.
     *
     * @param enregistrement the enregistrement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated enregistrement,
     * or with status 400 (Bad Request) if the enregistrement is not valid,
     * or with status 500 (Internal Server Error) if the enregistrement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/enregistrements")
    @Timed
    public ResponseEntity<Enregistrement> updateEnregistrement(@Valid @RequestBody Enregistrement enregistrement) throws URISyntaxException {
        log.debug("REST request to update Enregistrement : {}", enregistrement);
        if (enregistrement.getId() == null) {
            return createEnregistrement(enregistrement);
        }
        Enregistrement result = enregistrementRepository.save(enregistrement);
        enregistrementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, enregistrement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enregistrements : get all the enregistrements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of enregistrements in body
     */
    @GetMapping("/enregistrements")
    @Timed
    public ResponseEntity<List<Enregistrement>> getAllEnregistrements(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Enregistrements");
        Page<Enregistrement> page = enregistrementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/enregistrements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /enregistrements/:id : get the "id" enregistrement.
     *
     * @param id the id of the enregistrement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the enregistrement, or with status 404 (Not Found)
     */
    @GetMapping("/enregistrements/{id}")
    @Timed
    public ResponseEntity<Enregistrement> getEnregistrement(@PathVariable Long id) {
        log.debug("REST request to get Enregistrement : {}", id);
        Enregistrement enregistrement = enregistrementRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(enregistrement));
    }

    /**
     * DELETE  /enregistrements/:id : delete the "id" enregistrement.
     *
     * @param id the id of the enregistrement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/enregistrements/{id}")
    @Timed
    public ResponseEntity<Void> deleteEnregistrement(@PathVariable Long id) {
        log.debug("REST request to delete Enregistrement : {}", id);
        enregistrementRepository.delete(id);
        enregistrementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/enregistrements?query=:query : search for the enregistrement corresponding
     * to the query.
     *
     * @param query the query of the enregistrement search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/enregistrements")
    @Timed
    public ResponseEntity<List<Enregistrement>> searchEnregistrements(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Enregistrements for query {}", query);
        Page<Enregistrement> page = enregistrementSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/enregistrements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
