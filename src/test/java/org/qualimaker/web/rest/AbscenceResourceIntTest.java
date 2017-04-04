package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Abscence;
import org.qualimaker.repository.AbscenceRepository;
import org.qualimaker.repository.search.AbscenceSearchRepository;
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
 * Test class for the AbscenceResource REST controller.
 *
 * @see AbscenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class AbscenceResourceIntTest {

    private static final String DEFAULT_JUSTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFIER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_D = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_D = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_F = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_F = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_INTEGRE = "AAAAAAAAAA";
    private static final String UPDATED_INTEGRE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    @Autowired
    private AbscenceRepository abscenceRepository;

    @Autowired
    private AbscenceSearchRepository abscenceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAbscenceMockMvc;

    private Abscence abscence;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AbscenceResource abscenceResource = new AbscenceResource(abscenceRepository, abscenceSearchRepository);
        this.restAbscenceMockMvc = MockMvcBuilders.standaloneSetup(abscenceResource)
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
    public static Abscence createEntity(EntityManager em) {
        Abscence abscence = new Abscence()
            .justifier(DEFAULT_JUSTIFIER)
            .dateD(DEFAULT_DATE_D)
            .dateF(DEFAULT_DATE_F)
            .etat(DEFAULT_ETAT)
            .integre(DEFAULT_INTEGRE)
            .commentaire(DEFAULT_COMMENTAIRE);
        return abscence;
    }

    @Before
    public void initTest() {
        abscenceSearchRepository.deleteAll();
        abscence = createEntity(em);
    }

    @Test
    @Transactional
    public void createAbscence() throws Exception {
        int databaseSizeBeforeCreate = abscenceRepository.findAll().size();

        // Create the Abscence
        restAbscenceMockMvc.perform(post("/api/abscences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abscence)))
            .andExpect(status().isCreated());

        // Validate the Abscence in the database
        List<Abscence> abscenceList = abscenceRepository.findAll();
        assertThat(abscenceList).hasSize(databaseSizeBeforeCreate + 1);
        Abscence testAbscence = abscenceList.get(abscenceList.size() - 1);
        assertThat(testAbscence.getJustifier()).isEqualTo(DEFAULT_JUSTIFIER);
        assertThat(testAbscence.getDateD()).isEqualTo(DEFAULT_DATE_D);
        assertThat(testAbscence.getDateF()).isEqualTo(DEFAULT_DATE_F);
        assertThat(testAbscence.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testAbscence.getIntegre()).isEqualTo(DEFAULT_INTEGRE);
        assertThat(testAbscence.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);

        // Validate the Abscence in Elasticsearch
        Abscence abscenceEs = abscenceSearchRepository.findOne(testAbscence.getId());
        assertThat(abscenceEs).isEqualToComparingFieldByField(testAbscence);
    }

    @Test
    @Transactional
    public void createAbscenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = abscenceRepository.findAll().size();

        // Create the Abscence with an existing ID
        abscence.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbscenceMockMvc.perform(post("/api/abscences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abscence)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Abscence> abscenceList = abscenceRepository.findAll();
        assertThat(abscenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAbscences() throws Exception {
        // Initialize the database
        abscenceRepository.saveAndFlush(abscence);

        // Get all the abscenceList
        restAbscenceMockMvc.perform(get("/api/abscences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abscence.getId().intValue())))
            .andExpect(jsonPath("$.[*].justifier").value(hasItem(DEFAULT_JUSTIFIER.toString())))
            .andExpect(jsonPath("$.[*].dateD").value(hasItem(sameInstant(DEFAULT_DATE_D))))
            .andExpect(jsonPath("$.[*].dateF").value(hasItem(sameInstant(DEFAULT_DATE_F))))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].integre").value(hasItem(DEFAULT_INTEGRE.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }

    @Test
    @Transactional
    public void getAbscence() throws Exception {
        // Initialize the database
        abscenceRepository.saveAndFlush(abscence);

        // Get the abscence
        restAbscenceMockMvc.perform(get("/api/abscences/{id}", abscence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(abscence.getId().intValue()))
            .andExpect(jsonPath("$.justifier").value(DEFAULT_JUSTIFIER.toString()))
            .andExpect(jsonPath("$.dateD").value(sameInstant(DEFAULT_DATE_D)))
            .andExpect(jsonPath("$.dateF").value(sameInstant(DEFAULT_DATE_F)))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.integre").value(DEFAULT_INTEGRE.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAbscence() throws Exception {
        // Get the abscence
        restAbscenceMockMvc.perform(get("/api/abscences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbscence() throws Exception {
        // Initialize the database
        abscenceRepository.saveAndFlush(abscence);
        abscenceSearchRepository.save(abscence);
        int databaseSizeBeforeUpdate = abscenceRepository.findAll().size();

        // Update the abscence
        Abscence updatedAbscence = abscenceRepository.findOne(abscence.getId());
        updatedAbscence
            .justifier(UPDATED_JUSTIFIER)
            .dateD(UPDATED_DATE_D)
            .dateF(UPDATED_DATE_F)
            .etat(UPDATED_ETAT)
            .integre(UPDATED_INTEGRE)
            .commentaire(UPDATED_COMMENTAIRE);

        restAbscenceMockMvc.perform(put("/api/abscences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAbscence)))
            .andExpect(status().isOk());

        // Validate the Abscence in the database
        List<Abscence> abscenceList = abscenceRepository.findAll();
        assertThat(abscenceList).hasSize(databaseSizeBeforeUpdate);
        Abscence testAbscence = abscenceList.get(abscenceList.size() - 1);
        assertThat(testAbscence.getJustifier()).isEqualTo(UPDATED_JUSTIFIER);
        assertThat(testAbscence.getDateD()).isEqualTo(UPDATED_DATE_D);
        assertThat(testAbscence.getDateF()).isEqualTo(UPDATED_DATE_F);
        assertThat(testAbscence.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testAbscence.getIntegre()).isEqualTo(UPDATED_INTEGRE);
        assertThat(testAbscence.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);

        // Validate the Abscence in Elasticsearch
        Abscence abscenceEs = abscenceSearchRepository.findOne(testAbscence.getId());
        assertThat(abscenceEs).isEqualToComparingFieldByField(testAbscence);
    }

    @Test
    @Transactional
    public void updateNonExistingAbscence() throws Exception {
        int databaseSizeBeforeUpdate = abscenceRepository.findAll().size();

        // Create the Abscence

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAbscenceMockMvc.perform(put("/api/abscences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abscence)))
            .andExpect(status().isCreated());

        // Validate the Abscence in the database
        List<Abscence> abscenceList = abscenceRepository.findAll();
        assertThat(abscenceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAbscence() throws Exception {
        // Initialize the database
        abscenceRepository.saveAndFlush(abscence);
        abscenceSearchRepository.save(abscence);
        int databaseSizeBeforeDelete = abscenceRepository.findAll().size();

        // Get the abscence
        restAbscenceMockMvc.perform(delete("/api/abscences/{id}", abscence.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean abscenceExistsInEs = abscenceSearchRepository.exists(abscence.getId());
        assertThat(abscenceExistsInEs).isFalse();

        // Validate the database is empty
        List<Abscence> abscenceList = abscenceRepository.findAll();
        assertThat(abscenceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAbscence() throws Exception {
        // Initialize the database
        abscenceRepository.saveAndFlush(abscence);
        abscenceSearchRepository.save(abscence);

        // Search the abscence
        restAbscenceMockMvc.perform(get("/api/_search/abscences?query=id:" + abscence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abscence.getId().intValue())))
            .andExpect(jsonPath("$.[*].justifier").value(hasItem(DEFAULT_JUSTIFIER.toString())))
            .andExpect(jsonPath("$.[*].dateD").value(hasItem(sameInstant(DEFAULT_DATE_D))))
            .andExpect(jsonPath("$.[*].dateF").value(hasItem(sameInstant(DEFAULT_DATE_F))))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].integre").value(hasItem(DEFAULT_INTEGRE.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Abscence.class);
    }
}
