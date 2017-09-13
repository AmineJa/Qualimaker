package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.DroitaccesDocument;

import org.qualimaker.repository.DroitaccesDocumentRepository;
import org.qualimaker.repository.search.DroitaccesDocumentSearchRepository;
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
 * REST controller for managing DroitaccesDocument.
 */
@RestController
@RequestMapping("/api")
public class DroitaccesDocumentResource {

    private final Logger log = LoggerFactory.getLogger(DroitaccesDocumentResource.class);

    private static final String ENTITY_NAME = "droitaccesDocument";
        
    private final DroitaccesDocumentRepository droitaccesDocumentRepository;

    private final DroitaccesDocumentSearchRepository droitaccesDocumentSearchRepository;

    public DroitaccesDocumentResource(DroitaccesDocumentRepository droitaccesDocumentRepository, DroitaccesDocumentSearchRepository droitaccesDocumentSearchRepository) {
        this.droitaccesDocumentRepository = droitaccesDocumentRepository;
        this.droitaccesDocumentSearchRepository = droitaccesDocumentSearchRepository;
    }

    /**
     * POST  /droitacces-documents : Create a new droitaccesDocument.
     *
     * @param droitaccesDocument the droitaccesDocument to create
     * @return the ResponseEntity with status 201 (Created) and with body the new droitaccesDocument, or with status 400 (Bad Request) if the droitaccesDocument has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/droitacces-documents")
    @Timed
    public ResponseEntity<DroitaccesDocument> createDroitaccesDocument(@Valid @RequestBody DroitaccesDocument droitaccesDocument) throws URISyntaxException {
        log.debug("REST request to save DroitaccesDocument : {}", droitaccesDocument);
        if (droitaccesDocument.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new droitaccesDocument cannot already have an ID")).body(null);
        }
        DroitaccesDocument result = droitaccesDocumentRepository.save(droitaccesDocument);
        droitaccesDocumentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/droitacces-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /droitacces-documents : Updates an existing droitaccesDocument.
     *
     * @param droitaccesDocument the droitaccesDocument to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated droitaccesDocument,
     * or with status 400 (Bad Request) if the droitaccesDocument is not valid,
     * or with status 500 (Internal Server Error) if the droitaccesDocument couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/droitacces-documents")
    @Timed
    public ResponseEntity<DroitaccesDocument> updateDroitaccesDocument(@Valid @RequestBody DroitaccesDocument droitaccesDocument) throws URISyntaxException {
        log.debug("REST request to update DroitaccesDocument : {}", droitaccesDocument);
        if (droitaccesDocument.getId() == null) {
            return createDroitaccesDocument(droitaccesDocument);
        }
        DroitaccesDocument result = droitaccesDocumentRepository.save(droitaccesDocument);
        droitaccesDocumentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, droitaccesDocument.getId().toString()))
            .body(result);
    }

    /**
     * GET  /droitacces-documents : get all the droitaccesDocuments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of droitaccesDocuments in body
     */
    @GetMapping("/droitacces-documents")
    @Timed
    public ResponseEntity<List<DroitaccesDocument>> getAllDroitaccesDocuments(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DroitaccesDocuments");
        Page<DroitaccesDocument> page = droitaccesDocumentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/droitacces-documents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /droitacces-documents/:id : get the "id" droitaccesDocument.
     *
     * @param id the id of the droitaccesDocument to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the droitaccesDocument, or with status 404 (Not Found)
     */
    @GetMapping("/droitacces-documents/{id}")
    @Timed
    public ResponseEntity<DroitaccesDocument> getDroitaccesDocument(@PathVariable Long id) {
        log.debug("REST request to get DroitaccesDocument : {}", id);
        DroitaccesDocument droitaccesDocument = droitaccesDocumentRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(droitaccesDocument));
    }

    /**
     * DELETE  /droitacces-documents/:id : delete the "id" droitaccesDocument.
     *
     * @param id the id of the droitaccesDocument to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/droitacces-documents/{id}")
    @Timed
    public ResponseEntity<Void> deleteDroitaccesDocument(@PathVariable Long id) {
        log.debug("REST request to delete DroitaccesDocument : {}", id);
        droitaccesDocumentRepository.delete(id);
        droitaccesDocumentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/droitacces-documents?query=:query : search for the droitaccesDocument corresponding
     * to the query.
     *
     * @param query the query of the droitaccesDocument search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/droitacces-documents")
    @Timed
    public ResponseEntity<List<DroitaccesDocument>> searchDroitaccesDocuments(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of DroitaccesDocuments for query {}", query);
        Page<DroitaccesDocument> page = droitaccesDocumentSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/droitacces-documents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
