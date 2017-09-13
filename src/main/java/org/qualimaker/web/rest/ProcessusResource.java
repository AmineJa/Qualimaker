package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Processus;

import org.qualimaker.repository.ProcessusRepository;
import org.qualimaker.repository.search.ProcessusSearchRepository;
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
 * REST controller for managing Processus.
 */
@RestController
@RequestMapping("/api")
public class ProcessusResource {

    private final Logger log = LoggerFactory.getLogger(ProcessusResource.class);

    private static final String ENTITY_NAME = "processus";
        
    private final ProcessusRepository processusRepository;

    private final ProcessusSearchRepository processusSearchRepository;

    public ProcessusResource(ProcessusRepository processusRepository, ProcessusSearchRepository processusSearchRepository) {
        this.processusRepository = processusRepository;
        this.processusSearchRepository = processusSearchRepository;
    }

    /**
     * POST  /processuses : Create a new processus.
     *
     * @param processus the processus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new processus, or with status 400 (Bad Request) if the processus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/processuses")
    @Timed
    public ResponseEntity<Processus> createProcessus(@RequestBody Processus processus) throws URISyntaxException {
        log.debug("REST request to save Processus : {}", processus);
        if (processus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new processus cannot already have an ID")).body(null);
        }
        Processus result = processusRepository.save(processus);
        processusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/processuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /processuses : Updates an existing processus.
     *
     * @param processus the processus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated processus,
     * or with status 400 (Bad Request) if the processus is not valid,
     * or with status 500 (Internal Server Error) if the processus couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/processuses")
    @Timed
    public ResponseEntity<Processus> updateProcessus(@RequestBody Processus processus) throws URISyntaxException {
        log.debug("REST request to update Processus : {}", processus);
        if (processus.getId() == null) {
            return createProcessus(processus);
        }
        Processus result = processusRepository.save(processus);
        processusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, processus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /processuses : get all the processuses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of processuses in body
     */
    @GetMapping("/processuses")
    @Timed
    public ResponseEntity<List<Processus>> getAllProcessuses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Processuses");
        Page<Processus> page = processusRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/processuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /processuses/:id : get the "id" processus.
     *
     * @param id the id of the processus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the processus, or with status 404 (Not Found)
     */
    @GetMapping("/processuses/{id}")
    @Timed
    public ResponseEntity<Processus> getProcessus(@PathVariable Long id) {
        log.debug("REST request to get Processus : {}", id);
        Processus processus = processusRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(processus));
    }

    /**
     * DELETE  /processuses/:id : delete the "id" processus.
     *
     * @param id the id of the processus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/processuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcessus(@PathVariable Long id) {
        log.debug("REST request to delete Processus : {}", id);
        processusRepository.delete(id);
        processusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/processuses?query=:query : search for the processus corresponding
     * to the query.
     *
     * @param query the query of the processus search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/processuses")
    @Timed
    public ResponseEntity<List<Processus>> searchProcessuses(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Processuses for query {}", query);
        Page<Processus> page = processusSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/processuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
