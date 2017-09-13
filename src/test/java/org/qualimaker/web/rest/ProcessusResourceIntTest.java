package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Processus;
import org.qualimaker.repository.ProcessusRepository;
import org.qualimaker.repository.search.ProcessusSearchRepository;
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
 * Test class for the ProcessusResource REST controller.
 *
 * @see ProcessusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class ProcessusResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private ProcessusRepository processusRepository;

    @Autowired
    private ProcessusSearchRepository processusSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcessusMockMvc;

    private Processus processus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProcessusResource processusResource = new ProcessusResource(processusRepository, processusSearchRepository);
        this.restProcessusMockMvc = MockMvcBuilders.standaloneSetup(processusResource)
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
    public static Processus createEntity(EntityManager em) {
        Processus processus = new Processus()
            .libelle(DEFAULT_LIBELLE);
        return processus;
    }

    @Before
    public void initTest() {
        processusSearchRepository.deleteAll();
        processus = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcessus() throws Exception {
        int databaseSizeBeforeCreate = processusRepository.findAll().size();

        // Create the Processus
        restProcessusMockMvc.perform(post("/api/processuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processus)))
            .andExpect(status().isCreated());

        // Validate the Processus in the database
        List<Processus> processusList = processusRepository.findAll();
        assertThat(processusList).hasSize(databaseSizeBeforeCreate + 1);
        Processus testProcessus = processusList.get(processusList.size() - 1);
        assertThat(testProcessus.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the Processus in Elasticsearch
        Processus processusEs = processusSearchRepository.findOne(testProcessus.getId());
        assertThat(processusEs).isEqualToComparingFieldByField(testProcessus);
    }

    @Test
    @Transactional
    public void createProcessusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = processusRepository.findAll().size();

        // Create the Processus with an existing ID
        processus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessusMockMvc.perform(post("/api/processuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processus)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Processus> processusList = processusRepository.findAll();
        assertThat(processusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProcessuses() throws Exception {
        // Initialize the database
        processusRepository.saveAndFlush(processus);

        // Get all the processusList
        restProcessusMockMvc.perform(get("/api/processuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processus.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getProcessus() throws Exception {
        // Initialize the database
        processusRepository.saveAndFlush(processus);

        // Get the processus
        restProcessusMockMvc.perform(get("/api/processuses/{id}", processus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(processus.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProcessus() throws Exception {
        // Get the processus
        restProcessusMockMvc.perform(get("/api/processuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcessus() throws Exception {
        // Initialize the database
        processusRepository.saveAndFlush(processus);
        processusSearchRepository.save(processus);
        int databaseSizeBeforeUpdate = processusRepository.findAll().size();

        // Update the processus
        Processus updatedProcessus = processusRepository.findOne(processus.getId());
        updatedProcessus
            .libelle(UPDATED_LIBELLE);

        restProcessusMockMvc.perform(put("/api/processuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcessus)))
            .andExpect(status().isOk());

        // Validate the Processus in the database
        List<Processus> processusList = processusRepository.findAll();
        assertThat(processusList).hasSize(databaseSizeBeforeUpdate);
        Processus testProcessus = processusList.get(processusList.size() - 1);
        assertThat(testProcessus.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the Processus in Elasticsearch
        Processus processusEs = processusSearchRepository.findOne(testProcessus.getId());
        assertThat(processusEs).isEqualToComparingFieldByField(testProcessus);
    }

    @Test
    @Transactional
    public void updateNonExistingProcessus() throws Exception {
        int databaseSizeBeforeUpdate = processusRepository.findAll().size();

        // Create the Processus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcessusMockMvc.perform(put("/api/processuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processus)))
            .andExpect(status().isCreated());

        // Validate the Processus in the database
        List<Processus> processusList = processusRepository.findAll();
        assertThat(processusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcessus() throws Exception {
        // Initialize the database
        processusRepository.saveAndFlush(processus);
        processusSearchRepository.save(processus);
        int databaseSizeBeforeDelete = processusRepository.findAll().size();

        // Get the processus
        restProcessusMockMvc.perform(delete("/api/processuses/{id}", processus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean processusExistsInEs = processusSearchRepository.exists(processus.getId());
        assertThat(processusExistsInEs).isFalse();

        // Validate the database is empty
        List<Processus> processusList = processusRepository.findAll();
        assertThat(processusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProcessus() throws Exception {
        // Initialize the database
        processusRepository.saveAndFlush(processus);
        processusSearchRepository.save(processus);

        // Search the processus
        restProcessusMockMvc.perform(get("/api/_search/processuses?query=id:" + processus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processus.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Processus.class);
    }
}
