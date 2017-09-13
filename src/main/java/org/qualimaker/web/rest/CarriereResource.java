package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Carriere;

import org.qualimaker.repository.CarriereRepository;
import org.qualimaker.repository.search.CarriereSearchRepository;
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
 * REST controller for managing Carriere.
 */
@RestController
@RequestMapping("/api")
public class CarriereResource {

    private final Logger log = LoggerFactory.getLogger(CarriereResource.class);

    private static final String ENTITY_NAME = "carriere";
        
    private final CarriereRepository carriereRepository;

    private final CarriereSearchRepository carriereSearchRepository;

    public CarriereResource(CarriereRepository carriereRepository, CarriereSearchRepository carriereSearchRepository) {
        this.carriereRepository = carriereRepository;
        this.carriereSearchRepository = carriereSearchRepository;
    }

    /**
     * POST  /carrieres : Create a new carriere.
     *
     * @param carriere the carriere to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carriere, or with status 400 (Bad Request) if the carriere has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/carrieres")
    @Timed
    public ResponseEntity<Carriere> createCarriere(@Valid @RequestBody Carriere carriere) throws URISyntaxException {
        log.debug("REST request to save Carriere : {}", carriere);
        if (carriere.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new carriere cannot already have an ID")).body(null);
        }
        Carriere result = carriereRepository.save(carriere);
        carriereSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/carrieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /carrieres : Updates an existing carriere.
     *
     * @param carriere the carriere to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carriere,
     * or with status 400 (Bad Request) if the carriere is not valid,
     * or with status 500 (Internal Server Error) if the carriere couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/carrieres")
    @Timed
    public ResponseEntity<Carriere> updateCarriere(@Valid @RequestBody Carriere carriere) throws URISyntaxException {
        log.debug("REST request to update Carriere : {}", carriere);
        if (carriere.getId() == null) {
            return createCarriere(carriere);
        }
        Carriere result = carriereRepository.save(carriere);
        carriereSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, carriere.getId().toString()))
            .body(result);
    }

    /**
     * GET  /carrieres : get all the carrieres.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of carrieres in body
     */
    @GetMapping("/carrieres")
    @Timed
    public ResponseEntity<List<Carriere>> getAllCarrieres(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Carrieres");
        Page<Carriere> page = carriereRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/carrieres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /carrieres/:id : get the "id" carriere.
     *
     * @param id the id of the carriere to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carriere, or with status 404 (Not Found)
     */
    @GetMapping("/carrieres/{id}")
    @Timed
    public ResponseEntity<Carriere> getCarriere(@PathVariable Long id) {
        log.debug("REST request to get Carriere : {}", id);
        Carriere carriere = carriereRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(carriere));
    }

    /**
     * DELETE  /carrieres/:id : delete the "id" carriere.
     *
     * @param id the id of the carriere to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/carrieres/{id}")
    @Timed
    public ResponseEntity<Void> deleteCarriere(@PathVariable Long id) {
        log.debug("REST request to delete Carriere : {}", id);
        carriereRepository.delete(id);
        carriereSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/carrieres?query=:query : search for the carriere corresponding
     * to the query.
     *
     * @param query the query of the carriere search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/carrieres")
    @Timed
    public ResponseEntity<List<Carriere>> searchCarrieres(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Carrieres for query {}", query);
        Page<Carriere> page = carriereSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/carrieres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
