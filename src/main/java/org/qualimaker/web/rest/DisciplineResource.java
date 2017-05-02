package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.Discipline;

import org.qualimaker.repository.DisciplineRepository;
import org.qualimaker.repository.search.DisciplineSearchRepository;
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
 * REST controller for managing Discipline.
 */
@RestController
@RequestMapping("/api")
public class DisciplineResource {

    private final Logger log = LoggerFactory.getLogger(DisciplineResource.class);

    private static final String ENTITY_NAME = "discipline";
        
    private final DisciplineRepository disciplineRepository;

    private final DisciplineSearchRepository disciplineSearchRepository;

    public DisciplineResource(DisciplineRepository disciplineRepository, DisciplineSearchRepository disciplineSearchRepository) {
        this.disciplineRepository = disciplineRepository;
        this.disciplineSearchRepository = disciplineSearchRepository;
    }

    /**
     * POST  /disciplines : Create a new discipline.
     *
     * @param discipline the discipline to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discipline, or with status 400 (Bad Request) if the discipline has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/disciplines")
    @Timed
    public ResponseEntity<Discipline> createDiscipline(@RequestBody Discipline discipline) throws URISyntaxException {
        log.debug("REST request to save Discipline : {}", discipline);
        if (discipline.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new discipline cannot already have an ID")).body(null);
        }
        Discipline result = disciplineRepository.save(discipline);
        disciplineSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/disciplines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /disciplines : Updates an existing discipline.
     *
     * @param discipline the discipline to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discipline,
     * or with status 400 (Bad Request) if the discipline is not valid,
     * or with status 500 (Internal Server Error) if the discipline couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/disciplines")
    @Timed
    public ResponseEntity<Discipline> updateDiscipline(@RequestBody Discipline discipline) throws URISyntaxException {
        log.debug("REST request to update Discipline : {}", discipline);
        if (discipline.getId() == null) {
            return createDiscipline(discipline);
        }
        Discipline result = disciplineRepository.save(discipline);
        disciplineSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, discipline.getId().toString()))
            .body(result);
    }

    /**
     * GET  /disciplines : get all the disciplines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of disciplines in body
     */
    @GetMapping("/disciplines")
    @Timed
    public ResponseEntity<List<Discipline>> getAllDisciplines(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Disciplines");
        Page<Discipline> page = disciplineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/disciplines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /disciplines/:id : get the "id" discipline.
     *
     * @param id the id of the discipline to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discipline, or with status 404 (Not Found)
     */
    @GetMapping("/disciplines/{id}")
    @Timed
    public ResponseEntity<Discipline> getDiscipline(@PathVariable Long id) {
        log.debug("REST request to get Discipline : {}", id);
        Discipline discipline = disciplineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(discipline));
    }

    /**
     * DELETE  /disciplines/:id : delete the "id" discipline.
     *
     * @param id the id of the discipline to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/disciplines/{id}")
    @Timed
    public ResponseEntity<Void> deleteDiscipline(@PathVariable Long id) {
        log.debug("REST request to delete Discipline : {}", id);
        disciplineRepository.delete(id);
        disciplineSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/disciplines?query=:query : search for the discipline corresponding
     * to the query.
     *
     * @param query the query of the discipline search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/disciplines")
    @Timed
    public ResponseEntity<List<Discipline>> searchDisciplines(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Disciplines for query {}", query);
        Page<Discipline> page = disciplineSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/disciplines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
