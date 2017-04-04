package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.TypeFormateur;

import org.qualimaker.repository.TypeFormateurRepository;
import org.qualimaker.repository.search.TypeFormateurSearchRepository;
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
 * REST controller for managing TypeFormateur.
 */
@RestController
@RequestMapping("/api")
public class TypeFormateurResource {

    private final Logger log = LoggerFactory.getLogger(TypeFormateurResource.class);

    private static final String ENTITY_NAME = "typeFormateur";
        
    private final TypeFormateurRepository typeFormateurRepository;

    private final TypeFormateurSearchRepository typeFormateurSearchRepository;

    public TypeFormateurResource(TypeFormateurRepository typeFormateurRepository, TypeFormateurSearchRepository typeFormateurSearchRepository) {
        this.typeFormateurRepository = typeFormateurRepository;
        this.typeFormateurSearchRepository = typeFormateurSearchRepository;
    }

    /**
     * POST  /type-formateurs : Create a new typeFormateur.
     *
     * @param typeFormateur the typeFormateur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeFormateur, or with status 400 (Bad Request) if the typeFormateur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-formateurs")
    @Timed
    public ResponseEntity<TypeFormateur> createTypeFormateur(@RequestBody TypeFormateur typeFormateur) throws URISyntaxException {
        log.debug("REST request to save TypeFormateur : {}", typeFormateur);
        if (typeFormateur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new typeFormateur cannot already have an ID")).body(null);
        }
        TypeFormateur result = typeFormateurRepository.save(typeFormateur);
        typeFormateurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-formateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-formateurs : Updates an existing typeFormateur.
     *
     * @param typeFormateur the typeFormateur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeFormateur,
     * or with status 400 (Bad Request) if the typeFormateur is not valid,
     * or with status 500 (Internal Server Error) if the typeFormateur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-formateurs")
    @Timed
    public ResponseEntity<TypeFormateur> updateTypeFormateur(@RequestBody TypeFormateur typeFormateur) throws URISyntaxException {
        log.debug("REST request to update TypeFormateur : {}", typeFormateur);
        if (typeFormateur.getId() == null) {
            return createTypeFormateur(typeFormateur);
        }
        TypeFormateur result = typeFormateurRepository.save(typeFormateur);
        typeFormateurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeFormateur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-formateurs : get all the typeFormateurs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeFormateurs in body
     */
    @GetMapping("/type-formateurs")
    @Timed
    public List<TypeFormateur> getAllTypeFormateurs() {
        log.debug("REST request to get all TypeFormateurs");
        List<TypeFormateur> typeFormateurs = typeFormateurRepository.findAll();
        return typeFormateurs;
    }

    /**
     * GET  /type-formateurs/:id : get the "id" typeFormateur.
     *
     * @param id the id of the typeFormateur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeFormateur, or with status 404 (Not Found)
     */
    @GetMapping("/type-formateurs/{id}")
    @Timed
    public ResponseEntity<TypeFormateur> getTypeFormateur(@PathVariable Long id) {
        log.debug("REST request to get TypeFormateur : {}", id);
        TypeFormateur typeFormateur = typeFormateurRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(typeFormateur));
    }

    /**
     * DELETE  /type-formateurs/:id : delete the "id" typeFormateur.
     *
     * @param id the id of the typeFormateur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-formateurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeFormateur(@PathVariable Long id) {
        log.debug("REST request to delete TypeFormateur : {}", id);
        typeFormateurRepository.delete(id);
        typeFormateurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-formateurs?query=:query : search for the typeFormateur corresponding
     * to the query.
     *
     * @param query the query of the typeFormateur search 
     * @return the result of the search
     */
    @GetMapping("/_search/type-formateurs")
    @Timed
    public List<TypeFormateur> searchTypeFormateurs(@RequestParam String query) {
        log.debug("REST request to search TypeFormateurs for query {}", query);
        return StreamSupport
            .stream(typeFormateurSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
