package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Etat;
import org.qualimaker.repository.EtatRepository;
import org.qualimaker.repository.search.EtatSearchRepository;
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
 * Test class for the EtatResource REST controller.
 *
 * @see EtatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class EtatResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private EtatRepository etatRepository;

    @Autowired
    private EtatSearchRepository etatSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEtatMockMvc;

    private Etat etat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EtatResource etatResource = new EtatResource(etatRepository, etatSearchRepository);
        this.restEtatMockMvc = MockMvcBuilders.standaloneSetup(etatResource)
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
    public static Etat createEntity(EntityManager em) {
        Etat etat = new Etat()
            .libelle(DEFAULT_LIBELLE);
        return etat;
    }

    @Before
    public void initTest() {
        etatSearchRepository.deleteAll();
        etat = createEntity(em);
    }

    @Test
    @Transactional
    public void createEtat() throws Exception {
        int databaseSizeBeforeCreate = etatRepository.findAll().size();

        // Create the Etat
        restEtatMockMvc.perform(post("/api/etats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etat)))
            .andExpect(status().isCreated());

        // Validate the Etat in the database
        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeCreate + 1);
        Etat testEtat = etatList.get(etatList.size() - 1);
        assertThat(testEtat.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the Etat in Elasticsearch
        Etat etatEs = etatSearchRepository.findOne(testEtat.getId());
        assertThat(etatEs).isEqualToComparingFieldByField(testEtat);
    }

    @Test
    @Transactional
    public void createEtatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = etatRepository.findAll().size();

        // Create the Etat with an existing ID
        etat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtatMockMvc.perform(post("/api/etats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etat)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEtats() throws Exception {
        // Initialize the database
        etatRepository.saveAndFlush(etat);

        // Get all the etatList
        restEtatMockMvc.perform(get("/api/etats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etat.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getEtat() throws Exception {
        // Initialize the database
        etatRepository.saveAndFlush(etat);

        // Get the etat
        restEtatMockMvc.perform(get("/api/etats/{id}", etat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(etat.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEtat() throws Exception {
        // Get the etat
        restEtatMockMvc.perform(get("/api/etats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtat() throws Exception {
        // Initialize the database
        etatRepository.saveAndFlush(etat);
        etatSearchRepository.save(etat);
        int databaseSizeBeforeUpdate = etatRepository.findAll().size();

        // Update the etat
        Etat updatedEtat = etatRepository.findOne(etat.getId());
        updatedEtat
            .libelle(UPDATED_LIBELLE);

        restEtatMockMvc.perform(put("/api/etats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEtat)))
            .andExpect(status().isOk());

        // Validate the Etat in the database
        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeUpdate);
        Etat testEtat = etatList.get(etatList.size() - 1);
        assertThat(testEtat.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the Etat in Elasticsearch
        Etat etatEs = etatSearchRepository.findOne(testEtat.getId());
        assertThat(etatEs).isEqualToComparingFieldByField(testEtat);
    }

    @Test
    @Transactional
    public void updateNonExistingEtat() throws Exception {
        int databaseSizeBeforeUpdate = etatRepository.findAll().size();

        // Create the Etat

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEtatMockMvc.perform(put("/api/etats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etat)))
            .andExpect(status().isCreated());

        // Validate the Etat in the database
        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEtat() throws Exception {
        // Initialize the database
        etatRepository.saveAndFlush(etat);
        etatSearchRepository.save(etat);
        int databaseSizeBeforeDelete = etatRepository.findAll().size();

        // Get the etat
        restEtatMockMvc.perform(delete("/api/etats/{id}", etat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean etatExistsInEs = etatSearchRepository.exists(etat.getId());
        assertThat(etatExistsInEs).isFalse();

        // Validate the database is empty
        List<Etat> etatList = etatRepository.findAll();
        assertThat(etatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEtat() throws Exception {
        // Initialize the database
        etatRepository.saveAndFlush(etat);
        etatSearchRepository.save(etat);

        // Search the etat
        restEtatMockMvc.perform(get("/api/_search/etats?query=id:" + etat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etat.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etat.class);
    }
}
