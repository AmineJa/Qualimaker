package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.EtatDemande;
import org.qualimaker.repository.EtatDemandeRepository;
import org.qualimaker.repository.search.EtatDemandeSearchRepository;
import org.qualimaker.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EtatDemandeResource REST controller.
 *
 * @see EtatDemandeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class EtatDemandeResourceIntTest {

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    @Autowired
    private EtatDemandeRepository etatDemandeRepository;

    @Autowired
    private EtatDemandeSearchRepository etatDemandeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEtatDemandeMockMvc;

    private EtatDemande etatDemande;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EtatDemandeResource etatDemandeResource = new EtatDemandeResource(etatDemandeRepository, etatDemandeSearchRepository);
        this.restEtatDemandeMockMvc = MockMvcBuilders.standaloneSetup(etatDemandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EtatDemande createEntity(EntityManager em) {
        EtatDemande etatDemande = new EtatDemande()
            .etat(DEFAULT_ETAT);
        return etatDemande;
    }

    @Before
    public void initTest() {
        etatDemandeSearchRepository.deleteAll();
        etatDemande = createEntity(em);
    }

    @Test
    @Transactional
    public void createEtatDemande() throws Exception {
        int databaseSizeBeforeCreate = etatDemandeRepository.findAll().size();

        // Create the EtatDemande
        restEtatDemandeMockMvc.perform(post("/api/etat-demandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etatDemande)))
            .andExpect(status().isCreated());

        // Validate the EtatDemande in the database
        List<EtatDemande> etatDemandeList = etatDemandeRepository.findAll();
        assertThat(etatDemandeList).hasSize(databaseSizeBeforeCreate + 1);
        EtatDemande testEtatDemande = etatDemandeList.get(etatDemandeList.size() - 1);
        assertThat(testEtatDemande.getEtat()).isEqualTo(DEFAULT_ETAT);

        // Validate the EtatDemande in Elasticsearch
        EtatDemande etatDemandeEs = etatDemandeSearchRepository.findOne(testEtatDemande.getId());
        assertThat(etatDemandeEs).isEqualToComparingFieldByField(testEtatDemande);
    }

    @Test
    @Transactional
    public void createEtatDemandeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = etatDemandeRepository.findAll().size();

        // Create the EtatDemande with an existing ID
        etatDemande.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtatDemandeMockMvc.perform(post("/api/etat-demandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etatDemande)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EtatDemande> etatDemandeList = etatDemandeRepository.findAll();
        assertThat(etatDemandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEtatDemandes() throws Exception {
        // Initialize the database
        etatDemandeRepository.saveAndFlush(etatDemande);

        // Get all the etatDemandeList
        restEtatDemandeMockMvc.perform(get("/api/etat-demandes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etatDemande.getId().intValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    public void getEtatDemande() throws Exception {
        // Initialize the database
        etatDemandeRepository.saveAndFlush(etatDemande);

        // Get the etatDemande
        restEtatDemandeMockMvc.perform(get("/api/etat-demandes/{id}", etatDemande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(etatDemande.getId().intValue()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEtatDemande() throws Exception {
        // Get the etatDemande
        restEtatDemandeMockMvc.perform(get("/api/etat-demandes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtatDemande() throws Exception {
        // Initialize the database
        etatDemandeRepository.saveAndFlush(etatDemande);
        etatDemandeSearchRepository.save(etatDemande);
        int databaseSizeBeforeUpdate = etatDemandeRepository.findAll().size();

        // Update the etatDemande
        EtatDemande updatedEtatDemande = etatDemandeRepository.findOne(etatDemande.getId());
        updatedEtatDemande
            .etat(UPDATED_ETAT);

        restEtatDemandeMockMvc.perform(put("/api/etat-demandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEtatDemande)))
            .andExpect(status().isOk());

        // Validate the EtatDemande in the database
        List<EtatDemande> etatDemandeList = etatDemandeRepository.findAll();
        assertThat(etatDemandeList).hasSize(databaseSizeBeforeUpdate);
        EtatDemande testEtatDemande = etatDemandeList.get(etatDemandeList.size() - 1);
        assertThat(testEtatDemande.getEtat()).isEqualTo(UPDATED_ETAT);

        // Validate the EtatDemande in Elasticsearch
        EtatDemande etatDemandeEs = etatDemandeSearchRepository.findOne(testEtatDemande.getId());
        assertThat(etatDemandeEs).isEqualToComparingFieldByField(testEtatDemande);
    }

    @Test
    @Transactional
    public void updateNonExistingEtatDemande() throws Exception {
        int databaseSizeBeforeUpdate = etatDemandeRepository.findAll().size();

        // Create the EtatDemande

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEtatDemandeMockMvc.perform(put("/api/etat-demandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etatDemande)))
            .andExpect(status().isCreated());

        // Validate the EtatDemande in the database
        List<EtatDemande> etatDemandeList = etatDemandeRepository.findAll();
        assertThat(etatDemandeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEtatDemande() throws Exception {
        // Initialize the database
        etatDemandeRepository.saveAndFlush(etatDemande);
        etatDemandeSearchRepository.save(etatDemande);
        int databaseSizeBeforeDelete = etatDemandeRepository.findAll().size();

        // Get the etatDemande
        restEtatDemandeMockMvc.perform(delete("/api/etat-demandes/{id}", etatDemande.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean etatDemandeExistsInEs = etatDemandeSearchRepository.exists(etatDemande.getId());
        assertThat(etatDemandeExistsInEs).isFalse();

        // Validate the database is empty
        List<EtatDemande> etatDemandeList = etatDemandeRepository.findAll();
        assertThat(etatDemandeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEtatDemande() throws Exception {
        // Initialize the database
        etatDemandeRepository.saveAndFlush(etatDemande);
        etatDemandeSearchRepository.save(etatDemande);

        // Search the etatDemande
        restEtatDemandeMockMvc.perform(get("/api/_search/etat-demandes?query=id:" + etatDemande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etatDemande.getId().intValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EtatDemande.class);
    }
}
