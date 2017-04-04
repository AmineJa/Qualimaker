package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Conge;
import org.qualimaker.repository.CongeRepository;
import org.qualimaker.repository.search.CongeSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.qualimaker.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CongeResource REST controller.
 *
 * @see CongeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class CongeResourceIntTest {

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_D = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_D = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_F = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_F = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CongeRepository congeRepository;

    @Autowired
    private CongeSearchRepository congeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCongeMockMvc;

    private Conge conge;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CongeResource congeResource = new CongeResource(congeRepository, congeSearchRepository);
        this.restCongeMockMvc = MockMvcBuilders.standaloneSetup(congeResource)
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
    public static Conge createEntity(EntityManager em) {
        Conge conge = new Conge()
            .statut(DEFAULT_STATUT)
            .dateD(DEFAULT_DATE_D)
            .dateF(DEFAULT_DATE_F);
        return conge;
    }

    @Before
    public void initTest() {
        congeSearchRepository.deleteAll();
        conge = createEntity(em);
    }

    @Test
    @Transactional
    public void createConge() throws Exception {
        int databaseSizeBeforeCreate = congeRepository.findAll().size();

        // Create the Conge
        restCongeMockMvc.perform(post("/api/conges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isCreated());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeCreate + 1);
        Conge testConge = congeList.get(congeList.size() - 1);
        assertThat(testConge.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testConge.getDateD()).isEqualTo(DEFAULT_DATE_D);
        assertThat(testConge.getDateF()).isEqualTo(DEFAULT_DATE_F);

        // Validate the Conge in Elasticsearch
        Conge congeEs = congeSearchRepository.findOne(testConge.getId());
        assertThat(congeEs).isEqualToComparingFieldByField(testConge);
    }

    @Test
    @Transactional
    public void createCongeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = congeRepository.findAll().size();

        // Create the Conge with an existing ID
        conge.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCongeMockMvc.perform(post("/api/conges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllConges() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);

        // Get all the congeList
        restCongeMockMvc.perform(get("/api/conges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conge.getId().intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateD").value(hasItem(sameInstant(DEFAULT_DATE_D))))
            .andExpect(jsonPath("$.[*].dateF").value(hasItem(sameInstant(DEFAULT_DATE_F))));
    }

    @Test
    @Transactional
    public void getConge() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);

        // Get the conge
        restCongeMockMvc.perform(get("/api/conges/{id}", conge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conge.getId().intValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.dateD").value(sameInstant(DEFAULT_DATE_D)))
            .andExpect(jsonPath("$.dateF").value(sameInstant(DEFAULT_DATE_F)));
    }

    @Test
    @Transactional
    public void getNonExistingConge() throws Exception {
        // Get the conge
        restCongeMockMvc.perform(get("/api/conges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConge() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);
        congeSearchRepository.save(conge);
        int databaseSizeBeforeUpdate = congeRepository.findAll().size();

        // Update the conge
        Conge updatedConge = congeRepository.findOne(conge.getId());
        updatedConge
            .statut(UPDATED_STATUT)
            .dateD(UPDATED_DATE_D)
            .dateF(UPDATED_DATE_F);

        restCongeMockMvc.perform(put("/api/conges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConge)))
            .andExpect(status().isOk());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate);
        Conge testConge = congeList.get(congeList.size() - 1);
        assertThat(testConge.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testConge.getDateD()).isEqualTo(UPDATED_DATE_D);
        assertThat(testConge.getDateF()).isEqualTo(UPDATED_DATE_F);

        // Validate the Conge in Elasticsearch
        Conge congeEs = congeSearchRepository.findOne(testConge.getId());
        assertThat(congeEs).isEqualToComparingFieldByField(testConge);
    }

    @Test
    @Transactional
    public void updateNonExistingConge() throws Exception {
        int databaseSizeBeforeUpdate = congeRepository.findAll().size();

        // Create the Conge

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCongeMockMvc.perform(put("/api/conges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conge)))
            .andExpect(status().isCreated());

        // Validate the Conge in the database
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConge() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);
        congeSearchRepository.save(conge);
        int databaseSizeBeforeDelete = congeRepository.findAll().size();

        // Get the conge
        restCongeMockMvc.perform(delete("/api/conges/{id}", conge.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean congeExistsInEs = congeSearchRepository.exists(conge.getId());
        assertThat(congeExistsInEs).isFalse();

        // Validate the database is empty
        List<Conge> congeList = congeRepository.findAll();
        assertThat(congeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchConge() throws Exception {
        // Initialize the database
        congeRepository.saveAndFlush(conge);
        congeSearchRepository.save(conge);

        // Search the conge
        restCongeMockMvc.perform(get("/api/_search/conges?query=id:" + conge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conge.getId().intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateD").value(hasItem(sameInstant(DEFAULT_DATE_D))))
            .andExpect(jsonPath("$.[*].dateF").value(hasItem(sameInstant(DEFAULT_DATE_F))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conge.class);
    }
}
