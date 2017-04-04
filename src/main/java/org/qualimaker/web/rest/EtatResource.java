package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Etat;

import org.qualimaker.repository.EtatRepository;
import org.qualimaker.repository.search.EtatSearchRepository;
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
 * REST controller for managing Etat.
 */
@RestController
@RequestMapping("/api")
public class EtatResource {

    private final Logger log = LoggerFactory.getLogger(EtatResource.class);

    private static final String ENTITY_NAME = "etat";
        
    private final EtatRepository etatRepository;

    private final EtatSearchRepository etatSearchRepository;

    public EtatResource(EtatRepository etatRepository, EtatSearchRepository etatSearchRepository) {
        this.etatRepository = etatRepository;
        this.etatSearchRepository = etatSearchRepository;
    }

    /**
     * POST  /etats : Create a new etat.
     *
     * @param etat the etat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new etat, or with status 400 (Bad Request) if the etat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/etats")
    @Timed
    public ResponseEntity<Etat> createEtat(@RequestBody Etat etat) throws URISyntaxException {
        log.debug("REST request to save Etat : {}", etat);
        if (etat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new etat cannot already have an ID")).body(null);
        }
        Etat result = etatRepository.save(etat);
        etatSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/etats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /etats : Updates an existing etat.
     *
     * @param etat the etat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated etat,
     * or with status 400 (Bad Request) if the etat is not valid,
     * or with status 500 (Internal Server Error) if the etat couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/etats")
    @Timed
    public ResponseEntity<Etat> updateEtat(@RequestBody Etat etat) throws URISyntaxException {
        log.debug("REST request to update Etat : {}", etat);
        if (etat.getId() == null) {
            return createEtat(etat);
        }
        Etat result = etatRepository.save(etat);
        etatSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, etat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /etats : get all the etats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of etats in body
     */
    @GetMapping("/etats")
    @Timed
    public List<Etat> getAllEtats() {
        log.debug("REST request to get all Etats");
        List<Etat> etats = etatRepository.findAll();
        return etats;
    }

    /**
     * GET  /etats/:id : get the "id" etat.
     *
     * @param id the id of the etat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the etat, or with status 404 (Not Found)
     */
    @GetMapping("/etats/{id}")
    @Timed
    public ResponseEntity<Etat> getEtat(@PathVariable Long id) {
        log.debug("REST request to get Etat : {}", id);
        Etat etat = etatRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(etat));
    }

    /**
     * DELETE  /etats/:id : delete the "id" etat.
     *
     * @param id the id of the etat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/etats/{id}")
    @Timed
    public ResponseEntity<Void> deleteEtat(@PathVariable Long id) {
        log.debug("REST request to delete Etat : {}", id);
        etatRepository.delete(id);
        etatSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/etats?query=:query : search for the etat corresponding
     * to the query.
     *
     * @param query the query of the etat search 
     * @return the result of the search
     */
    @GetMapping("/_search/etats")
    @Timed
    public List<Etat> searchEtats(@RequestParam String query) {
        log.debug("REST request to search Etats for query {}", query);
        return StreamSupport
            .stream(etatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
