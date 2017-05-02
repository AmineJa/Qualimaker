package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Discipline;
import org.qualimaker.repository.DisciplineRepository;
import org.qualimaker.repository.search.DisciplineSearchRepository;
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
 * Test class for the DisciplineResource REST controller.
 *
 * @see DisciplineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class DisciplineResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_D = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_D = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_F = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_F = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_DUREE = 1;
    private static final Integer UPDATED_DUREE = 2;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_CAUSE = "AAAAAAAAAA";
    private static final String UPDATED_CAUSE = "BBBBBBBBBB";

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private DisciplineSearchRepository disciplineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDisciplineMockMvc;

    private Discipline discipline;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DisciplineResource disciplineResource = new DisciplineResource(disciplineRepository, disciplineSearchRepository);
        this.restDisciplineMockMvc = MockMvcBuilders.standaloneSetup(disciplineResource)
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
    public static Discipline createEntity(EntityManager em) {
        Discipline discipline = new Discipline()
            .dateD(DEFAULT_DATE_D)
            .dateF(DEFAULT_DATE_F)
            .duree(DEFAULT_DUREE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .cause(DEFAULT_CAUSE);
        return discipline;
    }

    @Before
    public void initTest() {
        disciplineSearchRepository.deleteAll();
        discipline = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscipline() throws Exception {
        int databaseSizeBeforeCreate = disciplineRepository.findAll().size();

        // Create the Discipline
        restDisciplineMockMvc.perform(post("/api/disciplines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discipline)))
            .andExpect(status().isCreated());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeCreate + 1);
        Discipline testDiscipline = disciplineList.get(disciplineList.size() - 1);
        assertThat(testDiscipline.getDateD()).isEqualTo(DEFAULT_DATE_D);
        assertThat(testDiscipline.getDateF()).isEqualTo(DEFAULT_DATE_F);
        assertThat(testDiscipline.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testDiscipline.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testDiscipline.getCause()).isEqualTo(DEFAULT_CAUSE);

        // Validate the Discipline in Elasticsearch
        Discipline disciplineEs = disciplineSearchRepository.findOne(testDiscipline.getId());
        assertThat(disciplineEs).isEqualToComparingFieldByField(testDiscipline);
    }

    @Test
    @Transactional
    public void createDisciplineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = disciplineRepository.findAll().size();

        // Create the Discipline with an existing ID
        discipline.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisciplineMockMvc.perform(post("/api/disciplines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discipline)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDisciplines() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);

        // Get all the disciplineList
        restDisciplineMockMvc.perform(get("/api/disciplines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discipline.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateD").value(hasItem(sameInstant(DEFAULT_DATE_D))))
            .andExpect(jsonPath("$.[*].dateF").value(hasItem(sameInstant(DEFAULT_DATE_F))))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].cause").value(hasItem(DEFAULT_CAUSE.toString())));
    }

    @Test
    @Transactional
    public void getDiscipline() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);

        // Get the discipline
        restDisciplineMockMvc.perform(get("/api/disciplines/{id}", discipline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discipline.getId().intValue()))
            .andExpect(jsonPath("$.dateD").value(sameInstant(DEFAULT_DATE_D)))
            .andExpect(jsonPath("$.dateF").value(sameInstant(DEFAULT_DATE_F)))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()))
            .andExpect(jsonPath("$.cause").value(DEFAULT_CAUSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiscipline() throws Exception {
        // Get the discipline
        restDisciplineMockMvc.perform(get("/api/disciplines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscipline() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);
        disciplineSearchRepository.save(discipline);
        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();

        // Update the discipline
        Discipline updatedDiscipline = disciplineRepository.findOne(discipline.getId());
        updatedDiscipline
            .dateD(UPDATED_DATE_D)
            .dateF(UPDATED_DATE_F)
            .duree(UPDATED_DUREE)
            .commentaire(UPDATED_COMMENTAIRE)
            .cause(UPDATED_CAUSE);

        restDisciplineMockMvc.perform(put("/api/disciplines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscipline)))
            .andExpect(status().isOk());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
        Discipline testDiscipline = disciplineList.get(disciplineList.size() - 1);
        assertThat(testDiscipline.getDateD()).isEqualTo(UPDATED_DATE_D);
        assertThat(testDiscipline.getDateF()).isEqualTo(UPDATED_DATE_F);
        assertThat(testDiscipline.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testDiscipline.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testDiscipline.getCause()).isEqualTo(UPDATED_CAUSE);

        // Validate the Discipline in Elasticsearch
        Discipline disciplineEs = disciplineSearchRepository.findOne(testDiscipline.getId());
        assertThat(disciplineEs).isEqualToComparingFieldByField(testDiscipline);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscipline() throws Exception {
        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();

        // Create the Discipline

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDisciplineMockMvc.perform(put("/api/disciplines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discipline)))
            .andExpect(status().isCreated());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDiscipline() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);
        disciplineSearchRepository.save(discipline);
        int databaseSizeBeforeDelete = disciplineRepository.findAll().size();

        // Get the discipline
        restDisciplineMockMvc.perform(delete("/api/disciplines/{id}", discipline.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean disciplineExistsInEs = disciplineSearchRepository.exists(discipline.getId());
        assertThat(disciplineExistsInEs).isFalse();

        // Validate the database is empty
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDiscipline() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);
        disciplineSearchRepository.save(discipline);

        // Search the discipline
        restDisciplineMockMvc.perform(get("/api/_search/disciplines?query=id:" + discipline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discipline.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateD").value(hasItem(sameInstant(DEFAULT_DATE_D))))
            .andExpect(jsonPath("$.[*].dateF").value(hasItem(sameInstant(DEFAULT_DATE_F))))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].cause").value(hasItem(DEFAULT_CAUSE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Discipline.class);
    }
}
