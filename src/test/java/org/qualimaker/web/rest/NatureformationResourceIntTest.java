package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Natureformation;
import org.qualimaker.repository.NatureformationRepository;
import org.qualimaker.repository.search.NatureformationSearchRepository;
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
 * Test class for the NatureformationResource REST controller.
 *
 * @see NatureformationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class NatureformationResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private NatureformationRepository natureformationRepository;

    @Autowired
    private NatureformationSearchRepository natureformationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNatureformationMockMvc;

    private Natureformation natureformation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NatureformationResource natureformationResource = new NatureformationResource(natureformationRepository, natureformationSearchRepository);
        this.restNatureformationMockMvc = MockMvcBuilders.standaloneSetup(natureformationResource)
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
    public static Natureformation createEntity(EntityManager em) {
        Natureformation natureformation = new Natureformation()
            .libelle(DEFAULT_LIBELLE);
        return natureformation;
    }

    @Before
    public void initTest() {
        natureformationSearchRepository.deleteAll();
        natureformation = createEntity(em);
    }

    @Test
    @Transactional
    public void createNatureformation() throws Exception {
        int databaseSizeBeforeCreate = natureformationRepository.findAll().size();

        // Create the Natureformation
        restNatureformationMockMvc.perform(post("/api/natureformations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(natureformation)))
            .andExpect(status().isCreated());

        // Validate the Natureformation in the database
        List<Natureformation> natureformationList = natureformationRepository.findAll();
        assertThat(natureformationList).hasSize(databaseSizeBeforeCreate + 1);
        Natureformation testNatureformation = natureformationList.get(natureformationList.size() - 1);
        assertThat(testNatureformation.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the Natureformation in Elasticsearch
        Natureformation natureformationEs = natureformationSearchRepository.findOne(testNatureformation.getId());
        assertThat(natureformationEs).isEqualToComparingFieldByField(testNatureformation);
    }

    @Test
    @Transactional
    public void createNatureformationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = natureformationRepository.findAll().size();

        // Create the Natureformation with an existing ID
        natureformation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNatureformationMockMvc.perform(post("/api/natureformations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(natureformation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Natureformation> natureformationList = natureformationRepository.findAll();
        assertThat(natureformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNatureformations() throws Exception {
        // Initialize the database
        natureformationRepository.saveAndFlush(natureformation);

        // Get all the natureformationList
        restNatureformationMockMvc.perform(get("/api/natureformations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(natureformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getNatureformation() throws Exception {
        // Initialize the database
        natureformationRepository.saveAndFlush(natureformation);

        // Get the natureformation
        restNatureformationMockMvc.perform(get("/api/natureformations/{id}", natureformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(natureformation.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNatureformation() throws Exception {
        // Get the natureformation
        restNatureformationMockMvc.perform(get("/api/natureformations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNatureformation() throws Exception {
        // Initialize the database
        natureformationRepository.saveAndFlush(natureformation);
        natureformationSearchRepository.save(natureformation);
        int databaseSizeBeforeUpdate = natureformationRepository.findAll().size();

        // Update the natureformation
        Natureformation updatedNatureformation = natureformationRepository.findOne(natureformation.getId());
        updatedNatureformation
            .libelle(UPDATED_LIBELLE);

        restNatureformationMockMvc.perform(put("/api/natureformations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNatureformation)))
            .andExpect(status().isOk());

        // Validate the Natureformation in the database
        List<Natureformation> natureformationList = natureformationRepository.findAll();
        assertThat(natureformationList).hasSize(databaseSizeBeforeUpdate);
        Natureformation testNatureformation = natureformationList.get(natureformationList.size() - 1);
        assertThat(testNatureformation.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the Natureformation in Elasticsearch
        Natureformation natureformationEs = natureformationSearchRepository.findOne(testNatureformation.getId());
        assertThat(natureformationEs).isEqualToComparingFieldByField(testNatureformation);
    }

    @Test
    @Transactional
    public void updateNonExistingNatureformation() throws Exception {
        int databaseSizeBeforeUpdate = natureformationRepository.findAll().size();

        // Create the Natureformation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNatureformationMockMvc.perform(put("/api/natureformations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(natureformation)))
            .andExpect(status().isCreated());

        // Validate the Natureformation in the database
        List<Natureformation> natureformationList = natureformationRepository.findAll();
        assertThat(natureformationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNatureformation() throws Exception {
        // Initialize the database
        natureformationRepository.saveAndFlush(natureformation);
        natureformationSearchRepository.save(natureformation);
        int databaseSizeBeforeDelete = natureformationRepository.findAll().size();

        // Get the natureformation
        restNatureformationMockMvc.perform(delete("/api/natureformations/{id}", natureformation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean natureformationExistsInEs = natureformationSearchRepository.exists(natureformation.getId());
        assertThat(natureformationExistsInEs).isFalse();

        // Validate the database is empty
        List<Natureformation> natureformationList = natureformationRepository.findAll();
        assertThat(natureformationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNatureformation() throws Exception {
        // Initialize the database
        natureformationRepository.saveAndFlush(natureformation);
        natureformationSearchRepository.save(natureformation);

        // Search the natureformation
        restNatureformationMockMvc.perform(get("/api/_search/natureformations?query=id:" + natureformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(natureformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Natureformation.class);
    }
}
