package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Naturediscipline;
import org.qualimaker.repository.NaturedisciplineRepository;
import org.qualimaker.repository.search.NaturedisciplineSearchRepository;
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
 * Test class for the NaturedisciplineResource REST controller.
 *
 * @see NaturedisciplineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class NaturedisciplineResourceIntTest {

    private static final String DEFAULT_NATURE = "AAAAAAAAAA";
    private static final String UPDATED_NATURE = "BBBBBBBBBB";

    @Autowired
    private NaturedisciplineRepository naturedisciplineRepository;

    @Autowired
    private NaturedisciplineSearchRepository naturedisciplineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNaturedisciplineMockMvc;

    private Naturediscipline naturediscipline;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NaturedisciplineResource naturedisciplineResource = new NaturedisciplineResource(naturedisciplineRepository, naturedisciplineSearchRepository);
        this.restNaturedisciplineMockMvc = MockMvcBuilders.standaloneSetup(naturedisciplineResource)
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
    public static Naturediscipline createEntity(EntityManager em) {
        Naturediscipline naturediscipline = new Naturediscipline()
            .nature(DEFAULT_NATURE);
        return naturediscipline;
    }

    @Before
    public void initTest() {
        naturedisciplineSearchRepository.deleteAll();
        naturediscipline = createEntity(em);
    }

    @Test
    @Transactional
    public void createNaturediscipline() throws Exception {
        int databaseSizeBeforeCreate = naturedisciplineRepository.findAll().size();

        // Create the Naturediscipline
        restNaturedisciplineMockMvc.perform(post("/api/naturedisciplines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturediscipline)))
            .andExpect(status().isCreated());

        // Validate the Naturediscipline in the database
        List<Naturediscipline> naturedisciplineList = naturedisciplineRepository.findAll();
        assertThat(naturedisciplineList).hasSize(databaseSizeBeforeCreate + 1);
        Naturediscipline testNaturediscipline = naturedisciplineList.get(naturedisciplineList.size() - 1);
        assertThat(testNaturediscipline.getNature()).isEqualTo(DEFAULT_NATURE);

        // Validate the Naturediscipline in Elasticsearch
        Naturediscipline naturedisciplineEs = naturedisciplineSearchRepository.findOne(testNaturediscipline.getId());
        assertThat(naturedisciplineEs).isEqualToComparingFieldByField(testNaturediscipline);
    }

    @Test
    @Transactional
    public void createNaturedisciplineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = naturedisciplineRepository.findAll().size();

        // Create the Naturediscipline with an existing ID
        naturediscipline.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNaturedisciplineMockMvc.perform(post("/api/naturedisciplines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturediscipline)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Naturediscipline> naturedisciplineList = naturedisciplineRepository.findAll();
        assertThat(naturedisciplineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNaturedisciplines() throws Exception {
        // Initialize the database
        naturedisciplineRepository.saveAndFlush(naturediscipline);

        // Get all the naturedisciplineList
        restNaturedisciplineMockMvc.perform(get("/api/naturedisciplines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(naturediscipline.getId().intValue())))
            .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE.toString())));
    }

    @Test
    @Transactional
    public void getNaturediscipline() throws Exception {
        // Initialize the database
        naturedisciplineRepository.saveAndFlush(naturediscipline);

        // Get the naturediscipline
        restNaturedisciplineMockMvc.perform(get("/api/naturedisciplines/{id}", naturediscipline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(naturediscipline.getId().intValue()))
            .andExpect(jsonPath("$.nature").value(DEFAULT_NATURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNaturediscipline() throws Exception {
        // Get the naturediscipline
        restNaturedisciplineMockMvc.perform(get("/api/naturedisciplines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNaturediscipline() throws Exception {
        // Initialize the database
        naturedisciplineRepository.saveAndFlush(naturediscipline);
        naturedisciplineSearchRepository.save(naturediscipline);
        int databaseSizeBeforeUpdate = naturedisciplineRepository.findAll().size();

        // Update the naturediscipline
        Naturediscipline updatedNaturediscipline = naturedisciplineRepository.findOne(naturediscipline.getId());
        updatedNaturediscipline
            .nature(UPDATED_NATURE);

        restNaturedisciplineMockMvc.perform(put("/api/naturedisciplines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNaturediscipline)))
            .andExpect(status().isOk());

        // Validate the Naturediscipline in the database
        List<Naturediscipline> naturedisciplineList = naturedisciplineRepository.findAll();
        assertThat(naturedisciplineList).hasSize(databaseSizeBeforeUpdate);
        Naturediscipline testNaturediscipline = naturedisciplineList.get(naturedisciplineList.size() - 1);
        assertThat(testNaturediscipline.getNature()).isEqualTo(UPDATED_NATURE);

        // Validate the Naturediscipline in Elasticsearch
        Naturediscipline naturedisciplineEs = naturedisciplineSearchRepository.findOne(testNaturediscipline.getId());
        assertThat(naturedisciplineEs).isEqualToComparingFieldByField(testNaturediscipline);
    }

    @Test
    @Transactional
    public void updateNonExistingNaturediscipline() throws Exception {
        int databaseSizeBeforeUpdate = naturedisciplineRepository.findAll().size();

        // Create the Naturediscipline

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNaturedisciplineMockMvc.perform(put("/api/naturedisciplines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturediscipline)))
            .andExpect(status().isCreated());

        // Validate the Naturediscipline in the database
        List<Naturediscipline> naturedisciplineList = naturedisciplineRepository.findAll();
        assertThat(naturedisciplineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNaturediscipline() throws Exception {
        // Initialize the database
        naturedisciplineRepository.saveAndFlush(naturediscipline);
        naturedisciplineSearchRepository.save(naturediscipline);
        int databaseSizeBeforeDelete = naturedisciplineRepository.findAll().size();

        // Get the naturediscipline
        restNaturedisciplineMockMvc.perform(delete("/api/naturedisciplines/{id}", naturediscipline.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean naturedisciplineExistsInEs = naturedisciplineSearchRepository.exists(naturediscipline.getId());
        assertThat(naturedisciplineExistsInEs).isFalse();

        // Validate the database is empty
        List<Naturediscipline> naturedisciplineList = naturedisciplineRepository.findAll();
        assertThat(naturedisciplineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNaturediscipline() throws Exception {
        // Initialize the database
        naturedisciplineRepository.saveAndFlush(naturediscipline);
        naturedisciplineSearchRepository.save(naturediscipline);

        // Search the naturediscipline
        restNaturedisciplineMockMvc.perform(get("/api/_search/naturedisciplines?query=id:" + naturediscipline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(naturediscipline.getId().intValue())))
            .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Naturediscipline.class);
    }
}
