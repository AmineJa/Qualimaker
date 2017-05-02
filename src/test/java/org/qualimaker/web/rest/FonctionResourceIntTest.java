package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.repository.FonctionRepository;
import org.qualimaker.repository.search.FonctionSearchRepository;
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
 * Test class for the FonctionResource REST controller.
 *
 * @see FonctionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class FonctionResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private FonctionRepository fonctionRepository;

    @Autowired
    private FonctionSearchRepository fonctionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFonctionMockMvc;

    private Fonction fonction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FonctionResource fonctionResource = new FonctionResource(fonctionRepository, fonctionSearchRepository);
        this.restFonctionMockMvc = MockMvcBuilders.standaloneSetup(fonctionResource)
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
    public static Fonction createEntity(EntityManager em) {
        Fonction fonction = new Fonction()
            .nom(DEFAULT_NOM);
        return fonction;
    }

    @Before
    public void initTest() {
        fonctionSearchRepository.deleteAll();
        fonction = createEntity(em);
    }

    @Test
    @Transactional
    public void createFonction() throws Exception {
        int databaseSizeBeforeCreate = fonctionRepository.findAll().size();

        // Create the Fonction
        restFonctionMockMvc.perform(post("/api/fonctions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fonction)))
            .andExpect(status().isCreated());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeCreate + 1);
        Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
        assertThat(testFonction.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Fonction in Elasticsearch
        Fonction fonctionEs = fonctionSearchRepository.findOne(testFonction.getId());
        assertThat(fonctionEs).isEqualToComparingFieldByField(testFonction);
    }

    @Test
    @Transactional
    public void createFonctionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fonctionRepository.findAll().size();

        // Create the Fonction with an existing ID
        fonction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFonctionMockMvc.perform(post("/api/fonctions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fonction)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFonctions() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList
        restFonctionMockMvc.perform(get("/api/fonctions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fonction.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get the fonction
        restFonctionMockMvc.perform(get("/api/fonctions/{id}", fonction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fonction.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFonction() throws Exception {
        // Get the fonction
        restFonctionMockMvc.perform(get("/api/fonctions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);
        fonctionSearchRepository.save(fonction);
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

        // Update the fonction
        Fonction updatedFonction = fonctionRepository.findOne(fonction.getId());
        updatedFonction
            .nom(UPDATED_NOM);

        restFonctionMockMvc.perform(put("/api/fonctions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFonction)))
            .andExpect(status().isOk());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
        Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
        assertThat(testFonction.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Fonction in Elasticsearch
        Fonction fonctionEs = fonctionSearchRepository.findOne(testFonction.getId());
        assertThat(fonctionEs).isEqualToComparingFieldByField(testFonction);
    }

    @Test
    @Transactional
    public void updateNonExistingFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

        // Create the Fonction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFonctionMockMvc.perform(put("/api/fonctions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fonction)))
            .andExpect(status().isCreated());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);
        fonctionSearchRepository.save(fonction);
        int databaseSizeBeforeDelete = fonctionRepository.findAll().size();

        // Get the fonction
        restFonctionMockMvc.perform(delete("/api/fonctions/{id}", fonction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean fonctionExistsInEs = fonctionSearchRepository.exists(fonction.getId());
        assertThat(fonctionExistsInEs).isFalse();

        // Validate the database is empty
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);
        fonctionSearchRepository.save(fonction);

        // Search the fonction
        restFonctionMockMvc.perform(get("/api/_search/fonctions?query=id:" + fonction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fonction.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fonction.class);
    }
}
