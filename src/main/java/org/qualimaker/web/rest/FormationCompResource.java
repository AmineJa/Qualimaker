package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.FormationComp;

import org.qualimaker.repository.FormationCompRepository;
import org.qualimaker.repository.search.FormationCompSearchRepository;
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
 * REST controller for managing FormationComp.
 */
@RestController
@RequestMapping("/api")
public class FormationCompResource {

    private final Logger log = LoggerFactory.getLogger(FormationCompResource.class);

    private static final String ENTITY_NAME = "formationComp";
        
    private final FormationCompRepository formationCompRepository;

    private final FormationCompSearchRepository formationCompSearchRepository;

    public FormationCompResource(FormationCompRepository formationCompRepository, FormationCompSearchRepository formationCompSearchRepository) {
        this.formationCompRepository = formationCompRepository;
        this.formationCompSearchRepository = formationCompSearchRepository;
    }

    /**
     * POST  /formation-comps : Create a new formationComp.
     *
     * @param formationComp the formationComp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formationComp, or with status 400 (Bad Request) if the formationComp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/formation-comps")
    @Timed
    public ResponseEntity<FormationComp> createFormationComp(@RequestBody FormationComp formationComp) throws URISyntaxException {
        log.debug("REST request to save FormationComp : {}", formationComp);
        if (formationComp.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new formationComp cannot already have an ID")).body(null);
        }
        FormationComp result = formationCompRepository.save(formationComp);
        formationCompSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/formation-comps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /formation-comps : Updates an existing formationComp.
     *
     * @param formationComp the formationComp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formationComp,
     * or with status 400 (Bad Request) if the formationComp is not valid,
     * or with status 500 (Internal Server Error) if the formationComp couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/formation-comps")
    @Timed
    public ResponseEntity<FormationComp> updateFormationComp(@RequestBody FormationComp formationComp) throws URISyntaxException {
        log.debug("REST request to update FormationComp : {}", formationComp);
        if (formationComp.getId() == null) {
            return createFormationComp(formationComp);
        }
        FormationComp result = formationCompRepository.save(formationComp);
        formationCompSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formationComp.getId().toString()))
            .body(result);
    }

    /**
     * GET  /formation-comps : get all the formationComps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of formationComps in body
     */
    @GetMapping("/formation-comps")
    @Timed
    public List<FormationComp> getAllFormationComps() {
        log.debug("REST request to get all FormationComps");
        List<FormationComp> formationComps = formationCompRepository.findAll();
        return formationComps;
    }

    /**
     * GET  /formation-comps/:id : get the "id" formationComp.
     *
     * @param id the id of the formationComp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formationComp, or with status 404 (Not Found)
     */
    @GetMapping("/formation-comps/{id}")
    @Timed
    public ResponseEntity<FormationComp> getFormationComp(@PathVariable Long id) {
        log.debug("REST request to get FormationComp : {}", id);
        FormationComp formationComp = formationCompRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(formationComp));
    }

    /**
     * DELETE  /formation-comps/:id : delete the "id" formationComp.
     *
     * @param id the id of the formationComp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/formation-comps/{id}")
    @Timed
    public ResponseEntity<Void> deleteFormationComp(@PathVariable Long id) {
        log.debug("REST request to delete FormationComp : {}", id);
        formationCompRepository.delete(id);
        formationCompSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/formation-comps?query=:query : search for the formationComp corresponding
     * to the query.
     *
     * @param query the query of the formationComp search 
     * @return the result of the search
     */
    @GetMapping("/_search/formation-comps")
    @Timed
    public List<FormationComp> searchFormationComps(@RequestParam String query) {
        log.debug("REST request to search FormationComps for query {}", query);
        return StreamSupport
            .stream(formationCompSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
