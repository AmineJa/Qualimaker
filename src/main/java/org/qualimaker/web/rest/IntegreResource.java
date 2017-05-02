package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Integre;

import org.qualimaker.repository.IntegreRepository;
import org.qualimaker.repository.search.IntegreSearchRepository;
import org.qualimaker.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Integre.
 */
@RestController
@RequestMapping("/api")
public class IntegreResource {

    private final Logger log = LoggerFactory.getLogger(IntegreResource.class);

    private static final String ENTITY_NAME = "integre";
        
    private final IntegreRepository integreRepository;

    private final IntegreSearchRepository integreSearchRepository;

    public IntegreResource(IntegreRepository integreRepository, IntegreSearchRepository integreSearchRepository) {
        this.integreRepository = integreRepository;
        this.integreSearchRepository = integreSearchRepository;
    }

    /**
     * POST  /integres : Create a new integre.
     *
     * @param integre the integre to create
     * @return the ResponseEntity with status 201 (Created) and with body the new integre, or with status 400 (Bad Request) if the integre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/integres")
    @Timed
    public ResponseEntity<Integre> createIntegre(@Valid @RequestBody Integre integre) throws URISyntaxException {
        log.debug("REST request to save Integre : {}", integre);
        if (integre.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new integre cannot already have an ID")).body(null);
        }
        Integre result = integreRepository.save(integre);
        integreSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/integres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /integres : Updates an existing integre.
     *
     * @param integre the integre to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated integre,
     * or with status 400 (Bad Request) if the integre is not valid,
     * or with status 500 (Internal Server Error) if the integre couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/integres")
    @Timed
    public ResponseEntity<Integre> updateIntegre(@Valid @RequestBody Integre integre) throws URISyntaxException {
        log.debug("REST request to update Integre : {}", integre);
        if (integre.getId() == null) {
            return createIntegre(integre);
        }
        Integre result = integreRepository.save(integre);
        integreSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, integre.getId().toString()))
            .body(result);
    }

    /**
     * GET  /integres : get all the integres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of integres in body
     */
    @GetMapping("/integres")
    @Timed
    public List<Integre> getAllIntegres() {
        log.debug("REST request to get all Integres");
        List<Integre> integres = integreRepository.findAll();
        return integres;
    }

    /**
     * GET  /integres/:id : get the "id" integre.
     *
     * @param id the id of the integre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the integre, or with status 404 (Not Found)
     */
    @GetMapping("/integres/{id}")
    @Timed
    public ResponseEntity<Integre> getIntegre(@PathVariable Long id) {
        log.debug("REST request to get Integre : {}", id);
        Integre integre = integreRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(integre));
    }

    /**
     * DELETE  /integres/:id : delete the "id" integre.
     *
     * @param id the id of the integre to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/integres/{id}")
    @Timed
    public ResponseEntity<Void> deleteIntegre(@PathVariable Long id) {
        log.debug("REST request to delete Integre : {}", id);
        integreRepository.delete(id);
        integreSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/integres?query=:query : search for the integre corresponding
     * to the query.
     *
     * @param query the query of the integre search 
     * @return the result of the search
     */
    @GetMapping("/_search/integres")
    @Timed
    public List<Integre> searchIntegres(@RequestParam String query) {
        log.debug("REST request to search Integres for query {}", query);
        return StreamSupport
            .stream(integreSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
