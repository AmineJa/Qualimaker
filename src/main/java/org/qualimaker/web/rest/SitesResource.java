package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Sites;

import org.qualimaker.repository.SitesRepository;
import org.qualimaker.repository.search.SitesSearchRepository;
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
 * REST controller for managing Sites.
 */
@RestController
@RequestMapping("/api")
public class SitesResource {

    private final Logger log = LoggerFactory.getLogger(SitesResource.class);

    private static final String ENTITY_NAME = "sites";
        
    private final SitesRepository sitesRepository;

    private final SitesSearchRepository sitesSearchRepository;

    public SitesResource(SitesRepository sitesRepository, SitesSearchRepository sitesSearchRepository) {
        this.sitesRepository = sitesRepository;
        this.sitesSearchRepository = sitesSearchRepository;
    }

    /**
     * POST  /sites : Create a new sites.
     *
     * @param sites the sites to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sites, or with status 400 (Bad Request) if the sites has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sites")
    @Timed
    public ResponseEntity<Sites> createSites(@RequestBody Sites sites) throws URISyntaxException {
        log.debug("REST request to save Sites : {}", sites);
        if (sites.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sites cannot already have an ID")).body(null);
        }
        Sites result = sitesRepository.save(sites);
        sitesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sites : Updates an existing sites.
     *
     * @param sites the sites to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sites,
     * or with status 400 (Bad Request) if the sites is not valid,
     * or with status 500 (Internal Server Error) if the sites couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sites")
    @Timed
    public ResponseEntity<Sites> updateSites(@RequestBody Sites sites) throws URISyntaxException {
        log.debug("REST request to update Sites : {}", sites);
        if (sites.getId() == null) {
            return createSites(sites);
        }
        Sites result = sitesRepository.save(sites);
        sitesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sites.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sites : get all the sites.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sites in body
     */
    @GetMapping("/sites")
    @Timed
    public ResponseEntity<List<Sites>> getAllSites(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Sites");
        Page<Sites> page = sitesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sites/:id : get the "id" sites.
     *
     * @param id the id of the sites to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sites, or with status 404 (Not Found)
     */
    @GetMapping("/sites/{id}")
    @Timed
    public ResponseEntity<Sites> getSites(@PathVariable Long id) {
        log.debug("REST request to get Sites : {}", id);
        Sites sites = sitesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sites));
    }

    /**
     * DELETE  /sites/:id : delete the "id" sites.
     *
     * @param id the id of the sites to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sites/{id}")
    @Timed
    public ResponseEntity<Void> deleteSites(@PathVariable Long id) {
        log.debug("REST request to delete Sites : {}", id);
        sitesRepository.delete(id);
        sitesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sites?query=:query : search for the sites corresponding
     * to the query.
     *
     * @param query the query of the sites search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sites")
    @Timed
    public ResponseEntity<List<Sites>> searchSites(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Sites for query {}", query);
        Page<Sites> page = sitesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
