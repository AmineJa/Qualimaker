package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.EvaluationFormation;
import org.qualimaker.repository.EvaluationFormationRepository;
import org.qualimaker.repository.search.EvaluationFormationSearchRepository;
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
 * Test class for the EvaluationFormationResource REST controller.
 *
 * @see EvaluationFormationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class EvaluationFormationResourceIntTest {

    private static final Double DEFAULT_MATRICUEL = 1D;
    private static final Double UPDATED_MATRICUEL = 2D;

    private static final Float DEFAULT_EFFECTUE = 0F;
    private static final Float UPDATED_EFFECTUE = 1F;

    private static final Float DEFAULT_AFROID = 0F;
    private static final Float UPDATED_AFROID = 1F;

    private static final Float DEFAULT_ACHAUD = 0F;
    private static final Float UPDATED_ACHAUD = 1F;

    private static final String DEFAULT_EVALUERPAR = "AAAAAAAAAA";
    private static final String UPDATED_EVALUERPAR = "BBBBBBBBBB";

    private static final Float DEFAULT_REFAIRE = 0F;
    private static final Float UPDATED_REFAIRE = 1F;

    private static final String DEFAULT_DATEPREVU = "AAAAAAAAAA";
    private static final String UPDATED_DATEPREVU = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EFFICACE = false;
    private static final Boolean UPDATED_EFFICACE = true;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Float DEFAULT_METHODOLOGIE = 0F;
    private static final Float UPDATED_METHODOLOGIE = 1F;

    private static final Float DEFAULT_COMPETECE = 0F;
    private static final Float UPDATED_COMPETECE = 1F;

    private static final Float DEFAULT_SUPPORTCOURS = 0F;
    private static final Float UPDATED_SUPPORTCOURS = 1F;

    private static final Float DEFAULT_ANIMATION = 0F;
    private static final Float UPDATED_ANIMATION = 1F;

    private static final Float DEFAULT_LIEU = 0F;
    private static final Float UPDATED_LIEU = 1F;

    private static final Float DEFAULT_RESPECTHORAIRE = 0F;
    private static final Float UPDATED_RESPECTHORAIRE = 1F;

    private static final Float DEFAULT_CONTNUCOURS = 0F;
    private static final Float UPDATED_CONTNUCOURS = 1F;

    private static final Float DEFAULT_TRAVEAUXP = 0F;
    private static final Float UPDATED_TRAVEAUXP = 1F;

    private static final Float DEFAULT_OBJECTIF = 0F;
    private static final Float UPDATED_OBJECTIF = 1F;

    private static final Float DEFAULT_DUREE = 0F;
    private static final Float UPDATED_DUREE = 1F;

    private static final Float DEFAULT_AMBIACE = 0F;
    private static final Float UPDATED_AMBIACE = 1F;

    @Autowired
    private EvaluationFormationRepository evaluationFormationRepository;

    @Autowired
    private EvaluationFormationSearchRepository evaluationFormationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEvaluationFormationMockMvc;

    private EvaluationFormation evaluationFormation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EvaluationFormationResource evaluationFormationResource = new EvaluationFormationResource(evaluationFormationRepository, evaluationFormationSearchRepository);
        this.restEvaluationFormationMockMvc = MockMvcBuilders.standaloneSetup(evaluationFormationResource)
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
    public static EvaluationFormation createEntity(EntityManager em) {
        EvaluationFormation evaluationFormation = new EvaluationFormation()
            .matricuel(DEFAULT_MATRICUEL)
            .effectue(DEFAULT_EFFECTUE)
            .afroid(DEFAULT_AFROID)
            .achaud(DEFAULT_ACHAUD)
            .evaluerpar(DEFAULT_EVALUERPAR)
            .refaire(DEFAULT_REFAIRE)
            .dateprevu(DEFAULT_DATEPREVU)
            .efficace(DEFAULT_EFFICACE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .methodologie(DEFAULT_METHODOLOGIE)
            .competece(DEFAULT_COMPETECE)
            .supportcours(DEFAULT_SUPPORTCOURS)
            .animation(DEFAULT_ANIMATION)
            .lieu(DEFAULT_LIEU)
            .respecthoraire(DEFAULT_RESPECTHORAIRE)
            .contnucours(DEFAULT_CONTNUCOURS)
            .traveauxp(DEFAULT_TRAVEAUXP)
            .objectif(DEFAULT_OBJECTIF)
            .duree(DEFAULT_DUREE)
            .ambiace(DEFAULT_AMBIACE);
        return evaluationFormation;
    }

    @Before
    public void initTest() {
        evaluationFormationSearchRepository.deleteAll();
        evaluationFormation = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvaluationFormation() throws Exception {
        int databaseSizeBeforeCreate = evaluationFormationRepository.findAll().size();

        // Create the EvaluationFormation
        restEvaluationFormationMockMvc.perform(post("/api/evaluation-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluationFormation)))
            .andExpect(status().isCreated());

        // Validate the EvaluationFormation in the database
        List<EvaluationFormation> evaluationFormationList = evaluationFormationRepository.findAll();
        assertThat(evaluationFormationList).hasSize(databaseSizeBeforeCreate + 1);
        EvaluationFormation testEvaluationFormation = evaluationFormationList.get(evaluationFormationList.size() - 1);
        assertThat(testEvaluationFormation.getMatricuel()).isEqualTo(DEFAULT_MATRICUEL);
        assertThat(testEvaluationFormation.getEffectue()).isEqualTo(DEFAULT_EFFECTUE);
        assertThat(testEvaluationFormation.getAfroid()).isEqualTo(DEFAULT_AFROID);
        assertThat(testEvaluationFormation.getAchaud()).isEqualTo(DEFAULT_ACHAUD);
        assertThat(testEvaluationFormation.getEvaluerpar()).isEqualTo(DEFAULT_EVALUERPAR);
        assertThat(testEvaluationFormation.getRefaire()).isEqualTo(DEFAULT_REFAIRE);
        assertThat(testEvaluationFormation.getDateprevu()).isEqualTo(DEFAULT_DATEPREVU);
        assertThat(testEvaluationFormation.isEfficace()).isEqualTo(DEFAULT_EFFICACE);
        assertThat(testEvaluationFormation.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testEvaluationFormation.getMethodologie()).isEqualTo(DEFAULT_METHODOLOGIE);
        assertThat(testEvaluationFormation.getCompetece()).isEqualTo(DEFAULT_COMPETECE);
        assertThat(testEvaluationFormation.getSupportcours()).isEqualTo(DEFAULT_SUPPORTCOURS);
        assertThat(testEvaluationFormation.getAnimation()).isEqualTo(DEFAULT_ANIMATION);
        assertThat(testEvaluationFormation.getLieu()).isEqualTo(DEFAULT_LIEU);
        assertThat(testEvaluationFormation.getRespecthoraire()).isEqualTo(DEFAULT_RESPECTHORAIRE);
        assertThat(testEvaluationFormation.getContnucours()).isEqualTo(DEFAULT_CONTNUCOURS);
        assertThat(testEvaluationFormation.getTraveauxp()).isEqualTo(DEFAULT_TRAVEAUXP);
        assertThat(testEvaluationFormation.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
        assertThat(testEvaluationFormation.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testEvaluationFormation.getAmbiace()).isEqualTo(DEFAULT_AMBIACE);

        // Validate the EvaluationFormation in Elasticsearch
        EvaluationFormation evaluationFormationEs = evaluationFormationSearchRepository.findOne(testEvaluationFormation.getId());
        assertThat(evaluationFormationEs).isEqualToComparingFieldByField(testEvaluationFormation);
    }

    @Test
    @Transactional
    public void createEvaluationFormationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = evaluationFormationRepository.findAll().size();

        // Create the EvaluationFormation with an existing ID
        evaluationFormation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluationFormationMockMvc.perform(post("/api/evaluation-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluationFormation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EvaluationFormation> evaluationFormationList = evaluationFormationRepository.findAll();
        assertThat(evaluationFormationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEvaluationFormations() throws Exception {
        // Initialize the database
        evaluationFormationRepository.saveAndFlush(evaluationFormation);

        // Get all the evaluationFormationList
        restEvaluationFormationMockMvc.perform(get("/api/evaluation-formations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluationFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricuel").value(hasItem(DEFAULT_MATRICUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].effectue").value(hasItem(DEFAULT_EFFECTUE.doubleValue())))
            .andExpect(jsonPath("$.[*].afroid").value(hasItem(DEFAULT_AFROID.doubleValue())))
            .andExpect(jsonPath("$.[*].achaud").value(hasItem(DEFAULT_ACHAUD.doubleValue())))
            .andExpect(jsonPath("$.[*].evaluerpar").value(hasItem(DEFAULT_EVALUERPAR.toString())))
            .andExpect(jsonPath("$.[*].refaire").value(hasItem(DEFAULT_REFAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateprevu").value(hasItem(DEFAULT_DATEPREVU.toString())))
            .andExpect(jsonPath("$.[*].efficace").value(hasItem(DEFAULT_EFFICACE.booleanValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].methodologie").value(hasItem(DEFAULT_METHODOLOGIE.doubleValue())))
            .andExpect(jsonPath("$.[*].competece").value(hasItem(DEFAULT_COMPETECE.doubleValue())))
            .andExpect(jsonPath("$.[*].supportcours").value(hasItem(DEFAULT_SUPPORTCOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].animation").value(hasItem(DEFAULT_ANIMATION.doubleValue())))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.doubleValue())))
            .andExpect(jsonPath("$.[*].respecthoraire").value(hasItem(DEFAULT_RESPECTHORAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].contnucours").value(hasItem(DEFAULT_CONTNUCOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].traveauxp").value(hasItem(DEFAULT_TRAVEAUXP.doubleValue())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.doubleValue())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE.doubleValue())))
            .andExpect(jsonPath("$.[*].ambiace").value(hasItem(DEFAULT_AMBIACE.doubleValue())));
    }

    @Test
    @Transactional
    public void getEvaluationFormation() throws Exception {
        // Initialize the database
        evaluationFormationRepository.saveAndFlush(evaluationFormation);

        // Get the evaluationFormation
        restEvaluationFormationMockMvc.perform(get("/api/evaluation-formations/{id}", evaluationFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(evaluationFormation.getId().intValue()))
            .andExpect(jsonPath("$.matricuel").value(DEFAULT_MATRICUEL.doubleValue()))
            .andExpect(jsonPath("$.effectue").value(DEFAULT_EFFECTUE.doubleValue()))
            .andExpect(jsonPath("$.afroid").value(DEFAULT_AFROID.doubleValue()))
            .andExpect(jsonPath("$.achaud").value(DEFAULT_ACHAUD.doubleValue()))
            .andExpect(jsonPath("$.evaluerpar").value(DEFAULT_EVALUERPAR.toString()))
            .andExpect(jsonPath("$.refaire").value(DEFAULT_REFAIRE.doubleValue()))
            .andExpect(jsonPath("$.dateprevu").value(DEFAULT_DATEPREVU.toString()))
            .andExpect(jsonPath("$.efficace").value(DEFAULT_EFFICACE.booleanValue()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()))
            .andExpect(jsonPath("$.methodologie").value(DEFAULT_METHODOLOGIE.doubleValue()))
            .andExpect(jsonPath("$.competece").value(DEFAULT_COMPETECE.doubleValue()))
            .andExpect(jsonPath("$.supportcours").value(DEFAULT_SUPPORTCOURS.doubleValue()))
            .andExpect(jsonPath("$.animation").value(DEFAULT_ANIMATION.doubleValue()))
            .andExpect(jsonPath("$.lieu").value(DEFAULT_LIEU.doubleValue()))
            .andExpect(jsonPath("$.respecthoraire").value(DEFAULT_RESPECTHORAIRE.doubleValue()))
            .andExpect(jsonPath("$.contnucours").value(DEFAULT_CONTNUCOURS.doubleValue()))
            .andExpect(jsonPath("$.traveauxp").value(DEFAULT_TRAVEAUXP.doubleValue()))
            .andExpect(jsonPath("$.objectif").value(DEFAULT_OBJECTIF.doubleValue()))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE.doubleValue()))
            .andExpect(jsonPath("$.ambiace").value(DEFAULT_AMBIACE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEvaluationFormation() throws Exception {
        // Get the evaluationFormation
        restEvaluationFormationMockMvc.perform(get("/api/evaluation-formations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvaluationFormation() throws Exception {
        // Initialize the database
        evaluationFormationRepository.saveAndFlush(evaluationFormation);
        evaluationFormationSearchRepository.save(evaluationFormation);
        int databaseSizeBeforeUpdate = evaluationFormationRepository.findAll().size();

        // Update the evaluationFormation
        EvaluationFormation updatedEvaluationFormation = evaluationFormationRepository.findOne(evaluationFormation.getId());
        updatedEvaluationFormation
            .matricuel(UPDATED_MATRICUEL)
            .effectue(UPDATED_EFFECTUE)
            .afroid(UPDATED_AFROID)
            .achaud(UPDATED_ACHAUD)
            .evaluerpar(UPDATED_EVALUERPAR)
            .refaire(UPDATED_REFAIRE)
            .dateprevu(UPDATED_DATEPREVU)
            .efficace(UPDATED_EFFICACE)
            .commentaire(UPDATED_COMMENTAIRE)
            .methodologie(UPDATED_METHODOLOGIE)
            .competece(UPDATED_COMPETECE)
            .supportcours(UPDATED_SUPPORTCOURS)
            .animation(UPDATED_ANIMATION)
            .lieu(UPDATED_LIEU)
            .respecthoraire(UPDATED_RESPECTHORAIRE)
            .contnucours(UPDATED_CONTNUCOURS)
            .traveauxp(UPDATED_TRAVEAUXP)
            .objectif(UPDATED_OBJECTIF)
            .duree(UPDATED_DUREE)
            .ambiace(UPDATED_AMBIACE);

        restEvaluationFormationMockMvc.perform(put("/api/evaluation-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvaluationFormation)))
            .andExpect(status().isOk());

        // Validate the EvaluationFormation in the database
        List<EvaluationFormation> evaluationFormationList = evaluationFormationRepository.findAll();
        assertThat(evaluationFormationList).hasSize(databaseSizeBeforeUpdate);
        EvaluationFormation testEvaluationFormation = evaluationFormationList.get(evaluationFormationList.size() - 1);
        assertThat(testEvaluationFormation.getMatricuel()).isEqualTo(UPDATED_MATRICUEL);
        assertThat(testEvaluationFormation.getEffectue()).isEqualTo(UPDATED_EFFECTUE);
        assertThat(testEvaluationFormation.getAfroid()).isEqualTo(UPDATED_AFROID);
        assertThat(testEvaluationFormation.getAchaud()).isEqualTo(UPDATED_ACHAUD);
        assertThat(testEvaluationFormation.getEvaluerpar()).isEqualTo(UPDATED_EVALUERPAR);
        assertThat(testEvaluationFormation.getRefaire()).isEqualTo(UPDATED_REFAIRE);
        assertThat(testEvaluationFormation.getDateprevu()).isEqualTo(UPDATED_DATEPREVU);
        assertThat(testEvaluationFormation.isEfficace()).isEqualTo(UPDATED_EFFICACE);
        assertThat(testEvaluationFormation.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testEvaluationFormation.getMethodologie()).isEqualTo(UPDATED_METHODOLOGIE);
        assertThat(testEvaluationFormation.getCompetece()).isEqualTo(UPDATED_COMPETECE);
        assertThat(testEvaluationFormation.getSupportcours()).isEqualTo(UPDATED_SUPPORTCOURS);
        assertThat(testEvaluationFormation.getAnimation()).isEqualTo(UPDATED_ANIMATION);
        assertThat(testEvaluationFormation.getLieu()).isEqualTo(UPDATED_LIEU);
        assertThat(testEvaluationFormation.getRespecthoraire()).isEqualTo(UPDATED_RESPECTHORAIRE);
        assertThat(testEvaluationFormation.getContnucours()).isEqualTo(UPDATED_CONTNUCOURS);
        assertThat(testEvaluationFormation.getTraveauxp()).isEqualTo(UPDATED_TRAVEAUXP);
        assertThat(testEvaluationFormation.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
        assertThat(testEvaluationFormation.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testEvaluationFormation.getAmbiace()).isEqualTo(UPDATED_AMBIACE);

        // Validate the EvaluationFormation in Elasticsearch
        EvaluationFormation evaluationFormationEs = evaluationFormationSearchRepository.findOne(testEvaluationFormation.getId());
        assertThat(evaluationFormationEs).isEqualToComparingFieldByField(testEvaluationFormation);
    }

    @Test
    @Transactional
    public void updateNonExistingEvaluationFormation() throws Exception {
        int databaseSizeBeforeUpdate = evaluationFormationRepository.findAll().size();

        // Create the EvaluationFormation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEvaluationFormationMockMvc.perform(put("/api/evaluation-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluationFormation)))
            .andExpect(status().isCreated());

        // Validate the EvaluationFormation in the database
        List<EvaluationFormation> evaluationFormationList = evaluationFormationRepository.findAll();
        assertThat(evaluationFormationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEvaluationFormation() throws Exception {
        // Initialize the database
        evaluationFormationRepository.saveAndFlush(evaluationFormation);
        evaluationFormationSearchRepository.save(evaluationFormation);
        int databaseSizeBeforeDelete = evaluationFormationRepository.findAll().size();

        // Get the evaluationFormation
        restEvaluationFormationMockMvc.perform(delete("/api/evaluation-formations/{id}", evaluationFormation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean evaluationFormationExistsInEs = evaluationFormationSearchRepository.exists(evaluationFormation.getId());
        assertThat(evaluationFormationExistsInEs).isFalse();

        // Validate the database is empty
        List<EvaluationFormation> evaluationFormationList = evaluationFormationRepository.findAll();
        assertThat(evaluationFormationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEvaluationFormation() throws Exception {
        // Initialize the database
        evaluationFormationRepository.saveAndFlush(evaluationFormation);
        evaluationFormationSearchRepository.save(evaluationFormation);

        // Search the evaluationFormation
        restEvaluationFormationMockMvc.perform(get("/api/_search/evaluation-formations?query=id:" + evaluationFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluationFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricuel").value(hasItem(DEFAULT_MATRICUEL.doubleValue())))
            .andExpect(jsonPath("$.[*].effectue").value(hasItem(DEFAULT_EFFECTUE.doubleValue())))
            .andExpect(jsonPath("$.[*].afroid").value(hasItem(DEFAULT_AFROID.doubleValue())))
            .andExpect(jsonPath("$.[*].achaud").value(hasItem(DEFAULT_ACHAUD.doubleValue())))
            .andExpect(jsonPath("$.[*].evaluerpar").value(hasItem(DEFAULT_EVALUERPAR.toString())))
            .andExpect(jsonPath("$.[*].refaire").value(hasItem(DEFAULT_REFAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateprevu").value(hasItem(DEFAULT_DATEPREVU.toString())))
            .andExpect(jsonPath("$.[*].efficace").value(hasItem(DEFAULT_EFFICACE.booleanValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].methodologie").value(hasItem(DEFAULT_METHODOLOGIE.doubleValue())))
            .andExpect(jsonPath("$.[*].competece").value(hasItem(DEFAULT_COMPETECE.doubleValue())))
            .andExpect(jsonPath("$.[*].supportcours").value(hasItem(DEFAULT_SUPPORTCOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].animation").value(hasItem(DEFAULT_ANIMATION.doubleValue())))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.doubleValue())))
            .andExpect(jsonPath("$.[*].respecthoraire").value(hasItem(DEFAULT_RESPECTHORAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].contnucours").value(hasItem(DEFAULT_CONTNUCOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].traveauxp").value(hasItem(DEFAULT_TRAVEAUXP.doubleValue())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.doubleValue())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE.doubleValue())))
            .andExpect(jsonPath("$.[*].ambiace").value(hasItem(DEFAULT_AMBIACE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvaluationFormation.class);
    }
}
