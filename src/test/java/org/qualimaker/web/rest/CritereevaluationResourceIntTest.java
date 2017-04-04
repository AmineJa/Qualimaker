package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Critereevaluation;
import org.qualimaker.repository.CritereevaluationRepository;
import org.qualimaker.repository.search.CritereevaluationSearchRepository;
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
 * Test class for the CritereevaluationResource REST controller.
 *
 * @see CritereevaluationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class CritereevaluationResourceIntTest {

    private static final String DEFAULT_CRITERE = "AAAAAAAAAA";
    private static final String UPDATED_CRITERE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORIE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIE = "BBBBBBBBBB";

    @Autowired
    private CritereevaluationRepository critereevaluationRepository;

    @Autowired
    private CritereevaluationSearchRepository critereevaluationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCritereevaluationMockMvc;

    private Critereevaluation critereevaluation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CritereevaluationResource critereevaluationResource = new CritereevaluationResource(critereevaluationRepository, critereevaluationSearchRepository);
        this.restCritereevaluationMockMvc = MockMvcBuilders.standaloneSetup(critereevaluationResource)
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
    public static Critereevaluation createEntity(EntityManager em) {
        Critereevaluation critereevaluation = new Critereevaluation()
            .critere(DEFAULT_CRITERE)
            .categorie(DEFAULT_CATEGORIE);
        return critereevaluation;
    }

    @Before
    public void initTest() {
        critereevaluationSearchRepository.deleteAll();
        critereevaluation = createEntity(em);
    }

    @Test
    @Transactional
    public void createCritereevaluation() throws Exception {
        int databaseSizeBeforeCreate = critereevaluationRepository.findAll().size();

        // Create the Critereevaluation
        restCritereevaluationMockMvc.perform(post("/api/critereevaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(critereevaluation)))
            .andExpect(status().isCreated());

        // Validate the Critereevaluation in the database
        List<Critereevaluation> critereevaluationList = critereevaluationRepository.findAll();
        assertThat(critereevaluationList).hasSize(databaseSizeBeforeCreate + 1);
        Critereevaluation testCritereevaluation = critereevaluationList.get(critereevaluationList.size() - 1);
        assertThat(testCritereevaluation.getCritere()).isEqualTo(DEFAULT_CRITERE);
        assertThat(testCritereevaluation.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);

        // Validate the Critereevaluation in Elasticsearch
        Critereevaluation critereevaluationEs = critereevaluationSearchRepository.findOne(testCritereevaluation.getId());
        assertThat(critereevaluationEs).isEqualToComparingFieldByField(testCritereevaluation);
    }

    @Test
    @Transactional
    public void createCritereevaluationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = critereevaluationRepository.findAll().size();

        // Create the Critereevaluation with an existing ID
        critereevaluation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCritereevaluationMockMvc.perform(post("/api/critereevaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(critereevaluation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Critereevaluation> critereevaluationList = critereevaluationRepository.findAll();
        assertThat(critereevaluationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCritereevaluations() throws Exception {
        // Initialize the database
        critereevaluationRepository.saveAndFlush(critereevaluation);

        // Get all the critereevaluationList
        restCritereevaluationMockMvc.perform(get("/api/critereevaluations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(critereevaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].critere").value(hasItem(DEFAULT_CRITERE.toString())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())));
    }

    @Test
    @Transactional
    public void getCritereevaluation() throws Exception {
        // Initialize the database
        critereevaluationRepository.saveAndFlush(critereevaluation);

        // Get the critereevaluation
        restCritereevaluationMockMvc.perform(get("/api/critereevaluations/{id}", critereevaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(critereevaluation.getId().intValue()))
            .andExpect(jsonPath("$.critere").value(DEFAULT_CRITERE.toString()))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCritereevaluation() throws Exception {
        // Get the critereevaluation
        restCritereevaluationMockMvc.perform(get("/api/critereevaluations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCritereevaluation() throws Exception {
        // Initialize the database
        critereevaluationRepository.saveAndFlush(critereevaluation);
        critereevaluationSearchRepository.save(critereevaluation);
        int databaseSizeBeforeUpdate = critereevaluationRepository.findAll().size();

        // Update the critereevaluation
        Critereevaluation updatedCritereevaluation = critereevaluationRepository.findOne(critereevaluation.getId());
        updatedCritereevaluation
            .critere(UPDATED_CRITERE)
            .categorie(UPDATED_CATEGORIE);

        restCritereevaluationMockMvc.perform(put("/api/critereevaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCritereevaluation)))
            .andExpect(status().isOk());

        // Validate the Critereevaluation in the database
        List<Critereevaluation> critereevaluationList = critereevaluationRepository.findAll();
        assertThat(critereevaluationList).hasSize(databaseSizeBeforeUpdate);
        Critereevaluation testCritereevaluation = critereevaluationList.get(critereevaluationList.size() - 1);
        assertThat(testCritereevaluation.getCritere()).isEqualTo(UPDATED_CRITERE);
        assertThat(testCritereevaluation.getCategorie()).isEqualTo(UPDATED_CATEGORIE);

        // Validate the Critereevaluation in Elasticsearch
        Critereevaluation critereevaluationEs = critereevaluationSearchRepository.findOne(testCritereevaluation.getId());
        assertThat(critereevaluationEs).isEqualToComparingFieldByField(testCritereevaluation);
    }

    @Test
    @Transactional
    public void updateNonExistingCritereevaluation() throws Exception {
        int databaseSizeBeforeUpdate = critereevaluationRepository.findAll().size();

        // Create the Critereevaluation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCritereevaluationMockMvc.perform(put("/api/critereevaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(critereevaluation)))
            .andExpect(status().isCreated());

        // Validate the Critereevaluation in the database
        List<Critereevaluation> critereevaluationList = critereevaluationRepository.findAll();
        assertThat(critereevaluationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCritereevaluation() throws Exception {
        // Initialize the database
        critereevaluationRepository.saveAndFlush(critereevaluation);
        critereevaluationSearchRepository.save(critereevaluation);
        int databaseSizeBeforeDelete = critereevaluationRepository.findAll().size();

        // Get the critereevaluation
        restCritereevaluationMockMvc.perform(delete("/api/critereevaluations/{id}", critereevaluation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean critereevaluationExistsInEs = critereevaluationSearchRepository.exists(critereevaluation.getId());
        assertThat(critereevaluationExistsInEs).isFalse();

        // Validate the database is empty
        List<Critereevaluation> critereevaluationList = critereevaluationRepository.findAll();
        assertThat(critereevaluationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCritereevaluation() throws Exception {
        // Initialize the database
        critereevaluationRepository.saveAndFlush(critereevaluation);
        critereevaluationSearchRepository.save(critereevaluation);

        // Search the critereevaluation
        restCritereevaluationMockMvc.perform(get("/api/_search/critereevaluations?query=id:" + critereevaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(critereevaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].critere").value(hasItem(DEFAULT_CRITERE.toString())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Critereevaluation.class);
    }
}
