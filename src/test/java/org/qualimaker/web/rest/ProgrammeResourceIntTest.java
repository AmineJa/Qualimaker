package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Programme;
import org.qualimaker.repository.ProgrammeRepository;
import org.qualimaker.repository.search.ProgrammeSearchRepository;
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
 * Test class for the ProgrammeResource REST controller.
 *
 * @see ProgrammeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class ProgrammeResourceIntTest {

    private static final String DEFAULT_MATIN = "AAAAAAAAAA";
    private static final String UPDATED_MATIN = "BBBBBBBBBB";

    private static final String DEFAULT_APRESMIDI = "AAAAAAAAAA";
    private static final String UPDATED_APRESMIDI = "BBBBBBBBBB";

    @Autowired
    private ProgrammeRepository programmeRepository;

    @Autowired
    private ProgrammeSearchRepository programmeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProgrammeMockMvc;

    private Programme programme;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProgrammeResource programmeResource = new ProgrammeResource(programmeRepository, programmeSearchRepository);
        this.restProgrammeMockMvc = MockMvcBuilders.standaloneSetup(programmeResource)
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
    public static Programme createEntity(EntityManager em) {
        Programme programme = new Programme()
            .matin(DEFAULT_MATIN)
            .apresmidi(DEFAULT_APRESMIDI);
        return programme;
    }

    @Before
    public void initTest() {
        programmeSearchRepository.deleteAll();
        programme = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgramme() throws Exception {
        int databaseSizeBeforeCreate = programmeRepository.findAll().size();

        // Create the Programme
        restProgrammeMockMvc.perform(post("/api/programmes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isCreated());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeCreate + 1);
        Programme testProgramme = programmeList.get(programmeList.size() - 1);
        assertThat(testProgramme.getMatin()).isEqualTo(DEFAULT_MATIN);
        assertThat(testProgramme.getApresmidi()).isEqualTo(DEFAULT_APRESMIDI);

        // Validate the Programme in Elasticsearch
        Programme programmeEs = programmeSearchRepository.findOne(testProgramme.getId());
        assertThat(programmeEs).isEqualToComparingFieldByField(testProgramme);
    }

    @Test
    @Transactional
    public void createProgrammeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programmeRepository.findAll().size();

        // Create the Programme with an existing ID
        programme.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgrammeMockMvc.perform(post("/api/programmes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProgrammes() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        // Get all the programmeList
        restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programme.getId().intValue())))
            .andExpect(jsonPath("$.[*].matin").value(hasItem(DEFAULT_MATIN.toString())))
            .andExpect(jsonPath("$.[*].apresmidi").value(hasItem(DEFAULT_APRESMIDI.toString())));
    }

    @Test
    @Transactional
    public void getProgramme() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        // Get the programme
        restProgrammeMockMvc.perform(get("/api/programmes/{id}", programme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(programme.getId().intValue()))
            .andExpect(jsonPath("$.matin").value(DEFAULT_MATIN.toString()))
            .andExpect(jsonPath("$.apresmidi").value(DEFAULT_APRESMIDI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProgramme() throws Exception {
        // Get the programme
        restProgrammeMockMvc.perform(get("/api/programmes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgramme() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);
        programmeSearchRepository.save(programme);
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

        // Update the programme
        Programme updatedProgramme = programmeRepository.findOne(programme.getId());
        updatedProgramme
            .matin(UPDATED_MATIN)
            .apresmidi(UPDATED_APRESMIDI);

        restProgrammeMockMvc.perform(put("/api/programmes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProgramme)))
            .andExpect(status().isOk());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
        Programme testProgramme = programmeList.get(programmeList.size() - 1);
        assertThat(testProgramme.getMatin()).isEqualTo(UPDATED_MATIN);
        assertThat(testProgramme.getApresmidi()).isEqualTo(UPDATED_APRESMIDI);

        // Validate the Programme in Elasticsearch
        Programme programmeEs = programmeSearchRepository.findOne(testProgramme.getId());
        assertThat(programmeEs).isEqualToComparingFieldByField(testProgramme);
    }

    @Test
    @Transactional
    public void updateNonExistingProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

        // Create the Programme

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProgrammeMockMvc.perform(put("/api/programmes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isCreated());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProgramme() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);
        programmeSearchRepository.save(programme);
        int databaseSizeBeforeDelete = programmeRepository.findAll().size();

        // Get the programme
        restProgrammeMockMvc.perform(delete("/api/programmes/{id}", programme.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean programmeExistsInEs = programmeSearchRepository.exists(programme.getId());
        assertThat(programmeExistsInEs).isFalse();

        // Validate the database is empty
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProgramme() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);
        programmeSearchRepository.save(programme);

        // Search the programme
        restProgrammeMockMvc.perform(get("/api/_search/programmes?query=id:" + programme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programme.getId().intValue())))
            .andExpect(jsonPath("$.[*].matin").value(hasItem(DEFAULT_MATIN.toString())))
            .andExpect(jsonPath("$.[*].apresmidi").value(hasItem(DEFAULT_APRESMIDI.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Programme.class);
    }
}
