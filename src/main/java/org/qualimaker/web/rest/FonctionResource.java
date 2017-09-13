package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Fonction;

import org.qualimaker.repository.FonctionRepository;
import org.qualimaker.repository.search.FonctionSearchRepository;
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
 * REST controller for managing Fonction.
 */
@RestController
@RequestMapping("/api")
public class FonctionResource {

    private final Logger log = LoggerFactory.getLogger(FonctionResource.class);

    private static final String ENTITY_NAME = "fonction";
        
    private final FonctionRepository fonctionRepository;

    private final FonctionSearchRepository fonctionSearchRepository;

    public FonctionResource(FonctionRepository fonctionRepository, FonctionSearchRepository fonctionSearchRepository) {
        this.fonctionRepository = fonctionRepository;
        this.fonctionSearchRepository = fonctionSearchRepository;
    }

    /**
     * POST  /fonctions : Create a new fonction.
     *
     * @param fonction the fonction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fonction, or with status 400 (Bad Request) if the fonction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fonctions")
    @Timed
    public ResponseEntity<Fonction> createFonction(@Valid @RequestBody Fonction fonction) throws URISyntaxException {
        log.debug("REST request to save Fonction : {}", fonction);
        if (fonction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fonction cannot already have an ID")).body(null);
        }
        Fonction result = fonctionRepository.save(fonction);
        fonctionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fonctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fonctions : Updates an existing fonction.
     *
     * @param fonction the fonction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fonction,
     * or with status 400 (Bad Request) if the fonction is not valid,
     * or with status 500 (Internal Server Error) if the fonction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fonctions")
    @Timed
    public ResponseEntity<Fonction> updateFonction(@Valid @RequestBody Fonction fonction) throws URISyntaxException {
        log.debug("REST request to update Fonction : {}", fonction);
        if (fonction.getId() == null) {
            return createFonction(fonction);
        }
        Fonction result = fonctionRepository.save(fonction);
        fonctionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fonction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fonctions : get all the fonctions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fonctions in body
     */
    @GetMapping("/fonctions")
    @Timed
    public ResponseEntity<List<Fonction>> getAllFonctions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Fonctions");
        Page<Fonction> page = fonctionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fonctions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fonctions/:id : get the "id" fonction.
     *
     * @param id the id of the fonction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fonction, or with status 404 (Not Found)
     */
    @GetMapping("/fonctions/{id}")
    @Timed
    public ResponseEntity<Fonction> getFonction(@PathVariable Long id) {
        log.debug("REST request to get Fonction : {}", id);
        Fonction fonction = fonctionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fonction));
    }

    /**
     * DELETE  /fonctions/:id : delete the "id" fonction.
     *
     * @param id the id of the fonction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fonctions/{id}")
    @Timed
    public ResponseEntity<Void> deleteFonction(@PathVariable Long id) {
        log.debug("REST request to delete Fonction : {}", id);
        fonctionRepository.delete(id);
        fonctionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/fonctions?query=:query : search for the fonction corresponding
     * to the query.
     *
     * @param query the query of the fonction search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/fonctions")
    @Timed
    public ResponseEntity<List<Fonction>> searchFonctions(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Fonctions for query {}", query);
        Page<Fonction> page = fonctionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fonctions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
