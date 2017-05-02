package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.repository.TypeFormateurRepository;
import org.qualimaker.repository.search.TypeFormateurSearchRepository;
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
 * Test class for the TypeFormateurResource REST controller.
 *
 * @see TypeFormateurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class TypeFormateurResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private TypeFormateurRepository typeFormateurRepository;

    @Autowired
    private TypeFormateurSearchRepository typeFormateurSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTypeFormateurMockMvc;

    private TypeFormateur typeFormateur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeFormateurResource typeFormateurResource = new TypeFormateurResource(typeFormateurRepository, typeFormateurSearchRepository);
        this.restTypeFormateurMockMvc = MockMvcBuilders.standaloneSetup(typeFormateurResource)
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
    public static TypeFormateur createEntity(EntityManager em) {
        TypeFormateur typeFormateur = new TypeFormateur()
            .libelle(DEFAULT_LIBELLE);
        return typeFormateur;
    }

    @Before
    public void initTest() {
        typeFormateurSearchRepository.deleteAll();
        typeFormateur = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeFormateur() throws Exception {
        int databaseSizeBeforeCreate = typeFormateurRepository.findAll().size();

        // Create the TypeFormateur
        restTypeFormateurMockMvc.perform(post("/api/type-formateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeFormateur)))
            .andExpect(status().isCreated());

        // Validate the TypeFormateur in the database
        List<TypeFormateur> typeFormateurList = typeFormateurRepository.findAll();
        assertThat(typeFormateurList).hasSize(databaseSizeBeforeCreate + 1);
        TypeFormateur testTypeFormateur = typeFormateurList.get(typeFormateurList.size() - 1);
        assertThat(testTypeFormateur.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the TypeFormateur in Elasticsearch
        TypeFormateur typeFormateurEs = typeFormateurSearchRepository.findOne(testTypeFormateur.getId());
        assertThat(typeFormateurEs).isEqualToComparingFieldByField(testTypeFormateur);
    }

    @Test
    @Transactional
    public void createTypeFormateurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeFormateurRepository.findAll().size();

        // Create the TypeFormateur with an existing ID
        typeFormateur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeFormateurMockMvc.perform(post("/api/type-formateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeFormateur)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TypeFormateur> typeFormateurList = typeFormateurRepository.findAll();
        assertThat(typeFormateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTypeFormateurs() throws Exception {
        // Initialize the database
        typeFormateurRepository.saveAndFlush(typeFormateur);

        // Get all the typeFormateurList
        restTypeFormateurMockMvc.perform(get("/api/type-formateurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeFormateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getTypeFormateur() throws Exception {
        // Initialize the database
        typeFormateurRepository.saveAndFlush(typeFormateur);

        // Get the typeFormateur
        restTypeFormateurMockMvc.perform(get("/api/type-formateurs/{id}", typeFormateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeFormateur.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeFormateur() throws Exception {
        // Get the typeFormateur
        restTypeFormateurMockMvc.perform(get("/api/type-formateurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeFormateur() throws Exception {
        // Initialize the database
        typeFormateurRepository.saveAndFlush(typeFormateur);
        typeFormateurSearchRepository.save(typeFormateur);
        int databaseSizeBeforeUpdate = typeFormateurRepository.findAll().size();

        // Update the typeFormateur
        TypeFormateur updatedTypeFormateur = typeFormateurRepository.findOne(typeFormateur.getId());
        updatedTypeFormateur
            .libelle(UPDATED_LIBELLE);

        restTypeFormateurMockMvc.perform(put("/api/type-formateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTypeFormateur)))
            .andExpect(status().isOk());

        // Validate the TypeFormateur in the database
        List<TypeFormateur> typeFormateurList = typeFormateurRepository.findAll();
        assertThat(typeFormateurList).hasSize(databaseSizeBeforeUpdate);
        TypeFormateur testTypeFormateur = typeFormateurList.get(typeFormateurList.size() - 1);
        assertThat(testTypeFormateur.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the TypeFormateur in Elasticsearch
        TypeFormateur typeFormateurEs = typeFormateurSearchRepository.findOne(testTypeFormateur.getId());
        assertThat(typeFormateurEs).isEqualToComparingFieldByField(testTypeFormateur);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeFormateur() throws Exception {
        int databaseSizeBeforeUpdate = typeFormateurRepository.findAll().size();

        // Create the TypeFormateur

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTypeFormateurMockMvc.perform(put("/api/type-formateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeFormateur)))
            .andExpect(status().isCreated());

        // Validate the TypeFormateur in the database
        List<TypeFormateur> typeFormateurList = typeFormateurRepository.findAll();
        assertThat(typeFormateurList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTypeFormateur() throws Exception {
        // Initialize the database
        typeFormateurRepository.saveAndFlush(typeFormateur);
        typeFormateurSearchRepository.save(typeFormateur);
        int databaseSizeBeforeDelete = typeFormateurRepository.findAll().size();

        // Get the typeFormateur
        restTypeFormateurMockMvc.perform(delete("/api/type-formateurs/{id}", typeFormateur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean typeFormateurExistsInEs = typeFormateurSearchRepository.exists(typeFormateur.getId());
        assertThat(typeFormateurExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeFormateur> typeFormateurList = typeFormateurRepository.findAll();
        assertThat(typeFormateurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeFormateur() throws Exception {
        // Initialize the database
        typeFormateurRepository.saveAndFlush(typeFormateur);
        typeFormateurSearchRepository.save(typeFormateur);

        // Search the typeFormateur
        restTypeFormateurMockMvc.perform(get("/api/_search/type-formateurs?query=id:" + typeFormateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeFormateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeFormateur.class);
    }
}
