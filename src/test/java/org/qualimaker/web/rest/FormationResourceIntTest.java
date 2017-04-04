package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Formation;
import org.qualimaker.repository.FormationRepository;
import org.qualimaker.repository.search.FormationSearchRepository;
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
 * Test class for the FormationResource REST controller.
 *
 * @see FormationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class FormationResourceIntTest {

    private static final String DEFAULT_THEME = "AAAAAAAAAA";
    private static final String UPDATED_THEME = "BBBBBBBBBB";

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_D = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_D = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_F = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_F = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LIEU = "AAAAAAAAAA";
    private static final String UPDATED_LIEU = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIF = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIF = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final String DEFAULT_COUT_MAT = "AAAAAAAAAA";
    private static final String UPDATED_COUT_MAT = "BBBBBBBBBB";

    private static final String DEFAULT_COUT_IMAT = "AAAAAAAAAA";
    private static final String UPDATED_COUT_IMAT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RECCURENCE = false;
    private static final Boolean UPDATED_RECCURENCE = true;

    private static final String DEFAULT_PERIODE = "AAAAAAAAAA";
    private static final String UPDATED_PERIODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FINAPRS = false;
    private static final Boolean UPDATED_FINAPRS = true;

    private static final Boolean DEFAULT_TERMINA = false;
    private static final Boolean UPDATED_TERMINA = true;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationSearchRepository formationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFormationMockMvc;

    private Formation formation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FormationResource formationResource = new FormationResource(formationRepository, formationSearchRepository);
        this.restFormationMockMvc = MockMvcBuilders.standaloneSetup(formationResource)
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
    public static Formation createEntity(EntityManager em) {
        Formation formation = new Formation()
            .theme(DEFAULT_THEME)
            .titre(DEFAULT_TITRE)
            .dateD(DEFAULT_DATE_D)
            .dateF(DEFAULT_DATE_F)
            .lieu(DEFAULT_LIEU)
            .objectif(DEFAULT_OBJECTIF)
            .contenu(DEFAULT_CONTENU)
            .coutMat(DEFAULT_COUT_MAT)
            .coutImat(DEFAULT_COUT_IMAT)
            .reccurence(DEFAULT_RECCURENCE)
            .periode(DEFAULT_PERIODE)
            .finaprs(DEFAULT_FINAPRS)
            .termina(DEFAULT_TERMINA);
        return formation;
    }

    @Before
    public void initTest() {
        formationSearchRepository.deleteAll();
        formation = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormation() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // Create the Formation
        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate + 1);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getTheme()).isEqualTo(DEFAULT_THEME);
        assertThat(testFormation.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testFormation.getDateD()).isEqualTo(DEFAULT_DATE_D);
        assertThat(testFormation.getDateF()).isEqualTo(DEFAULT_DATE_F);
        assertThat(testFormation.getLieu()).isEqualTo(DEFAULT_LIEU);
        assertThat(testFormation.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
        assertThat(testFormation.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testFormation.getCoutMat()).isEqualTo(DEFAULT_COUT_MAT);
        assertThat(testFormation.getCoutImat()).isEqualTo(DEFAULT_COUT_IMAT);
        assertThat(testFormation.isReccurence()).isEqualTo(DEFAULT_RECCURENCE);
        assertThat(testFormation.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testFormation.isFinaprs()).isEqualTo(DEFAULT_FINAPRS);
        assertThat(testFormation.isTermina()).isEqualTo(DEFAULT_TERMINA);

        // Validate the Formation in Elasticsearch
        Formation formationEs = formationSearchRepository.findOne(testFormation.getId());
        assertThat(formationEs).isEqualToComparingFieldByField(testFormation);
    }

    @Test
    @Transactional
    public void createFormationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // Create the Formation with an existing ID
        formation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkThemeIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setTheme(null);

        // Create the Formation, which fails.

        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setTitre(null);

        // Create the Formation, which fails.

        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setDateD(null);

        // Create the Formation, which fails.

        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setDateF(null);

        // Create the Formation, which fails.

        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFormations() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList
        restFormationMockMvc.perform(get("/api/formations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME.toString())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].dateD").value(hasItem(sameInstant(DEFAULT_DATE_D))))
            .andExpect(jsonPath("$.[*].dateF").value(hasItem(sameInstant(DEFAULT_DATE_F))))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.toString())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU.toString())))
            .andExpect(jsonPath("$.[*].coutMat").value(hasItem(DEFAULT_COUT_MAT.toString())))
            .andExpect(jsonPath("$.[*].coutImat").value(hasItem(DEFAULT_COUT_IMAT.toString())))
            .andExpect(jsonPath("$.[*].reccurence").value(hasItem(DEFAULT_RECCURENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
            .andExpect(jsonPath("$.[*].finaprs").value(hasItem(DEFAULT_FINAPRS.booleanValue())))
            .andExpect(jsonPath("$.[*].termina").value(hasItem(DEFAULT_TERMINA.booleanValue())));
    }

    @Test
    @Transactional
    public void getFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get the formation
        restFormationMockMvc.perform(get("/api/formations/{id}", formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formation.getId().intValue()))
            .andExpect(jsonPath("$.theme").value(DEFAULT_THEME.toString()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.dateD").value(sameInstant(DEFAULT_DATE_D)))
            .andExpect(jsonPath("$.dateF").value(sameInstant(DEFAULT_DATE_F)))
            .andExpect(jsonPath("$.lieu").value(DEFAULT_LIEU.toString()))
            .andExpect(jsonPath("$.objectif").value(DEFAULT_OBJECTIF.toString()))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU.toString()))
            .andExpect(jsonPath("$.coutMat").value(DEFAULT_COUT_MAT.toString()))
            .andExpect(jsonPath("$.coutImat").value(DEFAULT_COUT_IMAT.toString()))
            .andExpect(jsonPath("$.reccurence").value(DEFAULT_RECCURENCE.booleanValue()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE.toString()))
            .andExpect(jsonPath("$.finaprs").value(DEFAULT_FINAPRS.booleanValue()))
            .andExpect(jsonPath("$.termina").value(DEFAULT_TERMINA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFormation() throws Exception {
        // Get the formation
        restFormationMockMvc.perform(get("/api/formations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);
        formationSearchRepository.save(formation);
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation
        Formation updatedFormation = formationRepository.findOne(formation.getId());
        updatedFormation
            .theme(UPDATED_THEME)
            .titre(UPDATED_TITRE)
            .dateD(UPDATED_DATE_D)
            .dateF(UPDATED_DATE_F)
            .lieu(UPDATED_LIEU)
            .objectif(UPDATED_OBJECTIF)
            .contenu(UPDATED_CONTENU)
            .coutMat(UPDATED_COUT_MAT)
            .coutImat(UPDATED_COUT_IMAT)
            .reccurence(UPDATED_RECCURENCE)
            .periode(UPDATED_PERIODE)
            .finaprs(UPDATED_FINAPRS)
            .termina(UPDATED_TERMINA);

        restFormationMockMvc.perform(put("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormation)))
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testFormation.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testFormation.getDateD()).isEqualTo(UPDATED_DATE_D);
        assertThat(testFormation.getDateF()).isEqualTo(UPDATED_DATE_F);
        assertThat(testFormation.getLieu()).isEqualTo(UPDATED_LIEU);
        assertThat(testFormation.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
        assertThat(testFormation.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testFormation.getCoutMat()).isEqualTo(UPDATED_COUT_MAT);
        assertThat(testFormation.getCoutImat()).isEqualTo(UPDATED_COUT_IMAT);
        assertThat(testFormation.isReccurence()).isEqualTo(UPDATED_RECCURENCE);
        assertThat(testFormation.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testFormation.isFinaprs()).isEqualTo(UPDATED_FINAPRS);
        assertThat(testFormation.isTermina()).isEqualTo(UPDATED_TERMINA);

        // Validate the Formation in Elasticsearch
        Formation formationEs = formationSearchRepository.findOne(testFormation.getId());
        assertThat(formationEs).isEqualToComparingFieldByField(testFormation);
    }

    @Test
    @Transactional
    public void updateNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Create the Formation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFormationMockMvc.perform(put("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);
        formationSearchRepository.save(formation);
        int databaseSizeBeforeDelete = formationRepository.findAll().size();

        // Get the formation
        restFormationMockMvc.perform(delete("/api/formations/{id}", formation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean formationExistsInEs = formationSearchRepository.exists(formation.getId());
        assertThat(formationExistsInEs).isFalse();

        // Validate the database is empty
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);
        formationSearchRepository.save(formation);

        // Search the formation
        restFormationMockMvc.perform(get("/api/_search/formations?query=id:" + formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME.toString())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].dateD").value(hasItem(sameInstant(DEFAULT_DATE_D))))
            .andExpect(jsonPath("$.[*].dateF").value(hasItem(sameInstant(DEFAULT_DATE_F))))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.toString())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU.toString())))
            .andExpect(jsonPath("$.[*].coutMat").value(hasItem(DEFAULT_COUT_MAT.toString())))
            .andExpect(jsonPath("$.[*].coutImat").value(hasItem(DEFAULT_COUT_IMAT.toString())))
            .andExpect(jsonPath("$.[*].reccurence").value(hasItem(DEFAULT_RECCURENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
            .andExpect(jsonPath("$.[*].finaprs").value(hasItem(DEFAULT_FINAPRS.booleanValue())))
            .andExpect(jsonPath("$.[*].termina").value(hasItem(DEFAULT_TERMINA.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Formation.class);
    }
}
