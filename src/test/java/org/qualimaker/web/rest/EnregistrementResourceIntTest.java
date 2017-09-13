package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Enregistrement;
import org.qualimaker.repository.EnregistrementRepository;
import org.qualimaker.repository.search.EnregistrementSearchRepository;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the EnregistrementResource REST controller.
 *
 * @see EnregistrementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class EnregistrementResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FICHIER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FICHIER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_MOTCLEF = "AAAAAAAAAA";
    private static final String UPDATED_MOTCLEF = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_EMP = "AAAAAAAAAA";
    private static final String UPDATED_EMP = "BBBBBBBBBB";

    @Autowired
    private EnregistrementRepository enregistrementRepository;

    @Autowired
    private EnregistrementSearchRepository enregistrementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEnregistrementMockMvc;

    private Enregistrement enregistrement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnregistrementResource enregistrementResource = new EnregistrementResource(enregistrementRepository, enregistrementSearchRepository);
        this.restEnregistrementMockMvc = MockMvcBuilders.standaloneSetup(enregistrementResource)
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
    public static Enregistrement createEntity(EntityManager em) {
        Enregistrement enregistrement = new Enregistrement()
            .libelle(DEFAULT_LIBELLE)
            .titre(DEFAULT_TITRE)
            .fichier(DEFAULT_FICHIER)
            .fichierContentType(DEFAULT_FICHIER_CONTENT_TYPE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .motclef(DEFAULT_MOTCLEF)
            .date(DEFAULT_DATE)
            .emp(DEFAULT_EMP);
        return enregistrement;
    }

    @Before
    public void initTest() {
        enregistrementSearchRepository.deleteAll();
        enregistrement = createEntity(em);
    }

    @Test
    @Transactional
    public void createEnregistrement() throws Exception {
        int databaseSizeBeforeCreate = enregistrementRepository.findAll().size();

        // Create the Enregistrement
        restEnregistrementMockMvc.perform(post("/api/enregistrements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enregistrement)))
            .andExpect(status().isCreated());

        // Validate the Enregistrement in the database
        List<Enregistrement> enregistrementList = enregistrementRepository.findAll();
        assertThat(enregistrementList).hasSize(databaseSizeBeforeCreate + 1);
        Enregistrement testEnregistrement = enregistrementList.get(enregistrementList.size() - 1);
        assertThat(testEnregistrement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testEnregistrement.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testEnregistrement.getFichier()).isEqualTo(DEFAULT_FICHIER);
        assertThat(testEnregistrement.getFichierContentType()).isEqualTo(DEFAULT_FICHIER_CONTENT_TYPE);
        assertThat(testEnregistrement.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testEnregistrement.getMotclef()).isEqualTo(DEFAULT_MOTCLEF);
        assertThat(testEnregistrement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEnregistrement.getEmp()).isEqualTo(DEFAULT_EMP);

        // Validate the Enregistrement in Elasticsearch
        Enregistrement enregistrementEs = enregistrementSearchRepository.findOne(testEnregistrement.getId());
        assertThat(enregistrementEs).isEqualToComparingFieldByField(testEnregistrement);
    }

    @Test
    @Transactional
    public void createEnregistrementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = enregistrementRepository.findAll().size();

        // Create the Enregistrement with an existing ID
        enregistrement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnregistrementMockMvc.perform(post("/api/enregistrements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enregistrement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Enregistrement> enregistrementList = enregistrementRepository.findAll();
        assertThat(enregistrementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = enregistrementRepository.findAll().size();
        // set the field null
        enregistrement.setLibelle(null);

        // Create the Enregistrement, which fails.

        restEnregistrementMockMvc.perform(post("/api/enregistrements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enregistrement)))
            .andExpect(status().isBadRequest());

        List<Enregistrement> enregistrementList = enregistrementRepository.findAll();
        assertThat(enregistrementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = enregistrementRepository.findAll().size();
        // set the field null
        enregistrement.setTitre(null);

        // Create the Enregistrement, which fails.

        restEnregistrementMockMvc.perform(post("/api/enregistrements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enregistrement)))
            .andExpect(status().isBadRequest());

        List<Enregistrement> enregistrementList = enregistrementRepository.findAll();
        assertThat(enregistrementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFichierIsRequired() throws Exception {
        int databaseSizeBeforeTest = enregistrementRepository.findAll().size();
        // set the field null
        enregistrement.setFichier(null);

        // Create the Enregistrement, which fails.

        restEnregistrementMockMvc.perform(post("/api/enregistrements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enregistrement)))
            .andExpect(status().isBadRequest());

        List<Enregistrement> enregistrementList = enregistrementRepository.findAll();
        assertThat(enregistrementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnregistrements() throws Exception {
        // Initialize the database
        enregistrementRepository.saveAndFlush(enregistrement);

        // Get all the enregistrementList
        restEnregistrementMockMvc.perform(get("/api/enregistrements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enregistrement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].motclef").value(hasItem(DEFAULT_MOTCLEF.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].emp").value(hasItem(DEFAULT_EMP.toString())));
    }

    @Test
    @Transactional
    public void getEnregistrement() throws Exception {
        // Initialize the database
        enregistrementRepository.saveAndFlush(enregistrement);

        // Get the enregistrement
        restEnregistrementMockMvc.perform(get("/api/enregistrements/{id}", enregistrement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(enregistrement.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.fichierContentType").value(DEFAULT_FICHIER_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichier").value(Base64Utils.encodeToString(DEFAULT_FICHIER)))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()))
            .andExpect(jsonPath("$.motclef").value(DEFAULT_MOTCLEF.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.emp").value(DEFAULT_EMP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnregistrement() throws Exception {
        // Get the enregistrement
        restEnregistrementMockMvc.perform(get("/api/enregistrements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnregistrement() throws Exception {
        // Initialize the database
        enregistrementRepository.saveAndFlush(enregistrement);
        enregistrementSearchRepository.save(enregistrement);
        int databaseSizeBeforeUpdate = enregistrementRepository.findAll().size();

        // Update the enregistrement
        Enregistrement updatedEnregistrement = enregistrementRepository.findOne(enregistrement.getId());
        updatedEnregistrement
            .libelle(UPDATED_LIBELLE)
            .titre(UPDATED_TITRE)
            .fichier(UPDATED_FICHIER)
            .fichierContentType(UPDATED_FICHIER_CONTENT_TYPE)
            .commentaire(UPDATED_COMMENTAIRE)
            .motclef(UPDATED_MOTCLEF)
            .date(UPDATED_DATE)
            .emp(UPDATED_EMP);

        restEnregistrementMockMvc.perform(put("/api/enregistrements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEnregistrement)))
            .andExpect(status().isOk());

        // Validate the Enregistrement in the database
        List<Enregistrement> enregistrementList = enregistrementRepository.findAll();
        assertThat(enregistrementList).hasSize(databaseSizeBeforeUpdate);
        Enregistrement testEnregistrement = enregistrementList.get(enregistrementList.size() - 1);
        assertThat(testEnregistrement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testEnregistrement.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testEnregistrement.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testEnregistrement.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testEnregistrement.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testEnregistrement.getMotclef()).isEqualTo(UPDATED_MOTCLEF);
        assertThat(testEnregistrement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEnregistrement.getEmp()).isEqualTo(UPDATED_EMP);

        // Validate the Enregistrement in Elasticsearch
        Enregistrement enregistrementEs = enregistrementSearchRepository.findOne(testEnregistrement.getId());
        assertThat(enregistrementEs).isEqualToComparingFieldByField(testEnregistrement);
    }

    @Test
    @Transactional
    public void updateNonExistingEnregistrement() throws Exception {
        int databaseSizeBeforeUpdate = enregistrementRepository.findAll().size();

        // Create the Enregistrement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEnregistrementMockMvc.perform(put("/api/enregistrements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enregistrement)))
            .andExpect(status().isCreated());

        // Validate the Enregistrement in the database
        List<Enregistrement> enregistrementList = enregistrementRepository.findAll();
        assertThat(enregistrementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEnregistrement() throws Exception {
        // Initialize the database
        enregistrementRepository.saveAndFlush(enregistrement);
        enregistrementSearchRepository.save(enregistrement);
        int databaseSizeBeforeDelete = enregistrementRepository.findAll().size();

        // Get the enregistrement
        restEnregistrementMockMvc.perform(delete("/api/enregistrements/{id}", enregistrement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean enregistrementExistsInEs = enregistrementSearchRepository.exists(enregistrement.getId());
        assertThat(enregistrementExistsInEs).isFalse();

        // Validate the database is empty
        List<Enregistrement> enregistrementList = enregistrementRepository.findAll();
        assertThat(enregistrementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEnregistrement() throws Exception {
        // Initialize the database
        enregistrementRepository.saveAndFlush(enregistrement);
        enregistrementSearchRepository.save(enregistrement);

        // Search the enregistrement
        restEnregistrementMockMvc.perform(get("/api/_search/enregistrements?query=id:" + enregistrement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enregistrement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].motclef").value(hasItem(DEFAULT_MOTCLEF.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].emp").value(hasItem(DEFAULT_EMP.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enregistrement.class);
    }
}
