package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Natureformation;

import org.qualimaker.repository.NatureformationRepository;
import org.qualimaker.repository.search.NatureformationSearchRepository;
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
 * REST controller for managing Natureformation.
 */
@RestController
@RequestMapping("/api")
public class NatureformationResource {

    private final Logger log = LoggerFactory.getLogger(NatureformationResource.class);

    private static final String ENTITY_NAME = "natureformation";
        
    private final NatureformationRepository natureformationRepository;

    private final NatureformationSearchRepository natureformationSearchRepository;

    public NatureformationResource(NatureformationRepository natureformationRepository, NatureformationSearchRepository natureformationSearchRepository) {
        this.natureformationRepository = natureformationRepository;
        this.natureformationSearchRepository = natureformationSearchRepository;
    }

    /**
     * POST  /natureformations : Create a new natureformation.
     *
     * @param natureformation the natureformation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new natureformation, or with status 400 (Bad Request) if the natureformation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/natureformations")
    @Timed
    public ResponseEntity<Natureformation> createNatureformation(@RequestBody Natureformation natureformation) throws URISyntaxException {
        log.debug("REST request to save Natureformation : {}", natureformation);
        if (natureformation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new natureformation cannot already have an ID")).body(null);
        }
        Natureformation result = natureformationRepository.save(natureformation);
        natureformationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/natureformations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /natureformations : Updates an existing natureformation.
     *
     * @param natureformation the natureformation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated natureformation,
     * or with status 400 (Bad Request) if the natureformation is not valid,
     * or with status 500 (Internal Server Error) if the natureformation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/natureformations")
    @Timed
    public ResponseEntity<Natureformation> updateNatureformation(@RequestBody Natureformation natureformation) throws URISyntaxException {
        log.debug("REST request to update Natureformation : {}", natureformation);
        if (natureformation.getId() == null) {
            return createNatureformation(natureformation);
        }
        Natureformation result = natureformationRepository.save(natureformation);
        natureformationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, natureformation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /natureformations : get all the natureformations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of natureformations in body
     */
    @GetMapping("/natureformations")
    @Timed
    public List<Natureformation> getAllNatureformations() {
        log.debug("REST request to get all Natureformations");
        List<Natureformation> natureformations = natureformationRepository.findAll();
        return natureformations;
    }

    /**
     * GET  /natureformations/:id : get the "id" natureformation.
     *
     * @param id the id of the natureformation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the natureformation, or with status 404 (Not Found)
     */
    @GetMapping("/natureformations/{id}")
    @Timed
    public ResponseEntity<Natureformation> getNatureformation(@PathVariable Long id) {
        log.debug("REST request to get Natureformation : {}", id);
        Natureformation natureformation = natureformationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(natureformation));
    }

    /**
     * DELETE  /natureformations/:id : delete the "id" natureformation.
     *
     * @param id the id of the natureformation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/natureformations/{id}")
    @Timed
    public ResponseEntity<Void> deleteNatureformation(@PathVariable Long id) {
        log.debug("REST request to delete Natureformation : {}", id);
        natureformationRepository.delete(id);
        natureformationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/natureformations?query=:query : search for the natureformation corresponding
     * to the query.
     *
     * @param query the query of the natureformation search 
     * @return the result of the search
     */
    @GetMapping("/_search/natureformations")
    @Timed
    public List<Natureformation> searchNatureformations(@RequestParam String query) {
        log.debug("REST request to search Natureformations for query {}", query);
        return StreamSupport
            .stream(natureformationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
