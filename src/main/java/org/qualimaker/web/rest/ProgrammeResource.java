package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Programme;

import org.qualimaker.repository.ProgrammeRepository;
import org.qualimaker.repository.search.ProgrammeSearchRepository;
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
 * REST controller for managing Programme.
 */
@RestController
@RequestMapping("/api")
public class ProgrammeResource {

    private final Logger log = LoggerFactory.getLogger(ProgrammeResource.class);

    private static final String ENTITY_NAME = "programme";
        
    private final ProgrammeRepository programmeRepository;

    private final ProgrammeSearchRepository programmeSearchRepository;

    public ProgrammeResource(ProgrammeRepository programmeRepository, ProgrammeSearchRepository programmeSearchRepository) {
        this.programmeRepository = programmeRepository;
        this.programmeSearchRepository = programmeSearchRepository;
    }

    /**
     * POST  /programmes : Create a new programme.
     *
     * @param programme the programme to create
     * @return the ResponseEntity with status 201 (Created) and with body the new programme, or with status 400 (Bad Request) if the programme has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/programmes")
    @Timed
    public ResponseEntity<Programme> createProgramme(@RequestBody Programme programme) throws URISyntaxException {
        log.debug("REST request to save Programme : {}", programme);
        if (programme.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new programme cannot already have an ID")).body(null);
        }
        Programme result = programmeRepository.save(programme);
        programmeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/programmes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /programmes : Updates an existing programme.
     *
     * @param programme the programme to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated programme,
     * or with status 400 (Bad Request) if the programme is not valid,
     * or with status 500 (Internal Server Error) if the programme couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/programmes")
    @Timed
    public ResponseEntity<Programme> updateProgramme(@RequestBody Programme programme) throws URISyntaxException {
        log.debug("REST request to update Programme : {}", programme);
        if (programme.getId() == null) {
            return createProgramme(programme);
        }
        Programme result = programmeRepository.save(programme);
        programmeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programme.getId().toString()))
            .body(result);
    }

    /**
     * GET  /programmes : get all the programmes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of programmes in body
     */
    @GetMapping("/programmes")
    @Timed
    public List<Programme> getAllProgrammes() {
        log.debug("REST request to get all Programmes");
        List<Programme> programmes = programmeRepository.findAll();
        return programmes;
    }

    /**
     * GET  /programmes/:id : get the "id" programme.
     *
     * @param id the id of the programme to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the programme, or with status 404 (Not Found)
     */
    @GetMapping("/programmes/{id}")
    @Timed
    public ResponseEntity<Programme> getProgramme(@PathVariable Long id) {
        log.debug("REST request to get Programme : {}", id);
        Programme programme = programmeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programme));
    }

    /**
     * DELETE  /programmes/:id : delete the "id" programme.
     *
     * @param id the id of the programme to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/programmes/{id}")
    @Timed
    public ResponseEntity<Void> deleteProgramme(@PathVariable Long id) {
        log.debug("REST request to delete Programme : {}", id);
        programmeRepository.delete(id);
        programmeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/programmes?query=:query : search for the programme corresponding
     * to the query.
     *
     * @param query the query of the programme search 
     * @return the result of the search
     */
    @GetMapping("/_search/programmes")
    @Timed
    public List<Programme> searchProgrammes(@RequestParam String query) {
        log.debug("REST request to search Programmes for query {}", query);
        return StreamSupport
            .stream(programmeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
