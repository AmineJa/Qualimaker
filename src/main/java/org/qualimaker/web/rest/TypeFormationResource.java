package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.qualimaker.repository.TypeFormationRepository;
import org.qualimaker.repository.search.TypeFormationSearchRepository;
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
 * REST controller for managing TypeFormation.
 */
@RestController
@RequestMapping("/api")
public class TypeFormationResource {

    private final Logger log = LoggerFactory.getLogger(TypeFormationResource.class);

    private static final String ENTITY_NAME = "typeFormation";

    private final TypeFormationRepository typeFormationRepository;

    private final TypeFormationSearchRepository typeFormationSearchRepository;

    public TypeFormationResource(TypeFormationRepository typeFormationRepository, TypeFormationSearchRepository typeFormationSearchRepository) {
        this.typeFormationRepository = typeFormationRepository;
        this.typeFormationSearchRepository = typeFormationSearchRepository;
    }

    /**
     * POST  /type-formations : Create a new typeFormation.
     *
     * @param typeFormation the typeFormation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeFormation, or with status 400 (Bad Request) if the typeFormation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-formations")
    @Timed
    public ResponseEntity<TypeFormation> createTypeFormation(@RequestBody TypeFormation typeFormation) throws URISyntaxException {
        log.debug("REST request to save TypeFormation : {}", typeFormation);
        if (typeFormation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new typeFormation cannot already have an ID")).body(null);
        }
        TypeFormation result = typeFormationRepository.save(typeFormation);
        typeFormationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-formations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-formations : Updates an existing typeFormation.
     *
     * @param typeFormation the typeFormation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeFormation,
     * or with status 400 (Bad Request) if the typeFormation is not valid,
     * or with status 500 (Internal Server Error) if the typeFormation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-formations")
    @Timed
    public ResponseEntity<TypeFormation> updateTypeFormation(@RequestBody TypeFormation typeFormation) throws URISyntaxException {
        log.debug("REST request to update TypeFormation : {}", typeFormation);
        if (typeFormation.getId() == null) {
            return createTypeFormation(typeFormation);
        }
        TypeFormation result = typeFormationRepository.save(typeFormation);
        typeFormationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeFormation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-formations : get all the typeFormations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeFormations in body
     */
    @GetMapping("/type-formations")
    @Timed
    public List<TypeFormation> getAllTypeFormations() {
        log.debug("REST request to get all TypeFormations");
        List<TypeFormation> typeFormations = typeFormationRepository.findAll();
        return typeFormations;
    }

    /**
     * GET  /type-formations/:id : get the "id" typeFormation.
     *
     * @param id the id of the typeFormation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeFormation, or with status 404 (Not Found)
     */
    @GetMapping("/type-formations/{id}")
    @Timed
    public ResponseEntity<TypeFormation> getTypeFormation(@PathVariable Long id) {
        log.debug("REST request to get TypeFormation : {}", id);
        TypeFormation typeFormation = typeFormationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(typeFormation));
    }

    /**
     * DELETE  /type-formations/:id : delete the "id" typeFormation.
     *
     * @param id the id of the typeFormation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-formations/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeFormation(@PathVariable Long id) {
        log.debug("REST request to delete TypeFormation : {}", id);
        typeFormationRepository.delete(id);
        typeFormationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-formations?query=:query : search for the typeFormation corresponding
     * to the query.
     *
     * @param query the query of the typeFormation search
     * @return the result of the search
     */
    @GetMapping("/_search/type-formations")
    @Timed
    public List<TypeFormation> searchTypeFormations(@RequestParam String query) {
        log.debug("REST request to search TypeFormations for query {}", query);
        return StreamSupport
            .stream(typeFormationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
