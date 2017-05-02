package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.repository.DomaineCompetenceRepository;
import org.qualimaker.repository.search.DomaineCompetenceSearchRepository;
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
 * Test class for the DomaineCompetenceResource REST controller.
 *
 * @see DomaineCompetenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class DomaineCompetenceResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private DomaineCompetenceRepository domaineCompetenceRepository;

    @Autowired
    private DomaineCompetenceSearchRepository domaineCompetenceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDomaineCompetenceMockMvc;

    private DomaineCompetence domaineCompetence;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DomaineCompetenceResource domaineCompetenceResource = new DomaineCompetenceResource(domaineCompetenceRepository, domaineCompetenceSearchRepository);
        this.restDomaineCompetenceMockMvc = MockMvcBuilders.standaloneSetup(domaineCompetenceResource)
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
    public static DomaineCompetence createEntity(EntityManager em) {
        DomaineCompetence domaineCompetence = new DomaineCompetence()
            .libelle(DEFAULT_LIBELLE);
        return domaineCompetence;
    }

    @Before
    public void initTest() {
        domaineCompetenceSearchRepository.deleteAll();
        domaineCompetence = createEntity(em);
    }

    @Test
    @Transactional
    public void createDomaineCompetence() throws Exception {
        int databaseSizeBeforeCreate = domaineCompetenceRepository.findAll().size();

        // Create the DomaineCompetence
        restDomaineCompetenceMockMvc.perform(post("/api/domaine-competences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domaineCompetence)))
            .andExpect(status().isCreated());

        // Validate the DomaineCompetence in the database
        List<DomaineCompetence> domaineCompetenceList = domaineCompetenceRepository.findAll();
        assertThat(domaineCompetenceList).hasSize(databaseSizeBeforeCreate + 1);
        DomaineCompetence testDomaineCompetence = domaineCompetenceList.get(domaineCompetenceList.size() - 1);
        assertThat(testDomaineCompetence.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the DomaineCompetence in Elasticsearch
        DomaineCompetence domaineCompetenceEs = domaineCompetenceSearchRepository.findOne(testDomaineCompetence.getId());
        assertThat(domaineCompetenceEs).isEqualToComparingFieldByField(testDomaineCompetence);
    }

    @Test
    @Transactional
    public void createDomaineCompetenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = domaineCompetenceRepository.findAll().size();

        // Create the DomaineCompetence with an existing ID
        domaineCompetence.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomaineCompetenceMockMvc.perform(post("/api/domaine-competences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domaineCompetence)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DomaineCompetence> domaineCompetenceList = domaineCompetenceRepository.findAll();
        assertThat(domaineCompetenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDomaineCompetences() throws Exception {
        // Initialize the database
        domaineCompetenceRepository.saveAndFlush(domaineCompetence);

        // Get all the domaineCompetenceList
        restDomaineCompetenceMockMvc.perform(get("/api/domaine-competences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domaineCompetence.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getDomaineCompetence() throws Exception {
        // Initialize the database
        domaineCompetenceRepository.saveAndFlush(domaineCompetence);

        // Get the domaineCompetence
        restDomaineCompetenceMockMvc.perform(get("/api/domaine-competences/{id}", domaineCompetence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(domaineCompetence.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDomaineCompetence() throws Exception {
        // Get the domaineCompetence
        restDomaineCompetenceMockMvc.perform(get("/api/domaine-competences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDomaineCompetence() throws Exception {
        // Initialize the database
        domaineCompetenceRepository.saveAndFlush(domaineCompetence);
        domaineCompetenceSearchRepository.save(domaineCompetence);
        int databaseSizeBeforeUpdate = domaineCompetenceRepository.findAll().size();

        // Update the domaineCompetence
        DomaineCompetence updatedDomaineCompetence = domaineCompetenceRepository.findOne(domaineCompetence.getId());
        updatedDomaineCompetence
            .libelle(UPDATED_LIBELLE);

        restDomaineCompetenceMockMvc.perform(put("/api/domaine-competences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDomaineCompetence)))
            .andExpect(status().isOk());

        // Validate the DomaineCompetence in the database
        List<DomaineCompetence> domaineCompetenceList = domaineCompetenceRepository.findAll();
        assertThat(domaineCompetenceList).hasSize(databaseSizeBeforeUpdate);
        DomaineCompetence testDomaineCompetence = domaineCompetenceList.get(domaineCompetenceList.size() - 1);
        assertThat(testDomaineCompetence.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the DomaineCompetence in Elasticsearch
        DomaineCompetence domaineCompetenceEs = domaineCompetenceSearchRepository.findOne(testDomaineCompetence.getId());
        assertThat(domaineCompetenceEs).isEqualToComparingFieldByField(testDomaineCompetence);
    }

    @Test
    @Transactional
    public void updateNonExistingDomaineCompetence() throws Exception {
        int databaseSizeBeforeUpdate = domaineCompetenceRepository.findAll().size();

        // Create the DomaineCompetence

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDomaineCompetenceMockMvc.perform(put("/api/domaine-competences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domaineCompetence)))
            .andExpect(status().isCreated());

        // Validate the DomaineCompetence in the database
        List<DomaineCompetence> domaineCompetenceList = domaineCompetenceRepository.findAll();
        assertThat(domaineCompetenceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDomaineCompetence() throws Exception {
        // Initialize the database
        domaineCompetenceRepository.saveAndFlush(domaineCompetence);
        domaineCompetenceSearchRepository.save(domaineCompetence);
        int databaseSizeBeforeDelete = domaineCompetenceRepository.findAll().size();

        // Get the domaineCompetence
        restDomaineCompetenceMockMvc.perform(delete("/api/domaine-competences/{id}", domaineCompetence.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean domaineCompetenceExistsInEs = domaineCompetenceSearchRepository.exists(domaineCompetence.getId());
        assertThat(domaineCompetenceExistsInEs).isFalse();

        // Validate the database is empty
        List<DomaineCompetence> domaineCompetenceList = domaineCompetenceRepository.findAll();
        assertThat(domaineCompetenceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDomaineCompetence() throws Exception {
        // Initialize the database
        domaineCompetenceRepository.saveAndFlush(domaineCompetence);
        domaineCompetenceSearchRepository.save(domaineCompetence);

        // Search the domaineCompetence
        restDomaineCompetenceMockMvc.perform(get("/api/_search/domaine-competences?query=id:" + domaineCompetence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domaineCompetence.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DomaineCompetence.class);
    }
}
