package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.TypeDocumentation;

import org.qualimaker.repository.TypeDocumentationRepository;
import org.qualimaker.repository.search.TypeDocumentationSearchRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TypeDocumentation.
 */
@RestController
@RequestMapping("/api")
public class TypeDocumentationResource {

    private final Logger log = LoggerFactory.getLogger(TypeDocumentationResource.class);

    private static final String ENTITY_NAME = "typeDocumentation";
        
    private final TypeDocumentationRepository typeDocumentationRepository;

    private final TypeDocumentationSearchRepository typeDocumentationSearchRepository;

    public TypeDocumentationResource(TypeDocumentationRepository typeDocumentationRepository, TypeDocumentationSearchRepository typeDocumentationSearchRepository) {
        this.typeDocumentationRepository = typeDocumentationRepository;
        this.typeDocumentationSearchRepository = typeDocumentationSearchRepository;
    }

    /**
     * POST  /type-documentations : Create a new typeDocumentation.
     *
     * @param typeDocumentation the typeDocumentation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeDocumentation, or with status 400 (Bad Request) if the typeDocumentation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-documentations")
    @Timed
    public ResponseEntity<TypeDocumentation> createTypeDocumentation(@Valid @RequestBody TypeDocumentation typeDocumentation) throws URISyntaxException {
        log.debug("REST request to save TypeDocumentation : {}", typeDocumentation);
        if (typeDocumentation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new typeDocumentation cannot already have an ID")).body(null);
        }
        TypeDocumentation result = typeDocumentationRepository.save(typeDocumentation);
        typeDocumentationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-documentations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-documentations : Updates an existing typeDocumentation.
     *
     * @param typeDocumentation the typeDocumentation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeDocumentation,
     * or with status 400 (Bad Request) if the typeDocumentation is not valid,
     * or with status 500 (Internal Server Error) if the typeDocumentation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-documentations")
    @Timed
    public ResponseEntity<TypeDocumentation> updateTypeDocumentation(@Valid @RequestBody TypeDocumentation typeDocumentation) throws URISyntaxException {
        log.debug("REST request to update TypeDocumentation : {}", typeDocumentation);
        if (typeDocumentation.getId() == null) {
            return createTypeDocumentation(typeDocumentation);
        }
        TypeDocumentation result = typeDocumentationRepository.save(typeDocumentation);
        typeDocumentationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeDocumentation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-documentations : get all the typeDocumentations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of typeDocumentations in body
     */
    @GetMapping("/type-documentations")
    @Timed
    public ResponseEntity<List<TypeDocumentation>> getAllTypeDocumentations(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TypeDocumentations");
        Page<TypeDocumentation> page = typeDocumentationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/type-documentations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /type-documentations/:id : get the "id" typeDocumentation.
     *
     * @param id the id of the typeDocumentation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeDocumentation, or with status 404 (Not Found)
     */
    @GetMapping("/type-documentations/{id}")
    @Timed
    public ResponseEntity<TypeDocumentation> getTypeDocumentation(@PathVariable Long id) {
        log.debug("REST request to get TypeDocumentation : {}", id);
        TypeDocumentation typeDocumentation = typeDocumentationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(typeDocumentation));
    }

    /**
     * DELETE  /type-documentations/:id : delete the "id" typeDocumentation.
     *
     * @param id the id of the typeDocumentation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-documentations/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeDocumentation(@PathVariable Long id) {
        log.debug("REST request to delete TypeDocumentation : {}", id);
        typeDocumentationRepository.delete(id);
        typeDocumentationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-documentations?query=:query : search for the typeDocumentation corresponding
     * to the query.
     *
     * @param query the query of the typeDocumentation search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/type-documentations")
    @Timed
    public ResponseEntity<List<TypeDocumentation>> searchTypeDocumentations(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TypeDocumentations for query {}", query);
        Page<TypeDocumentation> page = typeDocumentationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/type-documentations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
