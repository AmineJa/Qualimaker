package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.DocumenExterne;
import org.qualimaker.domain.Employe;
import org.qualimaker.domain.TypeDocumentation;
import org.qualimaker.repository.DocumenExterneRepository;
import org.qualimaker.repository.search.DocumenExterneSearchRepository;
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
 * Test class for the DocumenExterneResource REST controller.
 *
 * @see DocumenExterneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class DocumenExterneResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATEREVISION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATEREVISION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_INDICE_EVOLUTION = "AAAAAAAAAA";
    private static final String UPDATED_INDICE_EVOLUTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FICHIER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FICHIER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_NOTIF = 1;
    private static final Integer UPDATED_NOTIF = 2;

    @Autowired
    private DocumenExterneRepository documenExterneRepository;

    @Autowired
    private DocumenExterneSearchRepository documenExterneSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocumenExterneMockMvc;

    private DocumenExterne documenExterne;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DocumenExterneResource documenExterneResource = new DocumenExterneResource(documenExterneRepository, documenExterneSearchRepository);
        this.restDocumenExterneMockMvc = MockMvcBuilders.standaloneSetup(documenExterneResource)
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
    public static DocumenExterne createEntity(EntityManager em) {
        DocumenExterne documenExterne = new DocumenExterne()
            .code(DEFAULT_CODE)
            .libele(DEFAULT_LIBELE)
            .daterevision(DEFAULT_DATEREVISION)
            .indiceEvolution(DEFAULT_INDICE_EVOLUTION)
            .fichier(DEFAULT_FICHIER)
            .fichierContentType(DEFAULT_FICHIER_CONTENT_TYPE)
            .notif(DEFAULT_NOTIF);
        // Add required entity
        Employe employe = EmployeResourceIntTest.createEntity(em);
        em.persist(employe);
        em.flush();
        documenExterne.getEmployes().add(employe);
        // Add required entity
        TypeDocumentation typeDocumentation = TypeDocumentationResourceIntTest.createEntity(em);
        em.persist(typeDocumentation);
        em.flush();
        documenExterne.setTypeDocumentation(typeDocumentation);
        return documenExterne;
    }

    @Before
    public void initTest() {
        documenExterneSearchRepository.deleteAll();
        documenExterne = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumenExterne() throws Exception {
        int databaseSizeBeforeCreate = documenExterneRepository.findAll().size();

        // Create the DocumenExterne
        restDocumenExterneMockMvc.perform(post("/api/documen-externes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documenExterne)))
            .andExpect(status().isCreated());

        // Validate the DocumenExterne in the database
        List<DocumenExterne> documenExterneList = documenExterneRepository.findAll();
        assertThat(documenExterneList).hasSize(databaseSizeBeforeCreate + 1);
        DocumenExterne testDocumenExterne = documenExterneList.get(documenExterneList.size() - 1);
        assertThat(testDocumenExterne.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDocumenExterne.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testDocumenExterne.getDaterevision()).isEqualTo(DEFAULT_DATEREVISION);
        assertThat(testDocumenExterne.getIndiceEvolution()).isEqualTo(DEFAULT_INDICE_EVOLUTION);
        assertThat(testDocumenExterne.getFichier()).isEqualTo(DEFAULT_FICHIER);
        assertThat(testDocumenExterne.getFichierContentType()).isEqualTo(DEFAULT_FICHIER_CONTENT_TYPE);
        assertThat(testDocumenExterne.getNotif()).isEqualTo(DEFAULT_NOTIF);

        // Validate the DocumenExterne in Elasticsearch
        DocumenExterne documenExterneEs = documenExterneSearchRepository.findOne(testDocumenExterne.getId());
        assertThat(documenExterneEs).isEqualToComparingFieldByField(testDocumenExterne);
    }

    @Test
    @Transactional
    public void createDocumenExterneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documenExterneRepository.findAll().size();

        // Create the DocumenExterne with an existing ID
        documenExterne.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumenExterneMockMvc.perform(post("/api/documen-externes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documenExterne)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DocumenExterne> documenExterneList = documenExterneRepository.findAll();
        assertThat(documenExterneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = documenExterneRepository.findAll().size();
        // set the field null
        documenExterne.setCode(null);

        // Create the DocumenExterne, which fails.

        restDocumenExterneMockMvc.perform(post("/api/documen-externes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documenExterne)))
            .andExpect(status().isBadRequest());

        List<DocumenExterne> documenExterneList = documenExterneRepository.findAll();
        assertThat(documenExterneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibeleIsRequired() throws Exception {
        int databaseSizeBeforeTest = documenExterneRepository.findAll().size();
        // set the field null
        documenExterne.setLibele(null);

        // Create the DocumenExterne, which fails.

        restDocumenExterneMockMvc.perform(post("/api/documen-externes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documenExterne)))
            .andExpect(status().isBadRequest());

        List<DocumenExterne> documenExterneList = documenExterneRepository.findAll();
        assertThat(documenExterneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocumenExternes() throws Exception {
        // Initialize the database
        documenExterneRepository.saveAndFlush(documenExterne);

        // Get all the documenExterneList
        restDocumenExterneMockMvc.perform(get("/api/documen-externes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documenExterne.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE.toString())))
            .andExpect(jsonPath("$.[*].daterevision").value(hasItem(sameInstant(DEFAULT_DATEREVISION))))
            .andExpect(jsonPath("$.[*].indiceEvolution").value(hasItem(DEFAULT_INDICE_EVOLUTION.toString())))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].notif").value(hasItem(DEFAULT_NOTIF)));
    }

    @Test
    @Transactional
    public void getDocumenExterne() throws Exception {
        // Initialize the database
        documenExterneRepository.saveAndFlush(documenExterne);

        // Get the documenExterne
        restDocumenExterneMockMvc.perform(get("/api/documen-externes/{id}", documenExterne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documenExterne.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE.toString()))
            .andExpect(jsonPath("$.daterevision").value(sameInstant(DEFAULT_DATEREVISION)))
            .andExpect(jsonPath("$.indiceEvolution").value(DEFAULT_INDICE_EVOLUTION.toString()))
            .andExpect(jsonPath("$.fichierContentType").value(DEFAULT_FICHIER_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichier").value(Base64Utils.encodeToString(DEFAULT_FICHIER)))
            .andExpect(jsonPath("$.notif").value(DEFAULT_NOTIF));
    }

    @Test
    @Transactional
    public void getNonExistingDocumenExterne() throws Exception {
        // Get the documenExterne
        restDocumenExterneMockMvc.perform(get("/api/documen-externes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumenExterne() throws Exception {
        // Initialize the database
        documenExterneRepository.saveAndFlush(documenExterne);
        documenExterneSearchRepository.save(documenExterne);
        int databaseSizeBeforeUpdate = documenExterneRepository.findAll().size();

        // Update the documenExterne
        DocumenExterne updatedDocumenExterne = documenExterneRepository.findOne(documenExterne.getId());
        updatedDocumenExterne
            .code(UPDATED_CODE)
            .libele(UPDATED_LIBELE)
            .daterevision(UPDATED_DATEREVISION)
            .indiceEvolution(UPDATED_INDICE_EVOLUTION)
            .fichier(UPDATED_FICHIER)
            .fichierContentType(UPDATED_FICHIER_CONTENT_TYPE)
            .notif(UPDATED_NOTIF);

        restDocumenExterneMockMvc.perform(put("/api/documen-externes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocumenExterne)))
            .andExpect(status().isOk());

        // Validate the DocumenExterne in the database
        List<DocumenExterne> documenExterneList = documenExterneRepository.findAll();
        assertThat(documenExterneList).hasSize(databaseSizeBeforeUpdate);
        DocumenExterne testDocumenExterne = documenExterneList.get(documenExterneList.size() - 1);
        assertThat(testDocumenExterne.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDocumenExterne.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testDocumenExterne.getDaterevision()).isEqualTo(UPDATED_DATEREVISION);
        assertThat(testDocumenExterne.getIndiceEvolution()).isEqualTo(UPDATED_INDICE_EVOLUTION);
        assertThat(testDocumenExterne.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testDocumenExterne.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testDocumenExterne.getNotif()).isEqualTo(UPDATED_NOTIF);

        // Validate the DocumenExterne in Elasticsearch
        DocumenExterne documenExterneEs = documenExterneSearchRepository.findOne(testDocumenExterne.getId());
        assertThat(documenExterneEs).isEqualToComparingFieldByField(testDocumenExterne);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumenExterne() throws Exception {
        int databaseSizeBeforeUpdate = documenExterneRepository.findAll().size();

        // Create the DocumenExterne

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocumenExterneMockMvc.perform(put("/api/documen-externes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documenExterne)))
            .andExpect(status().isCreated());

        // Validate the DocumenExterne in the database
        List<DocumenExterne> documenExterneList = documenExterneRepository.findAll();
        assertThat(documenExterneList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocumenExterne() throws Exception {
        // Initialize the database
        documenExterneRepository.saveAndFlush(documenExterne);
        documenExterneSearchRepository.save(documenExterne);
        int databaseSizeBeforeDelete = documenExterneRepository.findAll().size();

        // Get the documenExterne
        restDocumenExterneMockMvc.perform(delete("/api/documen-externes/{id}", documenExterne.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean documenExterneExistsInEs = documenExterneSearchRepository.exists(documenExterne.getId());
        assertThat(documenExterneExistsInEs).isFalse();

        // Validate the database is empty
        List<DocumenExterne> documenExterneList = documenExterneRepository.findAll();
        assertThat(documenExterneList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDocumenExterne() throws Exception {
        // Initialize the database
        documenExterneRepository.saveAndFlush(documenExterne);
        documenExterneSearchRepository.save(documenExterne);

        // Search the documenExterne
        restDocumenExterneMockMvc.perform(get("/api/_search/documen-externes?query=id:" + documenExterne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documenExterne.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE.toString())))
            .andExpect(jsonPath("$.[*].daterevision").value(hasItem(sameInstant(DEFAULT_DATEREVISION))))
            .andExpect(jsonPath("$.[*].indiceEvolution").value(hasItem(DEFAULT_INDICE_EVOLUTION.toString())))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].notif").value(hasItem(DEFAULT_NOTIF)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumenExterne.class);
    }
}
