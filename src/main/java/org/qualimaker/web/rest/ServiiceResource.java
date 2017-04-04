package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Serviice;

import org.qualimaker.repository.ServiiceRepository;
import org.qualimaker.repository.search.ServiiceSearchRepository;
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
 * REST controller for managing Serviice.
 */
@RestController
@RequestMapping("/api")
public class ServiiceResource {

    private final Logger log = LoggerFactory.getLogger(ServiiceResource.class);

    private static final String ENTITY_NAME = "serviice";
        
    private final ServiiceRepository serviiceRepository;

    private final ServiiceSearchRepository serviiceSearchRepository;

    public ServiiceResource(ServiiceRepository serviiceRepository, ServiiceSearchRepository serviiceSearchRepository) {
        this.serviiceRepository = serviiceRepository;
        this.serviiceSearchRepository = serviiceSearchRepository;
    }

    /**
     * POST  /serviices : Create a new serviice.
     *
     * @param serviice the serviice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviice, or with status 400 (Bad Request) if the serviice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/serviices")
    @Timed
    public ResponseEntity<Serviice> createServiice(@RequestBody Serviice serviice) throws URISyntaxException {
        log.debug("REST request to save Serviice : {}", serviice);
        if (serviice.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new serviice cannot already have an ID")).body(null);
        }
        Serviice result = serviiceRepository.save(serviice);
        serviiceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/serviices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /serviices : Updates an existing serviice.
     *
     * @param serviice the serviice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviice,
     * or with status 400 (Bad Request) if the serviice is not valid,
     * or with status 500 (Internal Server Error) if the serviice couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/serviices")
    @Timed
    public ResponseEntity<Serviice> updateServiice(@RequestBody Serviice serviice) throws URISyntaxException {
        log.debug("REST request to update Serviice : {}", serviice);
        if (serviice.getId() == null) {
            return createServiice(serviice);
        }
        Serviice result = serviiceRepository.save(serviice);
        serviiceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /serviices : get all the serviices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviices in body
     */
    @GetMapping("/serviices")
    @Timed
    public ResponseEntity<List<Serviice>> getAllServiices(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Serviices");
        Page<Serviice> page = serviiceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/serviices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /serviices/:id : get the "id" serviice.
     *
     * @param id the id of the serviice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviice, or with status 404 (Not Found)
     */
    @GetMapping("/serviices/{id}")
    @Timed
    public ResponseEntity<Serviice> getServiice(@PathVariable Long id) {
        log.debug("REST request to get Serviice : {}", id);
        Serviice serviice = serviiceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(serviice));
    }

    /**
     * DELETE  /serviices/:id : delete the "id" serviice.
     *
     * @param id the id of the serviice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/serviices/{id}")
    @Timed
    public ResponseEntity<Void> deleteServiice(@PathVariable Long id) {
        log.debug("REST request to delete Serviice : {}", id);
        serviiceRepository.delete(id);
        serviiceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/serviices?query=:query : search for the serviice corresponding
     * to the query.
     *
     * @param query the query of the serviice search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/serviices")
    @Timed
    public ResponseEntity<List<Serviice>> searchServiices(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Serviices for query {}", query);
        Page<Serviice> page = serviiceSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/serviices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
