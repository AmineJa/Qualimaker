package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.NatureAbs;

import org.qualimaker.repository.NatureAbsRepository;
import org.qualimaker.repository.search.NatureAbsSearchRepository;
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
 * REST controller for managing NatureAbs.
 */
@RestController
@RequestMapping("/api")
public class NatureAbsResource {

    private final Logger log = LoggerFactory.getLogger(NatureAbsResource.class);

    private static final String ENTITY_NAME = "natureAbs";
        
    private final NatureAbsRepository natureAbsRepository;

    private final NatureAbsSearchRepository natureAbsSearchRepository;

    public NatureAbsResource(NatureAbsRepository natureAbsRepository, NatureAbsSearchRepository natureAbsSearchRepository) {
        this.natureAbsRepository = natureAbsRepository;
        this.natureAbsSearchRepository = natureAbsSearchRepository;
    }

    /**
     * POST  /nature-abs : Create a new natureAbs.
     *
     * @param natureAbs the natureAbs to create
     * @return the ResponseEntity with status 201 (Created) and with body the new natureAbs, or with status 400 (Bad Request) if the natureAbs has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/nature-abs")
    @Timed
    public ResponseEntity<NatureAbs> createNatureAbs(@RequestBody NatureAbs natureAbs) throws URISyntaxException {
        log.debug("REST request to save NatureAbs : {}", natureAbs);
        if (natureAbs.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new natureAbs cannot already have an ID")).body(null);
        }
        NatureAbs result = natureAbsRepository.save(natureAbs);
        natureAbsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/nature-abs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /nature-abs : Updates an existing natureAbs.
     *
     * @param natureAbs the natureAbs to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated natureAbs,
     * or with status 400 (Bad Request) if the natureAbs is not valid,
     * or with status 500 (Internal Server Error) if the natureAbs couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/nature-abs")
    @Timed
    public ResponseEntity<NatureAbs> updateNatureAbs(@RequestBody NatureAbs natureAbs) throws URISyntaxException {
        log.debug("REST request to update NatureAbs : {}", natureAbs);
        if (natureAbs.getId() == null) {
            return createNatureAbs(natureAbs);
        }
        NatureAbs result = natureAbsRepository.save(natureAbs);
        natureAbsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, natureAbs.getId().toString()))
            .body(result);
    }

    /**
     * GET  /nature-abs : get all the natureAbs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of natureAbs in body
     */
    @GetMapping("/nature-abs")
    @Timed
    public List<NatureAbs> getAllNatureAbs() {
        log.debug("REST request to get all NatureAbs");
        List<NatureAbs> natureAbs = natureAbsRepository.findAll();
        return natureAbs;
    }

    /**
     * GET  /nature-abs/:id : get the "id" natureAbs.
     *
     * @param id the id of the natureAbs to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the natureAbs, or with status 404 (Not Found)
     */
    @GetMapping("/nature-abs/{id}")
    @Timed
    public ResponseEntity<NatureAbs> getNatureAbs(@PathVariable Long id) {
        log.debug("REST request to get NatureAbs : {}", id);
        NatureAbs natureAbs = natureAbsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(natureAbs));
    }

    /**
     * DELETE  /nature-abs/:id : delete the "id" natureAbs.
     *
     * @param id the id of the natureAbs to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/nature-abs/{id}")
    @Timed
    public ResponseEntity<Void> deleteNatureAbs(@PathVariable Long id) {
        log.debug("REST request to delete NatureAbs : {}", id);
        natureAbsRepository.delete(id);
        natureAbsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/nature-abs?query=:query : search for the natureAbs corresponding
     * to the query.
     *
     * @param query the query of the natureAbs search 
     * @return the result of the search
     */
    @GetMapping("/_search/nature-abs")
    @Timed
    public List<NatureAbs> searchNatureAbs(@RequestParam String query) {
        log.debug("REST request to search NatureAbs for query {}", query);
        return StreamSupport
            .stream(natureAbsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
