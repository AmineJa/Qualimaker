package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Fichierjoint;

import org.qualimaker.repository.FichierjointRepository;
import org.qualimaker.repository.search.FichierjointSearchRepository;
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
 * REST controller for managing Fichierjoint.
 */
@RestController
@RequestMapping("/api")
public class FichierjointResource {

    private final Logger log = LoggerFactory.getLogger(FichierjointResource.class);

    private static final String ENTITY_NAME = "fichierjoint";
        
    private final FichierjointRepository fichierjointRepository;

    private final FichierjointSearchRepository fichierjointSearchRepository;

    public FichierjointResource(FichierjointRepository fichierjointRepository, FichierjointSearchRepository fichierjointSearchRepository) {
        this.fichierjointRepository = fichierjointRepository;
        this.fichierjointSearchRepository = fichierjointSearchRepository;
    }

    /**
     * POST  /fichierjoints : Create a new fichierjoint.
     *
     * @param fichierjoint the fichierjoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fichierjoint, or with status 400 (Bad Request) if the fichierjoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fichierjoints")
    @Timed
    public ResponseEntity<Fichierjoint> createFichierjoint(@RequestBody Fichierjoint fichierjoint) throws URISyntaxException {
        log.debug("REST request to save Fichierjoint : {}", fichierjoint);
        if (fichierjoint.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fichierjoint cannot already have an ID")).body(null);
        }
        Fichierjoint result = fichierjointRepository.save(fichierjoint);
        fichierjointSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fichierjoints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fichierjoints : Updates an existing fichierjoint.
     *
     * @param fichierjoint the fichierjoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fichierjoint,
     * or with status 400 (Bad Request) if the fichierjoint is not valid,
     * or with status 500 (Internal Server Error) if the fichierjoint couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fichierjoints")
    @Timed
    public ResponseEntity<Fichierjoint> updateFichierjoint(@RequestBody Fichierjoint fichierjoint) throws URISyntaxException {
        log.debug("REST request to update Fichierjoint : {}", fichierjoint);
        if (fichierjoint.getId() == null) {
            return createFichierjoint(fichierjoint);
        }
        Fichierjoint result = fichierjointRepository.save(fichierjoint);
        fichierjointSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fichierjoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fichierjoints : get all the fichierjoints.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fichierjoints in body
     */
    @GetMapping("/fichierjoints")
    @Timed
    public List<Fichierjoint> getAllFichierjoints() {
        log.debug("REST request to get all Fichierjoints");
        List<Fichierjoint> fichierjoints = fichierjointRepository.findAll();
        return fichierjoints;
    }

    /**
     * GET  /fichierjoints/:id : get the "id" fichierjoint.
     *
     * @param id the id of the fichierjoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fichierjoint, or with status 404 (Not Found)
     */
    @GetMapping("/fichierjoints/{id}")
    @Timed
    public ResponseEntity<Fichierjoint> getFichierjoint(@PathVariable Long id) {
        log.debug("REST request to get Fichierjoint : {}", id);
        Fichierjoint fichierjoint = fichierjointRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fichierjoint));
    }

    /**
     * DELETE  /fichierjoints/:id : delete the "id" fichierjoint.
     *
     * @param id the id of the fichierjoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fichierjoints/{id}")
    @Timed
    public ResponseEntity<Void> deleteFichierjoint(@PathVariable Long id) {
        log.debug("REST request to delete Fichierjoint : {}", id);
        fichierjointRepository.delete(id);
        fichierjointSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/fichierjoints?query=:query : search for the fichierjoint corresponding
     * to the query.
     *
     * @param query the query of the fichierjoint search 
     * @return the result of the search
     */
    @GetMapping("/_search/fichierjoints")
    @Timed
    public List<Fichierjoint> searchFichierjoints(@RequestParam String query) {
        log.debug("REST request to search Fichierjoints for query {}", query);
        return StreamSupport
            .stream(fichierjointSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
