package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.TypeDocumentation;
import org.qualimaker.domain.Employe;
import org.qualimaker.repository.TypeDocumentationRepository;
import org.qualimaker.repository.search.TypeDocumentationSearchRepository;
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

import org.qualimaker.domain.enumeration.NatureDoc;
/**
 * Test class for the TypeDocumentationResource REST controller.
 *
 * @see TypeDocumentationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class TypeDocumentationResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_P_DF = false;
    private static final Boolean UPDATED_P_DF = true;

    private static final Boolean DEFAULT_SECURISE = false;
    private static final Boolean UPDATED_SECURISE = true;

    private static final String DEFAULT_ABREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABREVIATION = "BBBBBBBBBB";

    private static final NatureDoc DEFAULT_NATURE = NatureDoc.INTERNE;
    private static final NatureDoc UPDATED_NATURE = NatureDoc.EXTERNE;

    private static final Integer DEFAULT_NOTIF = 1;
    private static final Integer UPDATED_NOTIF = 2;

    @Autowired
    private TypeDocumentationRepository typeDocumentationRepository;

    @Autowired
    private TypeDocumentationSearchRepository typeDocumentationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTypeDocumentationMockMvc;

    private TypeDocumentation typeDocumentation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeDocumentationResource typeDocumentationResource = new TypeDocumentationResource(typeDocumentationRepository, typeDocumentationSearchRepository);
        this.restTypeDocumentationMockMvc = MockMvcBuilders.standaloneSetup(typeDocumentationResource)
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
    public static TypeDocumentation createEntity(EntityManager em) {
        TypeDocumentation typeDocumentation = new TypeDocumentation()
            .type(DEFAULT_TYPE)
            .pDf(DEFAULT_P_DF)
            .securise(DEFAULT_SECURISE)
            .abreviation(DEFAULT_ABREVIATION)
            .nature(DEFAULT_NATURE)
            .notif(DEFAULT_NOTIF);
        // Add required entity
        Employe employe = EmployeResourceIntTest.createEntity(em);
        em.persist(employe);
        em.flush();
        typeDocumentation.setEmploye(employe);
        return typeDocumentation;
    }

    @Before
    public void initTest() {
        typeDocumentationSearchRepository.deleteAll();
        typeDocumentation = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeDocumentation() throws Exception {
        int databaseSizeBeforeCreate = typeDocumentationRepository.findAll().size();

        // Create the TypeDocumentation
        restTypeDocumentationMockMvc.perform(post("/api/type-documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeDocumentation)))
            .andExpect(status().isCreated());

        // Validate the TypeDocumentation in the database
        List<TypeDocumentation> typeDocumentationList = typeDocumentationRepository.findAll();
        assertThat(typeDocumentationList).hasSize(databaseSizeBeforeCreate + 1);
        TypeDocumentation testTypeDocumentation = typeDocumentationList.get(typeDocumentationList.size() - 1);
        assertThat(testTypeDocumentation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTypeDocumentation.ispDf()).isEqualTo(DEFAULT_P_DF);
        assertThat(testTypeDocumentation.isSecurise()).isEqualTo(DEFAULT_SECURISE);
        assertThat(testTypeDocumentation.getAbreviation()).isEqualTo(DEFAULT_ABREVIATION);
        assertThat(testTypeDocumentation.getNature()).isEqualTo(DEFAULT_NATURE);
        assertThat(testTypeDocumentation.getNotif()).isEqualTo(DEFAULT_NOTIF);

        // Validate the TypeDocumentation in Elasticsearch
        TypeDocumentation typeDocumentationEs = typeDocumentationSearchRepository.findOne(testTypeDocumentation.getId());
        assertThat(typeDocumentationEs).isEqualToComparingFieldByField(testTypeDocumentation);
    }

    @Test
    @Transactional
    public void createTypeDocumentationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeDocumentationRepository.findAll().size();

        // Create the TypeDocumentation with an existing ID
        typeDocumentation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeDocumentationMockMvc.perform(post("/api/type-documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeDocumentation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TypeDocumentation> typeDocumentationList = typeDocumentationRepository.findAll();
        assertThat(typeDocumentationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeDocumentationRepository.findAll().size();
        // set the field null
        typeDocumentation.setType(null);

        // Create the TypeDocumentation, which fails.

        restTypeDocumentationMockMvc.perform(post("/api/type-documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeDocumentation)))
            .andExpect(status().isBadRequest());

        List<TypeDocumentation> typeDocumentationList = typeDocumentationRepository.findAll();
        assertThat(typeDocumentationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeDocumentations() throws Exception {
        // Initialize the database
        typeDocumentationRepository.saveAndFlush(typeDocumentation);

        // Get all the typeDocumentationList
        restTypeDocumentationMockMvc.perform(get("/api/type-documentations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDocumentation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pDf").value(hasItem(DEFAULT_P_DF.booleanValue())))
            .andExpect(jsonPath("$.[*].securise").value(hasItem(DEFAULT_SECURISE.booleanValue())))
            .andExpect(jsonPath("$.[*].abreviation").value(hasItem(DEFAULT_ABREVIATION.toString())))
            .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE.toString())))
            .andExpect(jsonPath("$.[*].notif").value(hasItem(DEFAULT_NOTIF)));
    }

    @Test
    @Transactional
    public void getTypeDocumentation() throws Exception {
        // Initialize the database
        typeDocumentationRepository.saveAndFlush(typeDocumentation);

        // Get the typeDocumentation
        restTypeDocumentationMockMvc.perform(get("/api/type-documentations/{id}", typeDocumentation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeDocumentation.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.pDf").value(DEFAULT_P_DF.booleanValue()))
            .andExpect(jsonPath("$.securise").value(DEFAULT_SECURISE.booleanValue()))
            .andExpect(jsonPath("$.abreviation").value(DEFAULT_ABREVIATION.toString()))
            .andExpect(jsonPath("$.nature").value(DEFAULT_NATURE.toString()))
            .andExpect(jsonPath("$.notif").value(DEFAULT_NOTIF));
    }

    @Test
    @Transactional
    public void getNonExistingTypeDocumentation() throws Exception {
        // Get the typeDocumentation
        restTypeDocumentationMockMvc.perform(get("/api/type-documentations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeDocumentation() throws Exception {
        // Initialize the database
        typeDocumentationRepository.saveAndFlush(typeDocumentation);
        typeDocumentationSearchRepository.save(typeDocumentation);
        int databaseSizeBeforeUpdate = typeDocumentationRepository.findAll().size();

        // Update the typeDocumentation
        TypeDocumentation updatedTypeDocumentation = typeDocumentationRepository.findOne(typeDocumentation.getId());
        updatedTypeDocumentation
            .type(UPDATED_TYPE)
            .pDf(UPDATED_P_DF)
            .securise(UPDATED_SECURISE)
            .abreviation(UPDATED_ABREVIATION)
            .nature(UPDATED_NATURE)
            .notif(UPDATED_NOTIF);

        restTypeDocumentationMockMvc.perform(put("/api/type-documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTypeDocumentation)))
            .andExpect(status().isOk());

        // Validate the TypeDocumentation in the database
        List<TypeDocumentation> typeDocumentationList = typeDocumentationRepository.findAll();
        assertThat(typeDocumentationList).hasSize(databaseSizeBeforeUpdate);
        TypeDocumentation testTypeDocumentation = typeDocumentationList.get(typeDocumentationList.size() - 1);
        assertThat(testTypeDocumentation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTypeDocumentation.ispDf()).isEqualTo(UPDATED_P_DF);
        assertThat(testTypeDocumentation.isSecurise()).isEqualTo(UPDATED_SECURISE);
        assertThat(testTypeDocumentation.getAbreviation()).isEqualTo(UPDATED_ABREVIATION);
        assertThat(testTypeDocumentation.getNature()).isEqualTo(UPDATED_NATURE);
        assertThat(testTypeDocumentation.getNotif()).isEqualTo(UPDATED_NOTIF);

        // Validate the TypeDocumentation in Elasticsearch
        TypeDocumentation typeDocumentationEs = typeDocumentationSearchRepository.findOne(testTypeDocumentation.getId());
        assertThat(typeDocumentationEs).isEqualToComparingFieldByField(testTypeDocumentation);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = typeDocumentationRepository.findAll().size();

        // Create the TypeDocumentation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTypeDocumentationMockMvc.perform(put("/api/type-documentations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeDocumentation)))
            .andExpect(status().isCreated());

        // Validate the TypeDocumentation in the database
        List<TypeDocumentation> typeDocumentationList = typeDocumentationRepository.findAll();
        assertThat(typeDocumentationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTypeDocumentation() throws Exception {
        // Initialize the database
        typeDocumentationRepository.saveAndFlush(typeDocumentation);
        typeDocumentationSearchRepository.save(typeDocumentation);
        int databaseSizeBeforeDelete = typeDocumentationRepository.findAll().size();

        // Get the typeDocumentation
        restTypeDocumentationMockMvc.perform(delete("/api/type-documentations/{id}", typeDocumentation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean typeDocumentationExistsInEs = typeDocumentationSearchRepository.exists(typeDocumentation.getId());
        assertThat(typeDocumentationExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeDocumentation> typeDocumentationList = typeDocumentationRepository.findAll();
        assertThat(typeDocumentationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeDocumentation() throws Exception {
        // Initialize the database
        typeDocumentationRepository.saveAndFlush(typeDocumentation);
        typeDocumentationSearchRepository.save(typeDocumentation);

        // Search the typeDocumentation
        restTypeDocumentationMockMvc.perform(get("/api/_search/type-documentations?query=id:" + typeDocumentation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDocumentation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pDf").value(hasItem(DEFAULT_P_DF.booleanValue())))
            .andExpect(jsonPath("$.[*].securise").value(hasItem(DEFAULT_SECURISE.booleanValue())))
            .andExpect(jsonPath("$.[*].abreviation").value(hasItem(DEFAULT_ABREVIATION.toString())))
            .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE.toString())))
            .andExpect(jsonPath("$.[*].notif").value(hasItem(DEFAULT_NOTIF)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDocumentation.class);
    }
}
