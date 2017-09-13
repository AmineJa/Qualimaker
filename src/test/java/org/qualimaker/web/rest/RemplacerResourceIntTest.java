package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Remplacer;
import org.qualimaker.domain.Employe;
import org.qualimaker.domain.Employe;
import org.qualimaker.domain.Profilsfonction;
import org.qualimaker.repository.RemplacerRepository;
import org.qualimaker.repository.search.RemplacerSearchRepository;
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
 * Test class for the RemplacerResource REST controller.
 *
 * @see RemplacerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class RemplacerResourceIntTest {

    private static final ZonedDateTime DEFAULT_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private RemplacerRepository remplacerRepository;

    @Autowired
    private RemplacerSearchRepository remplacerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRemplacerMockMvc;

    private Remplacer remplacer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RemplacerResource remplacerResource = new RemplacerResource(remplacerRepository, remplacerSearchRepository);
        this.restRemplacerMockMvc = MockMvcBuilders.standaloneSetup(remplacerResource)
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
    public static Remplacer createEntity(EntityManager em) {
        Remplacer remplacer = new Remplacer()
            .debut(DEFAULT_DEBUT)
            .fin(DEFAULT_FIN);
        // Add required entity
        Employe employe = EmployeResourceIntTest.createEntity(em);
        em.persist(employe);
        em.flush();
        remplacer.setEmploye(employe);
        // Add required entity
        Employe nouveau = EmployeResourceIntTest.createEntity(em);
        em.persist(nouveau);
        em.flush();
        remplacer.setNouveau(nouveau);
        // Add required entity
        Profilsfonction profilsfonction = ProfilsfonctionResourceIntTest.createEntity(em);
        em.persist(profilsfonction);
        em.flush();
        remplacer.setProfilsfonction(profilsfonction);
        return remplacer;
    }

    @Before
    public void initTest() {
        remplacerSearchRepository.deleteAll();
        remplacer = createEntity(em);
    }

    @Test
    @Transactional
    public void createRemplacer() throws Exception {
        int databaseSizeBeforeCreate = remplacerRepository.findAll().size();

        // Create the Remplacer
        restRemplacerMockMvc.perform(post("/api/remplacers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remplacer)))
            .andExpect(status().isCreated());

        // Validate the Remplacer in the database
        List<Remplacer> remplacerList = remplacerRepository.findAll();
        assertThat(remplacerList).hasSize(databaseSizeBeforeCreate + 1);
        Remplacer testRemplacer = remplacerList.get(remplacerList.size() - 1);
        assertThat(testRemplacer.getDebut()).isEqualTo(DEFAULT_DEBUT);
        assertThat(testRemplacer.getFin()).isEqualTo(DEFAULT_FIN);

        // Validate the Remplacer in Elasticsearch
        Remplacer remplacerEs = remplacerSearchRepository.findOne(testRemplacer.getId());
        assertThat(remplacerEs).isEqualToComparingFieldByField(testRemplacer);
    }

    @Test
    @Transactional
    public void createRemplacerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = remplacerRepository.findAll().size();

        // Create the Remplacer with an existing ID
        remplacer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRemplacerMockMvc.perform(post("/api/remplacers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remplacer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Remplacer> remplacerList = remplacerRepository.findAll();
        assertThat(remplacerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = remplacerRepository.findAll().size();
        // set the field null
        remplacer.setDebut(null);

        // Create the Remplacer, which fails.

        restRemplacerMockMvc.perform(post("/api/remplacers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remplacer)))
            .andExpect(status().isBadRequest());

        List<Remplacer> remplacerList = remplacerRepository.findAll();
        assertThat(remplacerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRemplacers() throws Exception {
        // Initialize the database
        remplacerRepository.saveAndFlush(remplacer);

        // Get all the remplacerList
        restRemplacerMockMvc.perform(get("/api/remplacers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(remplacer.getId().intValue())))
            .andExpect(jsonPath("$.[*].debut").value(hasItem(sameInstant(DEFAULT_DEBUT))))
            .andExpect(jsonPath("$.[*].fin").value(hasItem(sameInstant(DEFAULT_FIN))));
    }

    @Test
    @Transactional
    public void getRemplacer() throws Exception {
        // Initialize the database
        remplacerRepository.saveAndFlush(remplacer);

        // Get the remplacer
        restRemplacerMockMvc.perform(get("/api/remplacers/{id}", remplacer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(remplacer.getId().intValue()))
            .andExpect(jsonPath("$.debut").value(sameInstant(DEFAULT_DEBUT)))
            .andExpect(jsonPath("$.fin").value(sameInstant(DEFAULT_FIN)));
    }

    @Test
    @Transactional
    public void getNonExistingRemplacer() throws Exception {
        // Get the remplacer
        restRemplacerMockMvc.perform(get("/api/remplacers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRemplacer() throws Exception {
        // Initialize the database
        remplacerRepository.saveAndFlush(remplacer);
        remplacerSearchRepository.save(remplacer);
        int databaseSizeBeforeUpdate = remplacerRepository.findAll().size();

        // Update the remplacer
        Remplacer updatedRemplacer = remplacerRepository.findOne(remplacer.getId());
        updatedRemplacer
            .debut(UPDATED_DEBUT)
            .fin(UPDATED_FIN);

        restRemplacerMockMvc.perform(put("/api/remplacers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRemplacer)))
            .andExpect(status().isOk());

        // Validate the Remplacer in the database
        List<Remplacer> remplacerList = remplacerRepository.findAll();
        assertThat(remplacerList).hasSize(databaseSizeBeforeUpdate);
        Remplacer testRemplacer = remplacerList.get(remplacerList.size() - 1);
        assertThat(testRemplacer.getDebut()).isEqualTo(UPDATED_DEBUT);
        assertThat(testRemplacer.getFin()).isEqualTo(UPDATED_FIN);

        // Validate the Remplacer in Elasticsearch
        Remplacer remplacerEs = remplacerSearchRepository.findOne(testRemplacer.getId());
        assertThat(remplacerEs).isEqualToComparingFieldByField(testRemplacer);
    }

    @Test
    @Transactional
    public void updateNonExistingRemplacer() throws Exception {
        int databaseSizeBeforeUpdate = remplacerRepository.findAll().size();

        // Create the Remplacer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRemplacerMockMvc.perform(put("/api/remplacers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remplacer)))
            .andExpect(status().isCreated());

        // Validate the Remplacer in the database
        List<Remplacer> remplacerList = remplacerRepository.findAll();
        assertThat(remplacerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRemplacer() throws Exception {
        // Initialize the database
        remplacerRepository.saveAndFlush(remplacer);
        remplacerSearchRepository.save(remplacer);
        int databaseSizeBeforeDelete = remplacerRepository.findAll().size();

        // Get the remplacer
        restRemplacerMockMvc.perform(delete("/api/remplacers/{id}", remplacer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean remplacerExistsInEs = remplacerSearchRepository.exists(remplacer.getId());
        assertThat(remplacerExistsInEs).isFalse();

        // Validate the database is empty
        List<Remplacer> remplacerList = remplacerRepository.findAll();
        assertThat(remplacerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRemplacer() throws Exception {
        // Initialize the database
        remplacerRepository.saveAndFlush(remplacer);
        remplacerSearchRepository.save(remplacer);

        // Search the remplacer
        restRemplacerMockMvc.perform(get("/api/_search/remplacers?query=id:" + remplacer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(remplacer.getId().intValue())))
            .andExpect(jsonPath("$.[*].debut").value(hasItem(sameInstant(DEFAULT_DEBUT))))
            .andExpect(jsonPath("$.[*].fin").value(hasItem(sameInstant(DEFAULT_FIN))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Remplacer.class);
    }
}
