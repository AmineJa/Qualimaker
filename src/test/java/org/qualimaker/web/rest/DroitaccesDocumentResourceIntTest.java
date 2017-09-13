package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.DroitaccesDocument;
import org.qualimaker.domain.Employe;
import org.qualimaker.repository.DroitaccesDocumentRepository;
import org.qualimaker.repository.search.DroitaccesDocumentSearchRepository;
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

import org.qualimaker.domain.enumeration.Roles;
/**
 * Test class for the DroitaccesDocumentResource REST controller.
 *
 * @see DroitaccesDocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class DroitaccesDocumentResourceIntTest {

    private static final Roles DEFAULT_ROLES = Roles.Superviseur;
    private static final Roles UPDATED_ROLES = Roles.Redacteur;

    @Autowired
    private DroitaccesDocumentRepository droitaccesDocumentRepository;

    @Autowired
    private DroitaccesDocumentSearchRepository droitaccesDocumentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDroitaccesDocumentMockMvc;

    private DroitaccesDocument droitaccesDocument;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DroitaccesDocumentResource droitaccesDocumentResource = new DroitaccesDocumentResource(droitaccesDocumentRepository, droitaccesDocumentSearchRepository);
        this.restDroitaccesDocumentMockMvc = MockMvcBuilders.standaloneSetup(droitaccesDocumentResource)
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
    public static DroitaccesDocument createEntity(EntityManager em) {
        DroitaccesDocument droitaccesDocument = new DroitaccesDocument()
            .roles(DEFAULT_ROLES);
        // Add required entity
        Employe employe = EmployeResourceIntTest.createEntity(em);
        em.persist(employe);
        em.flush();
        droitaccesDocument.setEmploye(employe);
        return droitaccesDocument;
    }

    @Before
    public void initTest() {
        droitaccesDocumentSearchRepository.deleteAll();
        droitaccesDocument = createEntity(em);
    }

    @Test
    @Transactional
    public void createDroitaccesDocument() throws Exception {
        int databaseSizeBeforeCreate = droitaccesDocumentRepository.findAll().size();

        // Create the DroitaccesDocument
        restDroitaccesDocumentMockMvc.perform(post("/api/droitacces-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(droitaccesDocument)))
            .andExpect(status().isCreated());

        // Validate the DroitaccesDocument in the database
        List<DroitaccesDocument> droitaccesDocumentList = droitaccesDocumentRepository.findAll();
        assertThat(droitaccesDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        DroitaccesDocument testDroitaccesDocument = droitaccesDocumentList.get(droitaccesDocumentList.size() - 1);
        assertThat(testDroitaccesDocument.getRoles()).isEqualTo(DEFAULT_ROLES);

        // Validate the DroitaccesDocument in Elasticsearch
        DroitaccesDocument droitaccesDocumentEs = droitaccesDocumentSearchRepository.findOne(testDroitaccesDocument.getId());
        assertThat(droitaccesDocumentEs).isEqualToComparingFieldByField(testDroitaccesDocument);
    }

    @Test
    @Transactional
    public void createDroitaccesDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = droitaccesDocumentRepository.findAll().size();

        // Create the DroitaccesDocument with an existing ID
        droitaccesDocument.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDroitaccesDocumentMockMvc.perform(post("/api/droitacces-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(droitaccesDocument)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DroitaccesDocument> droitaccesDocumentList = droitaccesDocumentRepository.findAll();
        assertThat(droitaccesDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRolesIsRequired() throws Exception {
        int databaseSizeBeforeTest = droitaccesDocumentRepository.findAll().size();
        // set the field null
        droitaccesDocument.setRoles(null);

        // Create the DroitaccesDocument, which fails.

        restDroitaccesDocumentMockMvc.perform(post("/api/droitacces-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(droitaccesDocument)))
            .andExpect(status().isBadRequest());

        List<DroitaccesDocument> droitaccesDocumentList = droitaccesDocumentRepository.findAll();
        assertThat(droitaccesDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDroitaccesDocuments() throws Exception {
        // Initialize the database
        droitaccesDocumentRepository.saveAndFlush(droitaccesDocument);

        // Get all the droitaccesDocumentList
        restDroitaccesDocumentMockMvc.perform(get("/api/droitacces-documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(droitaccesDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].roles").value(hasItem(DEFAULT_ROLES.toString())));
    }

    @Test
    @Transactional
    public void getDroitaccesDocument() throws Exception {
        // Initialize the database
        droitaccesDocumentRepository.saveAndFlush(droitaccesDocument);

        // Get the droitaccesDocument
        restDroitaccesDocumentMockMvc.perform(get("/api/droitacces-documents/{id}", droitaccesDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(droitaccesDocument.getId().intValue()))
            .andExpect(jsonPath("$.roles").value(DEFAULT_ROLES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDroitaccesDocument() throws Exception {
        // Get the droitaccesDocument
        restDroitaccesDocumentMockMvc.perform(get("/api/droitacces-documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDroitaccesDocument() throws Exception {
        // Initialize the database
        droitaccesDocumentRepository.saveAndFlush(droitaccesDocument);
        droitaccesDocumentSearchRepository.save(droitaccesDocument);
        int databaseSizeBeforeUpdate = droitaccesDocumentRepository.findAll().size();

        // Update the droitaccesDocument
        DroitaccesDocument updatedDroitaccesDocument = droitaccesDocumentRepository.findOne(droitaccesDocument.getId());
        updatedDroitaccesDocument
            .roles(UPDATED_ROLES);

        restDroitaccesDocumentMockMvc.perform(put("/api/droitacces-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDroitaccesDocument)))
            .andExpect(status().isOk());

        // Validate the DroitaccesDocument in the database
        List<DroitaccesDocument> droitaccesDocumentList = droitaccesDocumentRepository.findAll();
        assertThat(droitaccesDocumentList).hasSize(databaseSizeBeforeUpdate);
        DroitaccesDocument testDroitaccesDocument = droitaccesDocumentList.get(droitaccesDocumentList.size() - 1);
        assertThat(testDroitaccesDocument.getRoles()).isEqualTo(UPDATED_ROLES);

        // Validate the DroitaccesDocument in Elasticsearch
        DroitaccesDocument droitaccesDocumentEs = droitaccesDocumentSearchRepository.findOne(testDroitaccesDocument.getId());
        assertThat(droitaccesDocumentEs).isEqualToComparingFieldByField(testDroitaccesDocument);
    }

    @Test
    @Transactional
    public void updateNonExistingDroitaccesDocument() throws Exception {
        int databaseSizeBeforeUpdate = droitaccesDocumentRepository.findAll().size();

        // Create the DroitaccesDocument

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDroitaccesDocumentMockMvc.perform(put("/api/droitacces-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(droitaccesDocument)))
            .andExpect(status().isCreated());

        // Validate the DroitaccesDocument in the database
        List<DroitaccesDocument> droitaccesDocumentList = droitaccesDocumentRepository.findAll();
        assertThat(droitaccesDocumentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDroitaccesDocument() throws Exception {
        // Initialize the database
        droitaccesDocumentRepository.saveAndFlush(droitaccesDocument);
        droitaccesDocumentSearchRepository.save(droitaccesDocument);
        int databaseSizeBeforeDelete = droitaccesDocumentRepository.findAll().size();

        // Get the droitaccesDocument
        restDroitaccesDocumentMockMvc.perform(delete("/api/droitacces-documents/{id}", droitaccesDocument.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean droitaccesDocumentExistsInEs = droitaccesDocumentSearchRepository.exists(droitaccesDocument.getId());
        assertThat(droitaccesDocumentExistsInEs).isFalse();

        // Validate the database is empty
        List<DroitaccesDocument> droitaccesDocumentList = droitaccesDocumentRepository.findAll();
        assertThat(droitaccesDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDroitaccesDocument() throws Exception {
        // Initialize the database
        droitaccesDocumentRepository.saveAndFlush(droitaccesDocument);
        droitaccesDocumentSearchRepository.save(droitaccesDocument);

        // Search the droitaccesDocument
        restDroitaccesDocumentMockMvc.perform(get("/api/_search/droitacces-documents?query=id:" + droitaccesDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(droitaccesDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].roles").value(hasItem(DEFAULT_ROLES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DroitaccesDocument.class);
    }
}
