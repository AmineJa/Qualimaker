package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Jour;

import org.qualimaker.repository.JourRepository;
import org.qualimaker.repository.search.JourSearchRepository;
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
 * REST controller for managing Jour.
 */
@RestController
@RequestMapping("/api")
public class JourResource {

    private final Logger log = LoggerFactory.getLogger(JourResource.class);

    private static final String ENTITY_NAME = "jour";
        
    private final JourRepository jourRepository;

    private final JourSearchRepository jourSearchRepository;

    public JourResource(JourRepository jourRepository, JourSearchRepository jourSearchRepository) {
        this.jourRepository = jourRepository;
        this.jourSearchRepository = jourSearchRepository;
    }

    /**
     * POST  /jours : Create a new jour.
     *
     * @param jour the jour to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jour, or with status 400 (Bad Request) if the jour has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/jours")
    @Timed
    public ResponseEntity<Jour> createJour(@RequestBody Jour jour) throws URISyntaxException {
        log.debug("REST request to save Jour : {}", jour);
        if (jour.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jour cannot already have an ID")).body(null);
        }
        Jour result = jourRepository.save(jour);
        jourSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/jours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jours : Updates an existing jour.
     *
     * @param jour the jour to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jour,
     * or with status 400 (Bad Request) if the jour is not valid,
     * or with status 500 (Internal Server Error) if the jour couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/jours")
    @Timed
    public ResponseEntity<Jour> updateJour(@RequestBody Jour jour) throws URISyntaxException {
        log.debug("REST request to update Jour : {}", jour);
        if (jour.getId() == null) {
            return createJour(jour);
        }
        Jour result = jourRepository.save(jour);
        jourSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jour.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jours : get all the jours.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jours in body
     */
    @GetMapping("/jours")
    @Timed
    public List<Jour> getAllJours() {
        log.debug("REST request to get all Jours");
        List<Jour> jours = jourRepository.findAll();
        return jours;
    }

    /**
     * GET  /jours/:id : get the "id" jour.
     *
     * @param id the id of the jour to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jour, or with status 404 (Not Found)
     */
    @GetMapping("/jours/{id}")
    @Timed
    public ResponseEntity<Jour> getJour(@PathVariable Long id) {
        log.debug("REST request to get Jour : {}", id);
        Jour jour = jourRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jour));
    }

    /**
     * DELETE  /jours/:id : delete the "id" jour.
     *
     * @param id the id of the jour to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/jours/{id}")
    @Timed
    public ResponseEntity<Void> deleteJour(@PathVariable Long id) {
        log.debug("REST request to delete Jour : {}", id);
        jourRepository.delete(id);
        jourSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/jours?query=:query : search for the jour corresponding
     * to the query.
     *
     * @param query the query of the jour search 
     * @return the result of the search
     */
    @GetMapping("/_search/jours")
    @Timed
    public List<Jour> searchJours(@RequestParam String query) {
        log.debug("REST request to search Jours for query {}", query);
        return StreamSupport
            .stream(jourSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
