package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Profilsfonction;
import org.qualimaker.repository.ProfilsfonctionRepository;
import org.qualimaker.repository.search.ProfilsfonctionSearchRepository;
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
 * Test class for the ProfilsfonctionResource REST controller.
 *
 * @see ProfilsfonctionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class ProfilsfonctionResourceIntTest {

    private static final String DEFAULT_PROFIL = "AAAAAAAAAA";
    private static final String UPDATED_PROFIL = "BBBBBBBBBB";

    @Autowired
    private ProfilsfonctionRepository profilsfonctionRepository;

    @Autowired
    private ProfilsfonctionSearchRepository profilsfonctionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfilsfonctionMockMvc;

    private Profilsfonction profilsfonction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProfilsfonctionResource profilsfonctionResource = new ProfilsfonctionResource(profilsfonctionRepository, profilsfonctionSearchRepository);
        this.restProfilsfonctionMockMvc = MockMvcBuilders.standaloneSetup(profilsfonctionResource)
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
    public static Profilsfonction createEntity(EntityManager em) {
        Profilsfonction profilsfonction = new Profilsfonction()
            .profil(DEFAULT_PROFIL);
        return profilsfonction;
    }

    @Before
    public void initTest() {
        profilsfonctionSearchRepository.deleteAll();
        profilsfonction = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfilsfonction() throws Exception {
        int databaseSizeBeforeCreate = profilsfonctionRepository.findAll().size();

        // Create the Profilsfonction
        restProfilsfonctionMockMvc.perform(post("/api/profilsfonctions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profilsfonction)))
            .andExpect(status().isCreated());

        // Validate the Profilsfonction in the database
        List<Profilsfonction> profilsfonctionList = profilsfonctionRepository.findAll();
        assertThat(profilsfonctionList).hasSize(databaseSizeBeforeCreate + 1);
        Profilsfonction testProfilsfonction = profilsfonctionList.get(profilsfonctionList.size() - 1);
        assertThat(testProfilsfonction.getProfil()).isEqualTo(DEFAULT_PROFIL);

        // Validate the Profilsfonction in Elasticsearch
        Profilsfonction profilsfonctionEs = profilsfonctionSearchRepository.findOne(testProfilsfonction.getId());
        assertThat(profilsfonctionEs).isEqualToComparingFieldByField(testProfilsfonction);
    }

    @Test
    @Transactional
    public void createProfilsfonctionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profilsfonctionRepository.findAll().size();

        // Create the Profilsfonction with an existing ID
        profilsfonction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfilsfonctionMockMvc.perform(post("/api/profilsfonctions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profilsfonction)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Profilsfonction> profilsfonctionList = profilsfonctionRepository.findAll();
        assertThat(profilsfonctionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProfilIsRequired() throws Exception {
        int databaseSizeBeforeTest = profilsfonctionRepository.findAll().size();
        // set the field null
        profilsfonction.setProfil(null);

        // Create the Profilsfonction, which fails.

        restProfilsfonctionMockMvc.perform(post("/api/profilsfonctions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profilsfonction)))
            .andExpect(status().isBadRequest());

        List<Profilsfonction> profilsfonctionList = profilsfonctionRepository.findAll();
        assertThat(profilsfonctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfilsfonctions() throws Exception {
        // Initialize the database
        profilsfonctionRepository.saveAndFlush(profilsfonction);

        // Get all the profilsfonctionList
        restProfilsfonctionMockMvc.perform(get("/api/profilsfonctions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profilsfonction.getId().intValue())))
            .andExpect(jsonPath("$.[*].profil").value(hasItem(DEFAULT_PROFIL.toString())));
    }

    @Test
    @Transactional
    public void getProfilsfonction() throws Exception {
        // Initialize the database
        profilsfonctionRepository.saveAndFlush(profilsfonction);

        // Get the profilsfonction
        restProfilsfonctionMockMvc.perform(get("/api/profilsfonctions/{id}", profilsfonction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profilsfonction.getId().intValue()))
            .andExpect(jsonPath("$.profil").value(DEFAULT_PROFIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProfilsfonction() throws Exception {
        // Get the profilsfonction
        restProfilsfonctionMockMvc.perform(get("/api/profilsfonctions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfilsfonction() throws Exception {
        // Initialize the database
        profilsfonctionRepository.saveAndFlush(profilsfonction);
        profilsfonctionSearchRepository.save(profilsfonction);
        int databaseSizeBeforeUpdate = profilsfonctionRepository.findAll().size();

        // Update the profilsfonction
        Profilsfonction updatedProfilsfonction = profilsfonctionRepository.findOne(profilsfonction.getId());
        updatedProfilsfonction
            .profil(UPDATED_PROFIL);

        restProfilsfonctionMockMvc.perform(put("/api/profilsfonctions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfilsfonction)))
            .andExpect(status().isOk());

        // Validate the Profilsfonction in the database
        List<Profilsfonction> profilsfonctionList = profilsfonctionRepository.findAll();
        assertThat(profilsfonctionList).hasSize(databaseSizeBeforeUpdate);
        Profilsfonction testProfilsfonction = profilsfonctionList.get(profilsfonctionList.size() - 1);
        assertThat(testProfilsfonction.getProfil()).isEqualTo(UPDATED_PROFIL);

        // Validate the Profilsfonction in Elasticsearch
        Profilsfonction profilsfonctionEs = profilsfonctionSearchRepository.findOne(testProfilsfonction.getId());
        assertThat(profilsfonctionEs).isEqualToComparingFieldByField(testProfilsfonction);
    }

    @Test
    @Transactional
    public void updateNonExistingProfilsfonction() throws Exception {
        int databaseSizeBeforeUpdate = profilsfonctionRepository.findAll().size();

        // Create the Profilsfonction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProfilsfonctionMockMvc.perform(put("/api/profilsfonctions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profilsfonction)))
            .andExpect(status().isCreated());

        // Validate the Profilsfonction in the database
        List<Profilsfonction> profilsfonctionList = profilsfonctionRepository.findAll();
        assertThat(profilsfonctionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProfilsfonction() throws Exception {
        // Initialize the database
        profilsfonctionRepository.saveAndFlush(profilsfonction);
        profilsfonctionSearchRepository.save(profilsfonction);
        int databaseSizeBeforeDelete = profilsfonctionRepository.findAll().size();

        // Get the profilsfonction
        restProfilsfonctionMockMvc.perform(delete("/api/profilsfonctions/{id}", profilsfonction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean profilsfonctionExistsInEs = profilsfonctionSearchRepository.exists(profilsfonction.getId());
        assertThat(profilsfonctionExistsInEs).isFalse();

        // Validate the database is empty
        List<Profilsfonction> profilsfonctionList = profilsfonctionRepository.findAll();
        assertThat(profilsfonctionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProfilsfonction() throws Exception {
        // Initialize the database
        profilsfonctionRepository.saveAndFlush(profilsfonction);
        profilsfonctionSearchRepository.save(profilsfonction);

        // Search the profilsfonction
        restProfilsfonctionMockMvc.perform(get("/api/_search/profilsfonctions?query=id:" + profilsfonction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profilsfonction.getId().intValue())))
            .andExpect(jsonPath("$.[*].profil").value(hasItem(DEFAULT_PROFIL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profilsfonction.class);
    }
}
