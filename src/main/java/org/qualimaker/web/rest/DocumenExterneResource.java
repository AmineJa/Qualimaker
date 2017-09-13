package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.DocumenExterne;

import org.qualimaker.repository.DocumenExterneRepository;
import org.qualimaker.repository.search.DocumenExterneSearchRepository;
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
 * REST controller for managing DocumenExterne.
 */
@RestController
@RequestMapping("/api")
public class DocumenExterneResource {

    private final Logger log = LoggerFactory.getLogger(DocumenExterneResource.class);

    private static final String ENTITY_NAME = "documenExterne";
        
    private final DocumenExterneRepository documenExterneRepository;

    private final DocumenExterneSearchRepository documenExterneSearchRepository;

    public DocumenExterneResource(DocumenExterneRepository documenExterneRepository, DocumenExterneSearchRepository documenExterneSearchRepository) {
        this.documenExterneRepository = documenExterneRepository;
        this.documenExterneSearchRepository = documenExterneSearchRepository;
    }

    /**
     * POST  /documen-externes : Create a new documenExterne.
     *
     * @param documenExterne the documenExterne to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documenExterne, or with status 400 (Bad Request) if the documenExterne has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/documen-externes")
    @Timed
    public ResponseEntity<DocumenExterne> createDocumenExterne(@Valid @RequestBody DocumenExterne documenExterne) throws URISyntaxException {
        log.debug("REST request to save DocumenExterne : {}", documenExterne);
        if (documenExterne.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new documenExterne cannot already have an ID")).body(null);
        }
        DocumenExterne result = documenExterneRepository.save(documenExterne);
        documenExterneSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/documen-externes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /documen-externes : Updates an existing documenExterne.
     *
     * @param documenExterne the documenExterne to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documenExterne,
     * or with status 400 (Bad Request) if the documenExterne is not valid,
     * or with status 500 (Internal Server Error) if the documenExterne couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/documen-externes")
    @Timed
    public ResponseEntity<DocumenExterne> updateDocumenExterne(@Valid @RequestBody DocumenExterne documenExterne) throws URISyntaxException {
        log.debug("REST request to update DocumenExterne : {}", documenExterne);
        if (documenExterne.getId() == null) {
            return createDocumenExterne(documenExterne);
        }
        DocumenExterne result = documenExterneRepository.save(documenExterne);
        documenExterneSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documenExterne.getId().toString()))
            .body(result);
    }

    /**
     * GET  /documen-externes : get all the documenExternes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of documenExternes in body
     */
    @GetMapping("/documen-externes")
    @Timed
    public ResponseEntity<List<DocumenExterne>> getAllDocumenExternes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DocumenExternes");
        Page<DocumenExterne> page = documenExterneRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/documen-externes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /documen-externes/:id : get the "id" documenExterne.
     *
     * @param id the id of the documenExterne to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documenExterne, or with status 404 (Not Found)
     */
    @GetMapping("/documen-externes/{id}")
    @Timed
    public ResponseEntity<DocumenExterne> getDocumenExterne(@PathVariable Long id) {
        log.debug("REST request to get DocumenExterne : {}", id);
        DocumenExterne documenExterne = documenExterneRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(documenExterne));
    }

    /**
     * DELETE  /documen-externes/:id : delete the "id" documenExterne.
     *
     * @param id the id of the documenExterne to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/documen-externes/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocumenExterne(@PathVariable Long id) {
        log.debug("REST request to delete DocumenExterne : {}", id);
        documenExterneRepository.delete(id);
        documenExterneSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/documen-externes?query=:query : search for the documenExterne corresponding
     * to the query.
     *
     * @param query the query of the documenExterne search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/documen-externes")
    @Timed
    public ResponseEntity<List<DocumenExterne>> searchDocumenExternes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of DocumenExternes for query {}", query);
        Page<DocumenExterne> page = documenExterneSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/documen-externes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
