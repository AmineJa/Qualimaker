package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Groupe;

import org.qualimaker.repository.GroupeRepository;
import org.qualimaker.repository.search.GroupeSearchRepository;
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
 * REST controller for managing Groupe.
 */
@RestController
@RequestMapping("/api")
public class GroupeResource {

    private final Logger log = LoggerFactory.getLogger(GroupeResource.class);

    private static final String ENTITY_NAME = "groupe";
        
    private final GroupeRepository groupeRepository;

    private final GroupeSearchRepository groupeSearchRepository;

    public GroupeResource(GroupeRepository groupeRepository, GroupeSearchRepository groupeSearchRepository) {
        this.groupeRepository = groupeRepository;
        this.groupeSearchRepository = groupeSearchRepository;
    }

    /**
     * POST  /groupes : Create a new groupe.
     *
     * @param groupe the groupe to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupe, or with status 400 (Bad Request) if the groupe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groupes")
    @Timed
    public ResponseEntity<Groupe> createGroupe(@RequestBody Groupe groupe) throws URISyntaxException {
        log.debug("REST request to save Groupe : {}", groupe);
        if (groupe.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new groupe cannot already have an ID")).body(null);
        }
        Groupe result = groupeRepository.save(groupe);
        groupeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/groupes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groupes : Updates an existing groupe.
     *
     * @param groupe the groupe to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupe,
     * or with status 400 (Bad Request) if the groupe is not valid,
     * or with status 500 (Internal Server Error) if the groupe couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groupes")
    @Timed
    public ResponseEntity<Groupe> updateGroupe(@RequestBody Groupe groupe) throws URISyntaxException {
        log.debug("REST request to update Groupe : {}", groupe);
        if (groupe.getId() == null) {
            return createGroupe(groupe);
        }
        Groupe result = groupeRepository.save(groupe);
        groupeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, groupe.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groupes : get all the groupes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of groupes in body
     */
    @GetMapping("/groupes")
    @Timed
    public List<Groupe> getAllGroupes() {
        log.debug("REST request to get all Groupes");
        List<Groupe> groupes = groupeRepository.findAll();
        return groupes;
    }

    /**
     * GET  /groupes/:id : get the "id" groupe.
     *
     * @param id the id of the groupe to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupe, or with status 404 (Not Found)
     */
    @GetMapping("/groupes/{id}")
    @Timed
    public ResponseEntity<Groupe> getGroupe(@PathVariable Long id) {
        log.debug("REST request to get Groupe : {}", id);
        Groupe groupe = groupeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(groupe));
    }

    /**
     * DELETE  /groupes/:id : delete the "id" groupe.
     *
     * @param id the id of the groupe to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groupes/{id}")
    @Timed
    public ResponseEntity<Void> deleteGroupe(@PathVariable Long id) {
        log.debug("REST request to delete Groupe : {}", id);
        groupeRepository.delete(id);
        groupeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/groupes?query=:query : search for the groupe corresponding
     * to the query.
     *
     * @param query the query of the groupe search 
     * @return the result of the search
     */
    @GetMapping("/_search/groupes")
    @Timed
    public List<Groupe> searchGroupes(@RequestParam String query) {
        log.debug("REST request to search Groupes for query {}", query);
        return StreamSupport
            .stream(groupeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
