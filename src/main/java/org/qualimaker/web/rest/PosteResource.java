package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Poste;

import org.qualimaker.repository.PosteRepository;
import org.qualimaker.repository.search.PosteSearchRepository;
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
 * REST controller for managing Poste.
 */
@RestController
@RequestMapping("/api")
public class PosteResource {

    private final Logger log = LoggerFactory.getLogger(PosteResource.class);

    private static final String ENTITY_NAME = "poste";
        
    private final PosteRepository posteRepository;

    private final PosteSearchRepository posteSearchRepository;

    public PosteResource(PosteRepository posteRepository, PosteSearchRepository posteSearchRepository) {
        this.posteRepository = posteRepository;
        this.posteSearchRepository = posteSearchRepository;
    }

    /**
     * POST  /postes : Create a new poste.
     *
     * @param poste the poste to create
     * @return the ResponseEntity with status 201 (Created) and with body the new poste, or with status 400 (Bad Request) if the poste has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/postes")
    @Timed
    public ResponseEntity<Poste> createPoste(@RequestBody Poste poste) throws URISyntaxException {
        log.debug("REST request to save Poste : {}", poste);
        if (poste.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new poste cannot already have an ID")).body(null);
        }
        Poste result = posteRepository.save(poste);
        posteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/postes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /postes : Updates an existing poste.
     *
     * @param poste the poste to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated poste,
     * or with status 400 (Bad Request) if the poste is not valid,
     * or with status 500 (Internal Server Error) if the poste couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/postes")
    @Timed
    public ResponseEntity<Poste> updatePoste(@RequestBody Poste poste) throws URISyntaxException {
        log.debug("REST request to update Poste : {}", poste);
        if (poste.getId() == null) {
            return createPoste(poste);
        }
        Poste result = posteRepository.save(poste);
        posteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, poste.getId().toString()))
            .body(result);
    }

    /**
     * GET  /postes : get all the postes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of postes in body
     */
    @GetMapping("/postes")
    @Timed
    public ResponseEntity<List<Poste>> getAllPostes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Postes");
        Page<Poste> page = posteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/postes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /postes/:id : get the "id" poste.
     *
     * @param id the id of the poste to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the poste, or with status 404 (Not Found)
     */
    @GetMapping("/postes/{id}")
    @Timed
    public ResponseEntity<Poste> getPoste(@PathVariable Long id) {
        log.debug("REST request to get Poste : {}", id);
        Poste poste = posteRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(poste));
    }

    /**
     * DELETE  /postes/:id : delete the "id" poste.
     *
     * @param id the id of the poste to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/postes/{id}")
    @Timed
    public ResponseEntity<Void> deletePoste(@PathVariable Long id) {
        log.debug("REST request to delete Poste : {}", id);
        posteRepository.delete(id);
        posteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/postes?query=:query : search for the poste corresponding
     * to the query.
     *
     * @param query the query of the poste search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/postes")
    @Timed
    public ResponseEntity<List<Poste>> searchPostes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Postes for query {}", query);
        Page<Poste> page = posteSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/postes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
