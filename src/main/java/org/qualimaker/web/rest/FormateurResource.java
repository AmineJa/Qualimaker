package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Formateur;

import org.qualimaker.repository.FormateurRepository;
import org.qualimaker.repository.search.FormateurSearchRepository;
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
 * REST controller for managing Formateur.
 */
@RestController
@RequestMapping("/api")
public class FormateurResource {

    private final Logger log = LoggerFactory.getLogger(FormateurResource.class);

    private static final String ENTITY_NAME = "formateur";
        
    private final FormateurRepository formateurRepository;

    private final FormateurSearchRepository formateurSearchRepository;

    public FormateurResource(FormateurRepository formateurRepository, FormateurSearchRepository formateurSearchRepository) {
        this.formateurRepository = formateurRepository;
        this.formateurSearchRepository = formateurSearchRepository;
    }

    /**
     * POST  /formateurs : Create a new formateur.
     *
     * @param formateur the formateur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formateur, or with status 400 (Bad Request) if the formateur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/formateurs")
    @Timed
    public ResponseEntity<Formateur> createFormateur(@RequestBody Formateur formateur) throws URISyntaxException {
        log.debug("REST request to save Formateur : {}", formateur);
        if (formateur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new formateur cannot already have an ID")).body(null);
        }
        Formateur result = formateurRepository.save(formateur);
        formateurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/formateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /formateurs : Updates an existing formateur.
     *
     * @param formateur the formateur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formateur,
     * or with status 400 (Bad Request) if the formateur is not valid,
     * or with status 500 (Internal Server Error) if the formateur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/formateurs")
    @Timed
    public ResponseEntity<Formateur> updateFormateur(@RequestBody Formateur formateur) throws URISyntaxException {
        log.debug("REST request to update Formateur : {}", formateur);
        if (formateur.getId() == null) {
            return createFormateur(formateur);
        }
        Formateur result = formateurRepository.save(formateur);
        formateurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formateur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /formateurs : get all the formateurs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of formateurs in body
     */
    @GetMapping("/formateurs")
    @Timed
    public List<Formateur> getAllFormateurs() {
        log.debug("REST request to get all Formateurs");
        List<Formateur> formateurs = formateurRepository.findAll();
        return formateurs;
    }

    /**
     * GET  /formateurs/:id : get the "id" formateur.
     *
     * @param id the id of the formateur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formateur, or with status 404 (Not Found)
     */
    @GetMapping("/formateurs/{id}")
    @Timed
    public ResponseEntity<Formateur> getFormateur(@PathVariable Long id) {
        log.debug("REST request to get Formateur : {}", id);
        Formateur formateur = formateurRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(formateur));
    }

    /**
     * DELETE  /formateurs/:id : delete the "id" formateur.
     *
     * @param id the id of the formateur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/formateurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteFormateur(@PathVariable Long id) {
        log.debug("REST request to delete Formateur : {}", id);
        formateurRepository.delete(id);
        formateurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/formateurs?query=:query : search for the formateur corresponding
     * to the query.
     *
     * @param query the query of the formateur search 
     * @return the result of the search
     */
    @GetMapping("/_search/formateurs")
    @Timed
    public List<Formateur> searchFormateurs(@RequestParam String query) {
        log.debug("REST request to search Formateurs for query {}", query);
        return StreamSupport
            .stream(formateurSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
