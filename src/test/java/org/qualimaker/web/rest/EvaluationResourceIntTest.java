package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Evaluation;
import org.qualimaker.repository.EvaluationRepository;
import org.qualimaker.repository.search.EvaluationSearchRepository;
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
 * Test class for the EvaluationResource REST controller.
 *
 * @see EvaluationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class EvaluationResourceIntTest {

    private static final ZonedDateTime DEFAULT_ANNE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ANNE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_VALEURMAX = 1D;
    private static final Double UPDATED_VALEURMAX = 2D;

    private static final String DEFAULT_SEUIL_POL = "AAAAAAAAAA";
    private static final String UPDATED_SEUIL_POL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_COMPETENCE = "AAAAAAAAAA";
    private static final String UPDATED_COMPETENCE = "BBBBBBBBBB";

    private static final String DEFAULT_POLYVALENCE = "AAAAAAAAAA";
    private static final String UPDATED_POLYVALENCE = "BBBBBBBBBB";

    private static final String DEFAULT_CRITERE = "AAAAAAAAAA";
    private static final String UPDATED_CRITERE = "BBBBBBBBBB";

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EvaluationSearchRepository evaluationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEvaluationMockMvc;

    private Evaluation evaluation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EvaluationResource evaluationResource = new EvaluationResource(evaluationRepository, evaluationSearchRepository);
        this.restEvaluationMockMvc = MockMvcBuilders.standaloneSetup(evaluationResource)
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
    public static Evaluation createEntity(EntityManager em) {
        Evaluation evaluation = new Evaluation()
            .anne(DEFAULT_ANNE)
            .description(DEFAULT_DESCRIPTION)
            .valeurmax(DEFAULT_VALEURMAX)
            .seuilPol(DEFAULT_SEUIL_POL)
            .date(DEFAULT_DATE)
            .competence(DEFAULT_COMPETENCE)
            .polyvalence(DEFAULT_POLYVALENCE)
            .critere(DEFAULT_CRITERE);
        return evaluation;
    }

    @Before
    public void initTest() {
        evaluationSearchRepository.deleteAll();
        evaluation = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvaluation() throws Exception {
        int databaseSizeBeforeCreate = evaluationRepository.findAll().size();

        // Create the Evaluation
        restEvaluationMockMvc.perform(post("/api/evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isCreated());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeCreate + 1);
        Evaluation testEvaluation = evaluationList.get(evaluationList.size() - 1);
        assertThat(testEvaluation.getAnne()).isEqualTo(DEFAULT_ANNE);
        assertThat(testEvaluation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvaluation.getValeurmax()).isEqualTo(DEFAULT_VALEURMAX);
        assertThat(testEvaluation.getSeuilPol()).isEqualTo(DEFAULT_SEUIL_POL);
        assertThat(testEvaluation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEvaluation.getCompetence()).isEqualTo(DEFAULT_COMPETENCE);
        assertThat(testEvaluation.getPolyvalence()).isEqualTo(DEFAULT_POLYVALENCE);
        assertThat(testEvaluation.getCritere()).isEqualTo(DEFAULT_CRITERE);

        // Validate the Evaluation in Elasticsearch
        Evaluation evaluationEs = evaluationSearchRepository.findOne(testEvaluation.getId());
        assertThat(evaluationEs).isEqualToComparingFieldByField(testEvaluation);
    }

    @Test
    @Transactional
    public void createEvaluationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = evaluationRepository.findAll().size();

        // Create the Evaluation with an existing ID
        evaluation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluationMockMvc.perform(post("/api/evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEvaluations() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList
        restEvaluationMockMvc.perform(get("/api/evaluations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].anne").value(hasItem(sameInstant(DEFAULT_ANNE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].valeurmax").value(hasItem(DEFAULT_VALEURMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].seuilPol").value(hasItem(DEFAULT_SEUIL_POL.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].competence").value(hasItem(DEFAULT_COMPETENCE.toString())))
            .andExpect(jsonPath("$.[*].polyvalence").value(hasItem(DEFAULT_POLYVALENCE.toString())))
            .andExpect(jsonPath("$.[*].critere").value(hasItem(DEFAULT_CRITERE.toString())));
    }

    @Test
    @Transactional
    public void getEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        // Get the evaluation
        restEvaluationMockMvc.perform(get("/api/evaluations/{id}", evaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(evaluation.getId().intValue()))
            .andExpect(jsonPath("$.anne").value(sameInstant(DEFAULT_ANNE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.valeurmax").value(DEFAULT_VALEURMAX.doubleValue()))
            .andExpect(jsonPath("$.seuilPol").value(DEFAULT_SEUIL_POL.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.competence").value(DEFAULT_COMPETENCE.toString()))
            .andExpect(jsonPath("$.polyvalence").value(DEFAULT_POLYVALENCE.toString()))
            .andExpect(jsonPath("$.critere").value(DEFAULT_CRITERE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvaluation() throws Exception {
        // Get the evaluation
        restEvaluationMockMvc.perform(get("/api/evaluations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);
        evaluationSearchRepository.save(evaluation);
        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();

        // Update the evaluation
        Evaluation updatedEvaluation = evaluationRepository.findOne(evaluation.getId());
        updatedEvaluation
            .anne(UPDATED_ANNE)
            .description(UPDATED_DESCRIPTION)
            .valeurmax(UPDATED_VALEURMAX)
            .seuilPol(UPDATED_SEUIL_POL)
            .date(UPDATED_DATE)
            .competence(UPDATED_COMPETENCE)
            .polyvalence(UPDATED_POLYVALENCE)
            .critere(UPDATED_CRITERE);

        restEvaluationMockMvc.perform(put("/api/evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvaluation)))
            .andExpect(status().isOk());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
        Evaluation testEvaluation = evaluationList.get(evaluationList.size() - 1);
        assertThat(testEvaluation.getAnne()).isEqualTo(UPDATED_ANNE);
        assertThat(testEvaluation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvaluation.getValeurmax()).isEqualTo(UPDATED_VALEURMAX);
        assertThat(testEvaluation.getSeuilPol()).isEqualTo(UPDATED_SEUIL_POL);
        assertThat(testEvaluation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEvaluation.getCompetence()).isEqualTo(UPDATED_COMPETENCE);
        assertThat(testEvaluation.getPolyvalence()).isEqualTo(UPDATED_POLYVALENCE);
        assertThat(testEvaluation.getCritere()).isEqualTo(UPDATED_CRITERE);

        // Validate the Evaluation in Elasticsearch
        Evaluation evaluationEs = evaluationSearchRepository.findOne(testEvaluation.getId());
        assertThat(evaluationEs).isEqualToComparingFieldByField(testEvaluation);
    }

    @Test
    @Transactional
    public void updateNonExistingEvaluation() throws Exception {
        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();

        // Create the Evaluation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEvaluationMockMvc.perform(put("/api/evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isCreated());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);
        evaluationSearchRepository.save(evaluation);
        int databaseSizeBeforeDelete = evaluationRepository.findAll().size();

        // Get the evaluation
        restEvaluationMockMvc.perform(delete("/api/evaluations/{id}", evaluation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean evaluationExistsInEs = evaluationSearchRepository.exists(evaluation.getId());
        assertThat(evaluationExistsInEs).isFalse();

        // Validate the database is empty
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);
        evaluationSearchRepository.save(evaluation);

        // Search the evaluation
        restEvaluationMockMvc.perform(get("/api/_search/evaluations?query=id:" + evaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].anne").value(hasItem(sameInstant(DEFAULT_ANNE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].valeurmax").value(hasItem(DEFAULT_VALEURMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].seuilPol").value(hasItem(DEFAULT_SEUIL_POL.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].competence").value(hasItem(DEFAULT_COMPETENCE.toString())))
            .andExpect(jsonPath("$.[*].polyvalence").value(hasItem(DEFAULT_POLYVALENCE.toString())))
            .andExpect(jsonPath("$.[*].critere").value(hasItem(DEFAULT_CRITERE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evaluation.class);
    }
}
