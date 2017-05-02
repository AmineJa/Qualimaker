package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.repository.TypeFormationRepository;
import org.qualimaker.repository.search.TypeFormationSearchRepository;
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
 * Test class for the TypeFormationResource REST controller.
 *
 * @see TypeFormationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class TypeFormationResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private TypeFormationRepository typeFormationRepository;

    @Autowired
    private TypeFormationSearchRepository typeFormationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTypeFormationMockMvc;

    private TypeFormation typeFormation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeFormationResource typeFormationResource = new TypeFormationResource(typeFormationRepository, typeFormationSearchRepository);
        this.restTypeFormationMockMvc = MockMvcBuilders.standaloneSetup(typeFormationResource)
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
    public static TypeFormation createEntity(EntityManager em) {
        TypeFormation typeFormation = new TypeFormation()
            .libelle(DEFAULT_LIBELLE);
        return typeFormation;
    }

    @Before
    public void initTest() {
        typeFormationSearchRepository.deleteAll();
        typeFormation = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeFormation() throws Exception {
        int databaseSizeBeforeCreate = typeFormationRepository.findAll().size();

        // Create the TypeFormation
        restTypeFormationMockMvc.perform(post("/api/type-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeFormation)))
            .andExpect(status().isCreated());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeCreate + 1);
        TypeFormation testTypeFormation = typeFormationList.get(typeFormationList.size() - 1);
        assertThat(testTypeFormation.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the TypeFormation in Elasticsearch
        TypeFormation typeFormationEs = typeFormationSearchRepository.findOne(testTypeFormation.getId());
        assertThat(typeFormationEs).isEqualToComparingFieldByField(testTypeFormation);
    }

    @Test
    @Transactional
    public void createTypeFormationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeFormationRepository.findAll().size();

        // Create the TypeFormation with an existing ID
        typeFormation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeFormationMockMvc.perform(post("/api/type-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeFormation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTypeFormations() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);

        // Get all the typeFormationList
        restTypeFormationMockMvc.perform(get("/api/type-formations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getTypeFormation() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);

        // Get the typeFormation
        restTypeFormationMockMvc.perform(get("/api/type-formations/{id}", typeFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeFormation.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeFormation() throws Exception {
        // Get the typeFormation
        restTypeFormationMockMvc.perform(get("/api/type-formations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeFormation() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);
        typeFormationSearchRepository.save(typeFormation);
        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();

        // Update the typeFormation
        TypeFormation updatedTypeFormation = typeFormationRepository.findOne(typeFormation.getId());
        updatedTypeFormation
            .libelle(UPDATED_LIBELLE);

        restTypeFormationMockMvc.perform(put("/api/type-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTypeFormation)))
            .andExpect(status().isOk());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate);
        TypeFormation testTypeFormation = typeFormationList.get(typeFormationList.size() - 1);
        assertThat(testTypeFormation.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the TypeFormation in Elasticsearch
        TypeFormation typeFormationEs = typeFormationSearchRepository.findOne(testTypeFormation.getId());
        assertThat(typeFormationEs).isEqualToComparingFieldByField(testTypeFormation);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeFormation() throws Exception {
        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();

        // Create the TypeFormation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTypeFormationMockMvc.perform(put("/api/type-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeFormation)))
            .andExpect(status().isCreated());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTypeFormation() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);
        typeFormationSearchRepository.save(typeFormation);
        int databaseSizeBeforeDelete = typeFormationRepository.findAll().size();

        // Get the typeFormation
        restTypeFormationMockMvc.perform(delete("/api/type-formations/{id}", typeFormation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean typeFormationExistsInEs = typeFormationSearchRepository.exists(typeFormation.getId());
        assertThat(typeFormationExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeFormation() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);
        typeFormationSearchRepository.save(typeFormation);

        // Search the typeFormation
        restTypeFormationMockMvc.perform(get("/api/_search/type-formations?query=id:" + typeFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeFormation.class);
    }
}
