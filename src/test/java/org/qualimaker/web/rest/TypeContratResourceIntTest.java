package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.TypeContrat;
import org.qualimaker.repository.TypeContratRepository;
import org.qualimaker.repository.search.TypeContratSearchRepository;
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
 * Test class for the TypeContratResource REST controller.
 *
 * @see TypeContratResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class TypeContratResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private TypeContratRepository typeContratRepository;

    @Autowired
    private TypeContratSearchRepository typeContratSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTypeContratMockMvc;

    private TypeContrat typeContrat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeContratResource typeContratResource = new TypeContratResource(typeContratRepository, typeContratSearchRepository);
        this.restTypeContratMockMvc = MockMvcBuilders.standaloneSetup(typeContratResource)
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
    public static TypeContrat createEntity(EntityManager em) {
        TypeContrat typeContrat = new TypeContrat()
            .type(DEFAULT_TYPE);
        return typeContrat;
    }

    @Before
    public void initTest() {
        typeContratSearchRepository.deleteAll();
        typeContrat = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeContrat() throws Exception {
        int databaseSizeBeforeCreate = typeContratRepository.findAll().size();

        // Create the TypeContrat
        restTypeContratMockMvc.perform(post("/api/type-contrats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeContrat)))
            .andExpect(status().isCreated());

        // Validate the TypeContrat in the database
        List<TypeContrat> typeContratList = typeContratRepository.findAll();
        assertThat(typeContratList).hasSize(databaseSizeBeforeCreate + 1);
        TypeContrat testTypeContrat = typeContratList.get(typeContratList.size() - 1);
        assertThat(testTypeContrat.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the TypeContrat in Elasticsearch
        TypeContrat typeContratEs = typeContratSearchRepository.findOne(testTypeContrat.getId());
        assertThat(typeContratEs).isEqualToComparingFieldByField(testTypeContrat);
    }

    @Test
    @Transactional
    public void createTypeContratWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeContratRepository.findAll().size();

        // Create the TypeContrat with an existing ID
        typeContrat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeContratMockMvc.perform(post("/api/type-contrats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeContrat)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TypeContrat> typeContratList = typeContratRepository.findAll();
        assertThat(typeContratList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTypeContrats() throws Exception {
        // Initialize the database
        typeContratRepository.saveAndFlush(typeContrat);

        // Get all the typeContratList
        restTypeContratMockMvc.perform(get("/api/type-contrats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeContrat.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getTypeContrat() throws Exception {
        // Initialize the database
        typeContratRepository.saveAndFlush(typeContrat);

        // Get the typeContrat
        restTypeContratMockMvc.perform(get("/api/type-contrats/{id}", typeContrat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeContrat.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeContrat() throws Exception {
        // Get the typeContrat
        restTypeContratMockMvc.perform(get("/api/type-contrats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeContrat() throws Exception {
        // Initialize the database
        typeContratRepository.saveAndFlush(typeContrat);
        typeContratSearchRepository.save(typeContrat);
        int databaseSizeBeforeUpdate = typeContratRepository.findAll().size();

        // Update the typeContrat
        TypeContrat updatedTypeContrat = typeContratRepository.findOne(typeContrat.getId());
        updatedTypeContrat
            .type(UPDATED_TYPE);

        restTypeContratMockMvc.perform(put("/api/type-contrats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTypeContrat)))
            .andExpect(status().isOk());

        // Validate the TypeContrat in the database
        List<TypeContrat> typeContratList = typeContratRepository.findAll();
        assertThat(typeContratList).hasSize(databaseSizeBeforeUpdate);
        TypeContrat testTypeContrat = typeContratList.get(typeContratList.size() - 1);
        assertThat(testTypeContrat.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the TypeContrat in Elasticsearch
        TypeContrat typeContratEs = typeContratSearchRepository.findOne(testTypeContrat.getId());
        assertThat(typeContratEs).isEqualToComparingFieldByField(testTypeContrat);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeContrat() throws Exception {
        int databaseSizeBeforeUpdate = typeContratRepository.findAll().size();

        // Create the TypeContrat

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTypeContratMockMvc.perform(put("/api/type-contrats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeContrat)))
            .andExpect(status().isCreated());

        // Validate the TypeContrat in the database
        List<TypeContrat> typeContratList = typeContratRepository.findAll();
        assertThat(typeContratList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTypeContrat() throws Exception {
        // Initialize the database
        typeContratRepository.saveAndFlush(typeContrat);
        typeContratSearchRepository.save(typeContrat);
        int databaseSizeBeforeDelete = typeContratRepository.findAll().size();

        // Get the typeContrat
        restTypeContratMockMvc.perform(delete("/api/type-contrats/{id}", typeContrat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean typeContratExistsInEs = typeContratSearchRepository.exists(typeContrat.getId());
        assertThat(typeContratExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeContrat> typeContratList = typeContratRepository.findAll();
        assertThat(typeContratList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeContrat() throws Exception {
        // Initialize the database
        typeContratRepository.saveAndFlush(typeContrat);
        typeContratSearchRepository.save(typeContrat);

        // Search the typeContrat
        restTypeContratMockMvc.perform(get("/api/_search/type-contrats?query=id:" + typeContrat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeContrat.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeContrat.class);
    }
}
