package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.TypeConge;

import org.qualimaker.repository.TypeCongeRepository;
import org.qualimaker.repository.search.TypeCongeSearchRepository;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TypeConge.
 */
@RestController
@RequestMapping("/api")
public class TypeCongeResource {

    private final Logger log = LoggerFactory.getLogger(TypeCongeResource.class);

    private static final String ENTITY_NAME = "typeConge";
        
    private final TypeCongeRepository typeCongeRepository;

    private final TypeCongeSearchRepository typeCongeSearchRepository;

    public TypeCongeResource(TypeCongeRepository typeCongeRepository, TypeCongeSearchRepository typeCongeSearchRepository) {
        this.typeCongeRepository = typeCongeRepository;
        this.typeCongeSearchRepository = typeCongeSearchRepository;
    }

    /**
     * POST  /type-conges : Create a new typeConge.
     *
     * @param typeConge the typeConge to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeConge, or with status 400 (Bad Request) if the typeConge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-conges")
    @Timed
    public ResponseEntity<TypeConge> createTypeConge(@RequestBody TypeConge typeConge) throws URISyntaxException {
        log.debug("REST request to save TypeConge : {}", typeConge);
        if (typeConge.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new typeConge cannot already have an ID")).body(null);
        }
        TypeConge result = typeCongeRepository.save(typeConge);
        typeCongeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-conges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-conges : Updates an existing typeConge.
     *
     * @param typeConge the typeConge to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeConge,
     * or with status 400 (Bad Request) if the typeConge is not valid,
     * or with status 500 (Internal Server Error) if the typeConge couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-conges")
    @Timed
    public ResponseEntity<TypeConge> updateTypeConge(@RequestBody TypeConge typeConge) throws URISyntaxException {
        log.debug("REST request to update TypeConge : {}", typeConge);
        if (typeConge.getId() == null) {
            return createTypeConge(typeConge);
        }
        TypeConge result = typeCongeRepository.save(typeConge);
        typeCongeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeConge.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-conges : get all the typeConges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of typeConges in body
     */
    @GetMapping("/type-conges")
    @Timed
    public ResponseEntity<List<TypeConge>> getAllTypeConges(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TypeConges");
        Page<TypeConge> page = typeCongeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/type-conges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /type-conges/:id : get the "id" typeConge.
     *
     * @param id the id of the typeConge to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeConge, or with status 404 (Not Found)
     */
    @GetMapping("/type-conges/{id}")
    @Timed
    public ResponseEntity<TypeConge> getTypeConge(@PathVariable Long id) {
        log.debug("REST request to get TypeConge : {}", id);
        TypeConge typeConge = typeCongeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(typeConge));
    }

    /**
     * DELETE  /type-conges/:id : delete the "id" typeConge.
     *
     * @param id the id of the typeConge to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-conges/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeConge(@PathVariable Long id) {
        log.debug("REST request to delete TypeConge : {}", id);
        typeCongeRepository.delete(id);
        typeCongeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-conges?query=:query : search for the typeConge corresponding
     * to the query.
     *
     * @param query the query of the typeConge search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/type-conges")
    @Timed
    public ResponseEntity<List<TypeConge>> searchTypeConges(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TypeConges for query {}", query);
        Page<TypeConge> page = typeCongeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/type-conges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
