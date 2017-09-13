package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Profilsfonction;

import org.qualimaker.repository.ProfilsfonctionRepository;
import org.qualimaker.repository.search.ProfilsfonctionSearchRepository;
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
 * REST controller for managing Profilsfonction.
 */
@RestController
@RequestMapping("/api")
public class ProfilsfonctionResource {

    private final Logger log = LoggerFactory.getLogger(ProfilsfonctionResource.class);

    private static final String ENTITY_NAME = "profilsfonction";
        
    private final ProfilsfonctionRepository profilsfonctionRepository;

    private final ProfilsfonctionSearchRepository profilsfonctionSearchRepository;

    public ProfilsfonctionResource(ProfilsfonctionRepository profilsfonctionRepository, ProfilsfonctionSearchRepository profilsfonctionSearchRepository) {
        this.profilsfonctionRepository = profilsfonctionRepository;
        this.profilsfonctionSearchRepository = profilsfonctionSearchRepository;
    }

    /**
     * POST  /profilsfonctions : Create a new profilsfonction.
     *
     * @param profilsfonction the profilsfonction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new profilsfonction, or with status 400 (Bad Request) if the profilsfonction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/profilsfonctions")
    @Timed
    public ResponseEntity<Profilsfonction> createProfilsfonction(@Valid @RequestBody Profilsfonction profilsfonction) throws URISyntaxException {
        log.debug("REST request to save Profilsfonction : {}", profilsfonction);
        if (profilsfonction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new profilsfonction cannot already have an ID")).body(null);
        }
        Profilsfonction result = profilsfonctionRepository.save(profilsfonction);
        profilsfonctionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/profilsfonctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profilsfonctions : Updates an existing profilsfonction.
     *
     * @param profilsfonction the profilsfonction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated profilsfonction,
     * or with status 400 (Bad Request) if the profilsfonction is not valid,
     * or with status 500 (Internal Server Error) if the profilsfonction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/profilsfonctions")
    @Timed
    public ResponseEntity<Profilsfonction> updateProfilsfonction(@Valid @RequestBody Profilsfonction profilsfonction) throws URISyntaxException {
        log.debug("REST request to update Profilsfonction : {}", profilsfonction);
        if (profilsfonction.getId() == null) {
            return createProfilsfonction(profilsfonction);
        }
        Profilsfonction result = profilsfonctionRepository.save(profilsfonction);
        profilsfonctionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profilsfonction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /profilsfonctions : get all the profilsfonctions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of profilsfonctions in body
     */
    @GetMapping("/profilsfonctions")
    @Timed
    public ResponseEntity<List<Profilsfonction>> getAllProfilsfonctions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Profilsfonctions");
        Page<Profilsfonction> page = profilsfonctionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/profilsfonctions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /profilsfonctions/:id : get the "id" profilsfonction.
     *
     * @param id the id of the profilsfonction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profilsfonction, or with status 404 (Not Found)
     */
    @GetMapping("/profilsfonctions/{id}")
    @Timed
    public ResponseEntity<Profilsfonction> getProfilsfonction(@PathVariable Long id) {
        log.debug("REST request to get Profilsfonction : {}", id);
        Profilsfonction profilsfonction = profilsfonctionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profilsfonction));
    }

    /**
     * DELETE  /profilsfonctions/:id : delete the "id" profilsfonction.
     *
     * @param id the id of the profilsfonction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/profilsfonctions/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfilsfonction(@PathVariable Long id) {
        log.debug("REST request to delete Profilsfonction : {}", id);
        profilsfonctionRepository.delete(id);
        profilsfonctionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/profilsfonctions?query=:query : search for the profilsfonction corresponding
     * to the query.
     *
     * @param query the query of the profilsfonction search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/profilsfonctions")
    @Timed
    public ResponseEntity<List<Profilsfonction>> searchProfilsfonctions(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Profilsfonctions for query {}", query);
        Page<Profilsfonction> page = profilsfonctionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/profilsfonctions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
