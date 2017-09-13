package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Competences;
import org.qualimaker.repository.CompetencesRepository;
import org.qualimaker.repository.search.CompetencesSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CompetencesResource REST controller.
 *
 * @see CompetencesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class CompetencesResourceIntTest {

    private static final String DEFAULT_FILE = "AAAAAAAAAA";
    private static final String UPDATED_FILE = "BBBBBBBBBB";

    @Autowired
    private CompetencesRepository competencesRepository;

    @Autowired
    private CompetencesSearchRepository competencesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompetencesMockMvc;

    private Competences competences;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompetencesResource competencesResource = new CompetencesResource(competencesRepository, competencesSearchRepository);
        this.restCompetencesMockMvc = MockMvcBuilders.standaloneSetup(competencesResource)
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
    public static Competences createEntity(EntityManager em) {
        Competences competences = new Competences()
            .file(DEFAULT_FILE);
        return competences;
    }

    @Before
    public void initTest() {
        competencesSearchRepository.deleteAll();
        competences = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompetences() throws Exception {
        int databaseSizeBeforeCreate = competencesRepository.findAll().size();

        // Create the Competences
        restCompetencesMockMvc.perform(post("/api/competences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competences)))
            .andExpect(status().isCreated());

        // Validate the Competences in the database
        List<Competences> competencesList = competencesRepository.findAll();
        assertThat(competencesList).hasSize(databaseSizeBeforeCreate + 1);
        Competences testCompetences = competencesList.get(competencesList.size() - 1);
        assertThat(testCompetences.getFile()).isEqualTo(DEFAULT_FILE);

        // Validate the Competences in Elasticsearch
        Competences competencesEs = competencesSearchRepository.findOne(testCompetences.getId());
        assertThat(competencesEs).isEqualToComparingFieldByField(testCompetences);
    }

    @Test
    @Transactional
    public void createCompetencesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = competencesRepository.findAll().size();

        // Create the Competences with an existing ID
        competences.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetencesMockMvc.perform(post("/api/competences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competences)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Competences> competencesList = competencesRepository.findAll();
        assertThat(competencesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCompetences() throws Exception {
        // Initialize the database
        competencesRepository.saveAndFlush(competences);

        // Get all the competencesList
        restCompetencesMockMvc.perform(get("/api/competences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competences.getId().intValue())))
            .andExpect(jsonPath("$.[*].file").value(hasItem(DEFAULT_FILE.toString())));
    }

    @Test
    @Transactional
    public void getCompetences() throws Exception {
        // Initialize the database
        competencesRepository.saveAndFlush(competences);

        // Get the competences
        restCompetencesMockMvc.perform(get("/api/competences/{id}", competences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(competences.getId().intValue()))
            .andExpect(jsonPath("$.file").value(DEFAULT_FILE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompetences() throws Exception {
        // Get the competences
        restCompetencesMockMvc.perform(get("/api/competences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetences() throws Exception {
        // Initialize the database
        competencesRepository.saveAndFlush(competences);
        competencesSearchRepository.save(competences);
        int databaseSizeBeforeUpdate = competencesRepository.findAll().size();

        // Update the competences
        Competences updatedCompetences = competencesRepository.findOne(competences.getId());
        updatedCompetences
            .file(UPDATED_FILE);

        restCompetencesMockMvc.perform(put("/api/competences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompetences)))
            .andExpect(status().isOk());

        // Validate the Competences in the database
        List<Competences> competencesList = competencesRepository.findAll();
        assertThat(competencesList).hasSize(databaseSizeBeforeUpdate);
        Competences testCompetences = competencesList.get(competencesList.size() - 1);
        assertThat(testCompetences.getFile()).isEqualTo(UPDATED_FILE);

        // Validate the Competences in Elasticsearch
        Competences competencesEs = competencesSearchRepository.findOne(testCompetences.getId());
        assertThat(competencesEs).isEqualToComparingFieldByField(testCompetences);
    }

    @Test
    @Transactional
    public void updateNonExistingCompetences() throws Exception {
        int databaseSizeBeforeUpdate = competencesRepository.findAll().size();

        // Create the Competences

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompetencesMockMvc.perform(put("/api/competences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competences)))
            .andExpect(status().isCreated());

        // Validate the Competences in the database
        List<Competences> competencesList = competencesRepository.findAll();
        assertThat(competencesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompetences() throws Exception {
        // Initialize the database
        competencesRepository.saveAndFlush(competences);
        competencesSearchRepository.save(competences);
        int databaseSizeBeforeDelete = competencesRepository.findAll().size();

        // Get the competences
        restCompetencesMockMvc.perform(delete("/api/competences/{id}", competences.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean competencesExistsInEs = competencesSearchRepository.exists(competences.getId());
        assertThat(competencesExistsInEs).isFalse();

        // Validate the database is empty
        List<Competences> competencesList = competencesRepository.findAll();
        assertThat(competencesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompetences() throws Exception {
        // Initialize the database
        competencesRepository.saveAndFlush(competences);
        competencesSearchRepository.save(competences);

        // Search the competences
        restCompetencesMockMvc.perform(get("/api/_search/competences?query=id:" + competences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competences.getId().intValue())))
            .andExpect(jsonPath("$.[*].file").value(hasItem(DEFAULT_FILE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Competences.class);
    }
}
