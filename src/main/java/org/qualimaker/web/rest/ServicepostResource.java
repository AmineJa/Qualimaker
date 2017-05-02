package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Servicepost;

import org.qualimaker.repository.ServicepostRepository;
import org.qualimaker.repository.search.ServicepostSearchRepository;
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
 * REST controller for managing Servicepost.
 */
@RestController
@RequestMapping("/api")
public class ServicepostResource {

    private final Logger log = LoggerFactory.getLogger(ServicepostResource.class);

    private static final String ENTITY_NAME = "servicepost";
        
    private final ServicepostRepository servicepostRepository;

    private final ServicepostSearchRepository servicepostSearchRepository;

    public ServicepostResource(ServicepostRepository servicepostRepository, ServicepostSearchRepository servicepostSearchRepository) {
        this.servicepostRepository = servicepostRepository;
        this.servicepostSearchRepository = servicepostSearchRepository;
    }

    /**
     * POST  /serviceposts : Create a new servicepost.
     *
     * @param servicepost the servicepost to create
     * @return the ResponseEntity with status 201 (Created) and with body the new servicepost, or with status 400 (Bad Request) if the servicepost has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/serviceposts")
    @Timed
    public ResponseEntity<Servicepost> createServicepost(@RequestBody Servicepost servicepost) throws URISyntaxException {
        log.debug("REST request to save Servicepost : {}", servicepost);
        if (servicepost.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new servicepost cannot already have an ID")).body(null);
        }
        Servicepost result = servicepostRepository.save(servicepost);
        servicepostSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/serviceposts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /serviceposts : Updates an existing servicepost.
     *
     * @param servicepost the servicepost to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated servicepost,
     * or with status 400 (Bad Request) if the servicepost is not valid,
     * or with status 500 (Internal Server Error) if the servicepost couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/serviceposts")
    @Timed
    public ResponseEntity<Servicepost> updateServicepost(@RequestBody Servicepost servicepost) throws URISyntaxException {
        log.debug("REST request to update Servicepost : {}", servicepost);
        if (servicepost.getId() == null) {
            return createServicepost(servicepost);
        }
        Servicepost result = servicepostRepository.save(servicepost);
        servicepostSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, servicepost.getId().toString()))
            .body(result);
    }

    /**
     * GET  /serviceposts : get all the serviceposts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceposts in body
     */
    @GetMapping("/serviceposts")
    @Timed
    public ResponseEntity<List<Servicepost>> getAllServiceposts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Serviceposts");
        Page<Servicepost> page = servicepostRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/serviceposts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /serviceposts/:id : get the "id" servicepost.
     *
     * @param id the id of the servicepost to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the servicepost, or with status 404 (Not Found)
     */
    @GetMapping("/serviceposts/{id}")
    @Timed
    public ResponseEntity<Servicepost> getServicepost(@PathVariable Long id) {
        log.debug("REST request to get Servicepost : {}", id);
        Servicepost servicepost = servicepostRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(servicepost));
    }

    /**
     * DELETE  /serviceposts/:id : delete the "id" servicepost.
     *
     * @param id the id of the servicepost to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/serviceposts/{id}")
    @Timed
    public ResponseEntity<Void> deleteServicepost(@PathVariable Long id) {
        log.debug("REST request to delete Servicepost : {}", id);
        servicepostRepository.delete(id);
        servicepostSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/serviceposts?query=:query : search for the servicepost corresponding
     * to the query.
     *
     * @param query the query of the servicepost search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/serviceposts")
    @Timed
    public ResponseEntity<List<Servicepost>> searchServiceposts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Serviceposts for query {}", query);
        Page<Servicepost> page = servicepostSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/serviceposts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
