package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.LieuxClassement;
import org.qualimaker.repository.LieuxClassementRepository;
import org.qualimaker.repository.search.LieuxClassementSearchRepository;
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
 * Test class for the LieuxClassementResource REST controller.
 *
 * @see LieuxClassementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class LieuxClassementResourceIntTest {

    private static final String DEFAULT_LIEUX = "AAAAAAAAAA";
    private static final String UPDATED_LIEUX = "BBBBBBBBBB";

    @Autowired
    private LieuxClassementRepository lieuxClassementRepository;

    @Autowired
    private LieuxClassementSearchRepository lieuxClassementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLieuxClassementMockMvc;

    private LieuxClassement lieuxClassement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LieuxClassementResource lieuxClassementResource = new LieuxClassementResource(lieuxClassementRepository, lieuxClassementSearchRepository);
        this.restLieuxClassementMockMvc = MockMvcBuilders.standaloneSetup(lieuxClassementResource)
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
    public static LieuxClassement createEntity(EntityManager em) {
        LieuxClassement lieuxClassement = new LieuxClassement()
            .lieux(DEFAULT_LIEUX);
        return lieuxClassement;
    }

    @Before
    public void initTest() {
        lieuxClassementSearchRepository.deleteAll();
        lieuxClassement = createEntity(em);
    }

    @Test
    @Transactional
    public void createLieuxClassement() throws Exception {
        int databaseSizeBeforeCreate = lieuxClassementRepository.findAll().size();

        // Create the LieuxClassement
        restLieuxClassementMockMvc.perform(post("/api/lieux-classements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lieuxClassement)))
            .andExpect(status().isCreated());

        // Validate the LieuxClassement in the database
        List<LieuxClassement> lieuxClassementList = lieuxClassementRepository.findAll();
        assertThat(lieuxClassementList).hasSize(databaseSizeBeforeCreate + 1);
        LieuxClassement testLieuxClassement = lieuxClassementList.get(lieuxClassementList.size() - 1);
        assertThat(testLieuxClassement.getLieux()).isEqualTo(DEFAULT_LIEUX);

        // Validate the LieuxClassement in Elasticsearch
        LieuxClassement lieuxClassementEs = lieuxClassementSearchRepository.findOne(testLieuxClassement.getId());
        assertThat(lieuxClassementEs).isEqualToComparingFieldByField(testLieuxClassement);
    }

    @Test
    @Transactional
    public void createLieuxClassementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lieuxClassementRepository.findAll().size();

        // Create the LieuxClassement with an existing ID
        lieuxClassement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLieuxClassementMockMvc.perform(post("/api/lieux-classements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lieuxClassement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LieuxClassement> lieuxClassementList = lieuxClassementRepository.findAll();
        assertThat(lieuxClassementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLieuxClassements() throws Exception {
        // Initialize the database
        lieuxClassementRepository.saveAndFlush(lieuxClassement);

        // Get all the lieuxClassementList
        restLieuxClassementMockMvc.perform(get("/api/lieux-classements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lieuxClassement.getId().intValue())))
            .andExpect(jsonPath("$.[*].lieux").value(hasItem(DEFAULT_LIEUX.toString())));
    }

    @Test
    @Transactional
    public void getLieuxClassement() throws Exception {
        // Initialize the database
        lieuxClassementRepository.saveAndFlush(lieuxClassement);

        // Get the lieuxClassement
        restLieuxClassementMockMvc.perform(get("/api/lieux-classements/{id}", lieuxClassement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lieuxClassement.getId().intValue()))
            .andExpect(jsonPath("$.lieux").value(DEFAULT_LIEUX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLieuxClassement() throws Exception {
        // Get the lieuxClassement
        restLieuxClassementMockMvc.perform(get("/api/lieux-classements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLieuxClassement() throws Exception {
        // Initialize the database
        lieuxClassementRepository.saveAndFlush(lieuxClassement);
        lieuxClassementSearchRepository.save(lieuxClassement);
        int databaseSizeBeforeUpdate = lieuxClassementRepository.findAll().size();

        // Update the lieuxClassement
        LieuxClassement updatedLieuxClassement = lieuxClassementRepository.findOne(lieuxClassement.getId());
        updatedLieuxClassement
            .lieux(UPDATED_LIEUX);

        restLieuxClassementMockMvc.perform(put("/api/lieux-classements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLieuxClassement)))
            .andExpect(status().isOk());

        // Validate the LieuxClassement in the database
        List<LieuxClassement> lieuxClassementList = lieuxClassementRepository.findAll();
        assertThat(lieuxClassementList).hasSize(databaseSizeBeforeUpdate);
        LieuxClassement testLieuxClassement = lieuxClassementList.get(lieuxClassementList.size() - 1);
        assertThat(testLieuxClassement.getLieux()).isEqualTo(UPDATED_LIEUX);

        // Validate the LieuxClassement in Elasticsearch
        LieuxClassement lieuxClassementEs = lieuxClassementSearchRepository.findOne(testLieuxClassement.getId());
        assertThat(lieuxClassementEs).isEqualToComparingFieldByField(testLieuxClassement);
    }

    @Test
    @Transactional
    public void updateNonExistingLieuxClassement() throws Exception {
        int databaseSizeBeforeUpdate = lieuxClassementRepository.findAll().size();

        // Create the LieuxClassement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLieuxClassementMockMvc.perform(put("/api/lieux-classements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lieuxClassement)))
            .andExpect(status().isCreated());

        // Validate the LieuxClassement in the database
        List<LieuxClassement> lieuxClassementList = lieuxClassementRepository.findAll();
        assertThat(lieuxClassementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLieuxClassement() throws Exception {
        // Initialize the database
        lieuxClassementRepository.saveAndFlush(lieuxClassement);
        lieuxClassementSearchRepository.save(lieuxClassement);
        int databaseSizeBeforeDelete = lieuxClassementRepository.findAll().size();

        // Get the lieuxClassement
        restLieuxClassementMockMvc.perform(delete("/api/lieux-classements/{id}", lieuxClassement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean lieuxClassementExistsInEs = lieuxClassementSearchRepository.exists(lieuxClassement.getId());
        assertThat(lieuxClassementExistsInEs).isFalse();

        // Validate the database is empty
        List<LieuxClassement> lieuxClassementList = lieuxClassementRepository.findAll();
        assertThat(lieuxClassementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLieuxClassement() throws Exception {
        // Initialize the database
        lieuxClassementRepository.saveAndFlush(lieuxClassement);
        lieuxClassementSearchRepository.save(lieuxClassement);

        // Search the lieuxClassement
        restLieuxClassementMockMvc.perform(get("/api/_search/lieux-classements?query=id:" + lieuxClassement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lieuxClassement.getId().intValue())))
            .andExpect(jsonPath("$.[*].lieux").value(hasItem(DEFAULT_LIEUX.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LieuxClassement.class);
    }
}
