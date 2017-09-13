package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.DocumentInterne;
import org.qualimaker.domain.TypeDocumentation;
import org.qualimaker.repository.DocumentInterneRepository;
import org.qualimaker.repository.search.DocumentInterneSearchRepository;
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
 * Test class for the DocumentInterneResource REST controller.
 *
 * @see DocumentInterneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class DocumentInterneResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FICHIER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FICHIER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_MOTIF = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_V = false;
    private static final Boolean UPDATED_V = true;

    private static final Boolean DEFAULT_R = false;
    private static final Boolean UPDATED_R = true;

    private static final Boolean DEFAULT_A = false;
    private static final Boolean UPDATED_A = true;

    private static final String DEFAULT_TYPEUSER = "AAAAAAAAAA";
    private static final String UPDATED_TYPEUSER = "BBBBBBBBBB";

    private static final String DEFAULT_PRECEDENT = "AAAAAAAAAA";
    private static final String UPDATED_PRECEDENT = "BBBBBBBBBB";

    private static final String DEFAULT_SUIVANT = "AAAAAAAAAA";
    private static final String UPDATED_SUIVANT = "BBBBBBBBBB";

    private static final Float DEFAULT_VERSION = 1F;
    private static final Float UPDATED_VERSION = 2F;

    @Autowired
    private DocumentInterneRepository documentInterneRepository;

    @Autowired
    private DocumentInterneSearchRepository documentInterneSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocumentInterneMockMvc;

    private DocumentInterne documentInterne;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DocumentInterneResource documentInterneResource = new DocumentInterneResource(documentInterneRepository, documentInterneSearchRepository);
        this.restDocumentInterneMockMvc = MockMvcBuilders.standaloneSetup(documentInterneResource)
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
    public static DocumentInterne createEntity(EntityManager em) {
        DocumentInterne documentInterne = new DocumentInterne()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .fichier(DEFAULT_FICHIER)
            .fichierContentType(DEFAULT_FICHIER_CONTENT_TYPE)
            .motif(DEFAULT_MOTIF)
            .message(DEFAULT_MESSAGE)
            .etat(DEFAULT_ETAT)
            .date(DEFAULT_DATE)
            .v(DEFAULT_V)
            .r(DEFAULT_R)
            .a(DEFAULT_A)
            .typeuser(DEFAULT_TYPEUSER)
            .precedent(DEFAULT_PRECEDENT)
            .suivant(DEFAULT_SUIVANT)
            .version(DEFAULT_VERSION);
        // Add required entity
        TypeDocumentation typeDocumentation = TypeDocumentationResourceIntTest.createEntity(em);
        em.persist(typeDocumentation);
        em.flush();
        documentInterne.setTypeDocumentation(typeDocumentation);
        return documentInterne;
    }

    @Before
    public void initTest() {
        documentInterneSearchRepository.deleteAll();
        documentInterne = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumentInterne() throws Exception {
        int databaseSizeBeforeCreate = documentInterneRepository.findAll().size();

        // Create the DocumentInterne
        restDocumentInterneMockMvc.perform(post("/api/document-internes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentInterne)))
            .andExpect(status().isCreated());

        // Validate the DocumentInterne in the database
        List<DocumentInterne> documentInterneList = documentInterneRepository.findAll();
        assertThat(documentInterneList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentInterne testDocumentInterne = documentInterneList.get(documentInterneList.size() - 1);
        assertThat(testDocumentInterne.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDocumentInterne.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDocumentInterne.getFichier()).isEqualTo(DEFAULT_FICHIER);
        assertThat(testDocumentInterne.getFichierContentType()).isEqualTo(DEFAULT_FICHIER_CONTENT_TYPE);
        assertThat(testDocumentInterne.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testDocumentInterne.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testDocumentInterne.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testDocumentInterne.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDocumentInterne.isV()).isEqualTo(DEFAULT_V);
        assertThat(testDocumentInterne.isR()).isEqualTo(DEFAULT_R);
        assertThat(testDocumentInterne.isA()).isEqualTo(DEFAULT_A);
        assertThat(testDocumentInterne.getTypeuser()).isEqualTo(DEFAULT_TYPEUSER);
        assertThat(testDocumentInterne.getPrecedent()).isEqualTo(DEFAULT_PRECEDENT);
        assertThat(testDocumentInterne.getSuivant()).isEqualTo(DEFAULT_SUIVANT);
        assertThat(testDocumentInterne.getVersion()).isEqualTo(DEFAULT_VERSION);

        // Validate the DocumentInterne in Elasticsearch
        DocumentInterne documentInterneEs = documentInterneSearchRepository.findOne(testDocumentInterne.getId());
        assertThat(documentInterneEs).isEqualToComparingFieldByField(testDocumentInterne);
    }

    @Test
    @Transactional
    public void createDocumentInterneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentInterneRepository.findAll().size();

        // Create the DocumentInterne with an existing ID
        documentInterne.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentInterneMockMvc.perform(post("/api/document-internes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentInterne)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DocumentInterne> documentInterneList = documentInterneRepository.findAll();
        assertThat(documentInterneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentInterneRepository.findAll().size();
        // set the field null
        documentInterne.setCode(null);

        // Create the DocumentInterne, which fails.

        restDocumentInterneMockMvc.perform(post("/api/document-internes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentInterne)))
            .andExpect(status().isBadRequest());

        List<DocumentInterne> documentInterneList = documentInterneRepository.findAll();
        assertThat(documentInterneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentInterneRepository.findAll().size();
        // set the field null
        documentInterne.setLibelle(null);

        // Create the DocumentInterne, which fails.

        restDocumentInterneMockMvc.perform(post("/api/document-internes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentInterne)))
            .andExpect(status().isBadRequest());

        List<DocumentInterne> documentInterneList = documentInterneRepository.findAll();
        assertThat(documentInterneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFichierIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentInterneRepository.findAll().size();
        // set the field null
        documentInterne.setFichier(null);

        // Create the DocumentInterne, which fails.

        restDocumentInterneMockMvc.perform(post("/api/document-internes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentInterne)))
            .andExpect(status().isBadRequest());

        List<DocumentInterne> documentInterneList = documentInterneRepository.findAll();
        assertThat(documentInterneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMotifIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentInterneRepository.findAll().size();
        // set the field null
        documentInterne.setMotif(null);

        // Create the DocumentInterne, which fails.

        restDocumentInterneMockMvc.perform(post("/api/document-internes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentInterne)))
            .andExpect(status().isBadRequest());

        List<DocumentInterne> documentInterneList = documentInterneRepository.findAll();
        assertThat(documentInterneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocumentInternes() throws Exception {
        // Initialize the database
        documentInterneRepository.saveAndFlush(documentInterne);

        // Get all the documentInterneList
        restDocumentInterneMockMvc.perform(get("/api/document-internes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentInterne.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].v").value(hasItem(DEFAULT_V.booleanValue())))
            .andExpect(jsonPath("$.[*].r").value(hasItem(DEFAULT_R.booleanValue())))
            .andExpect(jsonPath("$.[*].a").value(hasItem(DEFAULT_A.booleanValue())))
            .andExpect(jsonPath("$.[*].typeuser").value(hasItem(DEFAULT_TYPEUSER.toString())))
            .andExpect(jsonPath("$.[*].precedent").value(hasItem(DEFAULT_PRECEDENT.toString())))
            .andExpect(jsonPath("$.[*].suivant").value(hasItem(DEFAULT_SUIVANT.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.doubleValue())));
    }

    @Test
    @Transactional
    public void getDocumentInterne() throws Exception {
        // Initialize the database
        documentInterneRepository.saveAndFlush(documentInterne);

        // Get the documentInterne
        restDocumentInterneMockMvc.perform(get("/api/document-internes/{id}", documentInterne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documentInterne.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.fichierContentType").value(DEFAULT_FICHIER_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichier").value(Base64Utils.encodeToString(DEFAULT_FICHIER)))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.v").value(DEFAULT_V.booleanValue()))
            .andExpect(jsonPath("$.r").value(DEFAULT_R.booleanValue()))
            .andExpect(jsonPath("$.a").value(DEFAULT_A.booleanValue()))
            .andExpect(jsonPath("$.typeuser").value(DEFAULT_TYPEUSER.toString()))
            .andExpect(jsonPath("$.precedent").value(DEFAULT_PRECEDENT.toString()))
            .andExpect(jsonPath("$.suivant").value(DEFAULT_SUIVANT.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDocumentInterne() throws Exception {
        // Get the documentInterne
        restDocumentInterneMockMvc.perform(get("/api/document-internes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumentInterne() throws Exception {
        // Initialize the database
        documentInterneRepository.saveAndFlush(documentInterne);
        documentInterneSearchRepository.save(documentInterne);
        int databaseSizeBeforeUpdate = documentInterneRepository.findAll().size();

        // Update the documentInterne
        DocumentInterne updatedDocumentInterne = documentInterneRepository.findOne(documentInterne.getId());
        updatedDocumentInterne
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .fichier(UPDATED_FICHIER)
            .fichierContentType(UPDATED_FICHIER_CONTENT_TYPE)
            .motif(UPDATED_MOTIF)
            .message(UPDATED_MESSAGE)
            .etat(UPDATED_ETAT)
            .date(UPDATED_DATE)
            .v(UPDATED_V)
            .r(UPDATED_R)
            .a(UPDATED_A)
            .typeuser(UPDATED_TYPEUSER)
            .precedent(UPDATED_PRECEDENT)
            .suivant(UPDATED_SUIVANT)
            .version(UPDATED_VERSION);

        restDocumentInterneMockMvc.perform(put("/api/document-internes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocumentInterne)))
            .andExpect(status().isOk());

        // Validate the DocumentInterne in the database
        List<DocumentInterne> documentInterneList = documentInterneRepository.findAll();
        assertThat(documentInterneList).hasSize(databaseSizeBeforeUpdate);
        DocumentInterne testDocumentInterne = documentInterneList.get(documentInterneList.size() - 1);
        assertThat(testDocumentInterne.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDocumentInterne.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDocumentInterne.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testDocumentInterne.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testDocumentInterne.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testDocumentInterne.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testDocumentInterne.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testDocumentInterne.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDocumentInterne.isV()).isEqualTo(UPDATED_V);
        assertThat(testDocumentInterne.isR()).isEqualTo(UPDATED_R);
        assertThat(testDocumentInterne.isA()).isEqualTo(UPDATED_A);
        assertThat(testDocumentInterne.getTypeuser()).isEqualTo(UPDATED_TYPEUSER);
        assertThat(testDocumentInterne.getPrecedent()).isEqualTo(UPDATED_PRECEDENT);
        assertThat(testDocumentInterne.getSuivant()).isEqualTo(UPDATED_SUIVANT);
        assertThat(testDocumentInterne.getVersion()).isEqualTo(UPDATED_VERSION);

        // Validate the DocumentInterne in Elasticsearch
        DocumentInterne documentInterneEs = documentInterneSearchRepository.findOne(testDocumentInterne.getId());
        assertThat(documentInterneEs).isEqualToComparingFieldByField(testDocumentInterne);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumentInterne() throws Exception {
        int databaseSizeBeforeUpdate = documentInterneRepository.findAll().size();

        // Create the DocumentInterne

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocumentInterneMockMvc.perform(put("/api/document-internes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentInterne)))
            .andExpect(status().isCreated());

        // Validate the DocumentInterne in the database
        List<DocumentInterne> documentInterneList = documentInterneRepository.findAll();
        assertThat(documentInterneList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocumentInterne() throws Exception {
        // Initialize the database
        documentInterneRepository.saveAndFlush(documentInterne);
        documentInterneSearchRepository.save(documentInterne);
        int databaseSizeBeforeDelete = documentInterneRepository.findAll().size();

        // Get the documentInterne
        restDocumentInterneMockMvc.perform(delete("/api/document-internes/{id}", documentInterne.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean documentInterneExistsInEs = documentInterneSearchRepository.exists(documentInterne.getId());
        assertThat(documentInterneExistsInEs).isFalse();

        // Validate the database is empty
        List<DocumentInterne> documentInterneList = documentInterneRepository.findAll();
        assertThat(documentInterneList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDocumentInterne() throws Exception {
        // Initialize the database
        documentInterneRepository.saveAndFlush(documentInterne);
        documentInterneSearchRepository.save(documentInterne);

        // Search the documentInterne
        restDocumentInterneMockMvc.perform(get("/api/_search/document-internes?query=id:" + documentInterne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentInterne.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].v").value(hasItem(DEFAULT_V.booleanValue())))
            .andExpect(jsonPath("$.[*].r").value(hasItem(DEFAULT_R.booleanValue())))
            .andExpect(jsonPath("$.[*].a").value(hasItem(DEFAULT_A.booleanValue())))
            .andExpect(jsonPath("$.[*].typeuser").value(hasItem(DEFAULT_TYPEUSER.toString())))
            .andExpect(jsonPath("$.[*].precedent").value(hasItem(DEFAULT_PRECEDENT.toString())))
            .andExpect(jsonPath("$.[*].suivant").value(hasItem(DEFAULT_SUIVANT.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentInterne.class);
    }
}
