package org.qualimaker.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.qualimaker.domain.EtatDemande;

import org.qualimaker.repository.EtatDemandeRepository;
import org.qualimaker.repository.search.EtatDemandeSearchRepository;
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
 * REST controller for managing EtatDemande.
 */
@RestController
@RequestMapping("/api")
public class EtatDemandeResource {

    private final Logger log = LoggerFactory.getLogger(EtatDemandeResource.class);

    private static final String ENTITY_NAME = "etatDemande";
        
    private final EtatDemandeRepository etatDemandeRepository;

    private final EtatDemandeSearchRepository etatDemandeSearchRepository;

    public EtatDemandeResource(EtatDemandeRepository etatDemandeRepository, EtatDemandeSearchRepository etatDemandeSearchRepository) {
        this.etatDemandeRepository = etatDemandeRepository;
        this.etatDemandeSearchRepository = etatDemandeSearchRepository;
    }

    /**
     * POST  /etat-demandes : Create a new etatDemande.
     *
     * @param etatDemande the etatDemande to create
     * @return the ResponseEntity with status 201 (Created) and with body the new etatDemande, or with status 400 (Bad Request) if the etatDemande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/etat-demandes")
    @Timed
    public ResponseEntity<EtatDemande> createEtatDemande(@RequestBody EtatDemande etatDemande) throws URISyntaxException {
        log.debug("REST request to save EtatDemande : {}", etatDemande);
        if (etatDemande.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new etatDemande cannot already have an ID")).body(null);
        }
        EtatDemande result = etatDemandeRepository.save(etatDemande);
        etatDemandeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/etat-demandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /etat-demandes : Updates an existing etatDemande.
     *
     * @param etatDemande the etatDemande to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated etatDemande,
     * or with status 400 (Bad Request) if the etatDemande is not valid,
     * or with status 500 (Internal Server Error) if the etatDemande couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/etat-demandes")
    @Timed
    public ResponseEntity<EtatDemande> updateEtatDemande(@RequestBody EtatDemande etatDemande) throws URISyntaxException {
        log.debug("REST request to update EtatDemande : {}", etatDemande);
        if (etatDemande.getId() == null) {
            return createEtatDemande(etatDemande);
        }
        EtatDemande result = etatDemandeRepository.save(etatDemande);
        etatDemandeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, etatDemande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /etat-demandes : get all the etatDemandes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of etatDemandes in body
     */
    @GetMapping("/etat-demandes")
    @Timed
    public ResponseEntity<List<EtatDemande>> getAllEtatDemandes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of EtatDemandes");
        Page<EtatDemande> page = etatDemandeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/etat-demandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /etat-demandes/:id : get the "id" etatDemande.
     *
     * @param id the id of the etatDemande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the etatDemande, or with status 404 (Not Found)
     */
    @GetMapping("/etat-demandes/{id}")
    @Timed
    public ResponseEntity<EtatDemande> getEtatDemande(@PathVariable Long id) {
        log.debug("REST request to get EtatDemande : {}", id);
        EtatDemande etatDemande = etatDemandeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(etatDemande));
    }

    /**
     * DELETE  /etat-demandes/:id : delete the "id" etatDemande.
     *
     * @param id the id of the etatDemande to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/etat-demandes/{id}")
    @Timed
    public ResponseEntity<Void> deleteEtatDemande(@PathVariable Long id) {
        log.debug("REST request to delete EtatDemande : {}", id);
        etatDemandeRepository.delete(id);
        etatDemandeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/etat-demandes?query=:query : search for the etatDemande corresponding
     * to the query.
     *
     * @param query the query of the etatDemande search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/etat-demandes")
    @Timed
    public ResponseEntity<List<EtatDemande>> searchEtatDemandes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of EtatDemandes for query {}", query);
        Page<EtatDemande> page = etatDemandeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/etat-demandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
