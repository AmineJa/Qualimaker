package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Employe;
import org.qualimaker.repository.EmployeRepository;
import org.qualimaker.repository.search.EmployeSearchRepository;
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

import org.qualimaker.domain.enumeration.Sexe;
/**
 * Test class for the EmployeResource REST controller.
 *
 * @see EmployeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class EmployeResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_N = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_N = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_CIN = 1D;
    private static final Double UPDATED_CIN = 2D;

    private static final Sexe DEFAULT_SEXE = Sexe.HOMME;
    private static final Sexe UPDATED_SEXE = Sexe.FEMME;

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final Double DEFAULT_GSM = 1D;
    private static final Double UPDATED_GSM = 2D;

    private static final String DEFAULT_LIEU_NAIS = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAIS = "BBBBBBBBBB";

    private static final Double DEFAULT_TEL_MAIS = 1D;
    private static final Double UPDATED_TEL_MAIS = 2D;

    private static final Double DEFAULT_TELEPH = 1D;
    private static final Double UPDATED_TELEPH = 2D;

    private static final ZonedDateTime DEFAULT_DELIVRELE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DELIVRELE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DELIVREA = "AAAAAAAAAA";
    private static final String UPDATED_DELIVREA = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Double DEFAULT_MATRICULE = 1D;
    private static final Double UPDATED_MATRICULE = 2D;

    private static final String DEFAULT_EMAIL_2 = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_GRADE = "AAAAAAAAAA";
    private static final String UPDATED_GRADE = "BBBBBBBBBB";

    private static final Double DEFAULT_RIB = 1D;
    private static final Double UPDATED_RIB = 2D;

    private static final Double DEFAULT_NSC = 1D;
    private static final Double UPDATED_NSC = 2D;

    private static final String DEFAULT_COMPETENCE = "AAAAAAAAAA";
    private static final String UPDATED_COMPETENCE = "BBBBBBBBBB";

    private static final String DEFAULT_DIPLOME = "AAAAAAAAAA";
    private static final String UPDATED_DIPLOME = "BBBBBBBBBB";

    private static final String DEFAULT_EXPERIENCE = "AAAAAAAAAA";
    private static final String UPDATED_EXPERIENCE = "BBBBBBBBBB";

    private static final String DEFAULT_APTPHY = "AAAAAAAAAA";
    private static final String UPDATED_APTPHY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_CV = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CV = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CV_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CV_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ETAT_DOC = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_DOC = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SIGNATURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SIGNATURE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_SIGNATURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SIGNATURE_CONTENT_TYPE = "image/png";

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EmployeSearchRepository employeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployeMockMvc;

    private Employe employe;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeResource employeResource = new EmployeResource(employeRepository, employeSearchRepository);
        this.restEmployeMockMvc = MockMvcBuilders.standaloneSetup(employeResource)
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
    public static Employe createEntity(EntityManager em) {
        Employe employe = new Employe()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateN(DEFAULT_DATE_N)
            .cin(DEFAULT_CIN)
            .sexe(DEFAULT_SEXE)
            .adress(DEFAULT_ADRESS)
            .gsm(DEFAULT_GSM)
            .lieuNais(DEFAULT_LIEU_NAIS)
            .telMais(DEFAULT_TEL_MAIS)
            .teleph(DEFAULT_TELEPH)
            .delivrele(DEFAULT_DELIVRELE)
            .delivrea(DEFAULT_DELIVREA)
            .email(DEFAULT_EMAIL)
            .matricule(DEFAULT_MATRICULE)
            .email2(DEFAULT_EMAIL_2)
            .grade(DEFAULT_GRADE)
            .rib(DEFAULT_RIB)
            .nsc(DEFAULT_NSC)
            .competence(DEFAULT_COMPETENCE)
            .diplome(DEFAULT_DIPLOME)
            .experience(DEFAULT_EXPERIENCE)
            .aptphy(DEFAULT_APTPHY)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .cv(DEFAULT_CV)
            .cvContentType(DEFAULT_CV_CONTENT_TYPE)
            .etatDoc(DEFAULT_ETAT_DOC)
            .signature(DEFAULT_SIGNATURE)
            .signatureContentType(DEFAULT_SIGNATURE_CONTENT_TYPE);
        return employe;
    }

    @Before
    public void initTest() {
        employeSearchRepository.deleteAll();
        employe = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmploye() throws Exception {
        int databaseSizeBeforeCreate = employeRepository.findAll().size();

        // Create the Employe
        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isCreated());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeCreate + 1);
        Employe testEmploye = employeList.get(employeList.size() - 1);
        assertThat(testEmploye.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEmploye.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEmploye.getDateN()).isEqualTo(DEFAULT_DATE_N);
        assertThat(testEmploye.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testEmploye.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEmploye.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testEmploye.getGsm()).isEqualTo(DEFAULT_GSM);
        assertThat(testEmploye.getLieuNais()).isEqualTo(DEFAULT_LIEU_NAIS);
        assertThat(testEmploye.getTelMais()).isEqualTo(DEFAULT_TEL_MAIS);
        assertThat(testEmploye.getTeleph()).isEqualTo(DEFAULT_TELEPH);
        assertThat(testEmploye.getDelivrele()).isEqualTo(DEFAULT_DELIVRELE);
        assertThat(testEmploye.getDelivrea()).isEqualTo(DEFAULT_DELIVREA);
        assertThat(testEmploye.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmploye.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testEmploye.getEmail2()).isEqualTo(DEFAULT_EMAIL_2);
        assertThat(testEmploye.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testEmploye.getRib()).isEqualTo(DEFAULT_RIB);
        assertThat(testEmploye.getNsc()).isEqualTo(DEFAULT_NSC);
        assertThat(testEmploye.getCompetence()).isEqualTo(DEFAULT_COMPETENCE);
        assertThat(testEmploye.getDiplome()).isEqualTo(DEFAULT_DIPLOME);
        assertThat(testEmploye.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testEmploye.getAptphy()).isEqualTo(DEFAULT_APTPHY);
        assertThat(testEmploye.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testEmploye.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testEmploye.getCv()).isEqualTo(DEFAULT_CV);
        assertThat(testEmploye.getCvContentType()).isEqualTo(DEFAULT_CV_CONTENT_TYPE);
        assertThat(testEmploye.getEtatDoc()).isEqualTo(DEFAULT_ETAT_DOC);
        assertThat(testEmploye.getSignature()).isEqualTo(DEFAULT_SIGNATURE);
        assertThat(testEmploye.getSignatureContentType()).isEqualTo(DEFAULT_SIGNATURE_CONTENT_TYPE);

        // Validate the Employe in Elasticsearch
        Employe employeEs = employeSearchRepository.findOne(testEmploye.getId());
        assertThat(employeEs).isEqualToComparingFieldByField(testEmploye);
    }

    @Test
    @Transactional
    public void createEmployeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeRepository.findAll().size();

        // Create the Employe with an existing ID
        employe.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeRepository.findAll().size();
        // set the field null
        employe.setNom(null);

        // Create the Employe, which fails.

        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isBadRequest());

        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeRepository.findAll().size();
        // set the field null
        employe.setPrenom(null);

        // Create the Employe, which fails.

        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isBadRequest());

        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateNIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeRepository.findAll().size();
        // set the field null
        employe.setDateN(null);

        // Create the Employe, which fails.

        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isBadRequest());

        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCinIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeRepository.findAll().size();
        // set the field null
        employe.setCin(null);

        // Create the Employe, which fails.

        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isBadRequest());

        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGsmIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeRepository.findAll().size();
        // set the field null
        employe.setGsm(null);

        // Create the Employe, which fails.

        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isBadRequest());

        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeRepository.findAll().size();
        // set the field null
        employe.setEmail(null);

        // Create the Employe, which fails.

        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isBadRequest());

        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployes() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        // Get all the employeList
        restEmployeMockMvc.perform(get("/api/employes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].dateN").value(hasItem(sameInstant(DEFAULT_DATE_N))))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN.doubleValue())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
            .andExpect(jsonPath("$.[*].gsm").value(hasItem(DEFAULT_GSM.doubleValue())))
            .andExpect(jsonPath("$.[*].lieuNais").value(hasItem(DEFAULT_LIEU_NAIS.toString())))
            .andExpect(jsonPath("$.[*].telMais").value(hasItem(DEFAULT_TEL_MAIS.doubleValue())))
            .andExpect(jsonPath("$.[*].teleph").value(hasItem(DEFAULT_TELEPH.doubleValue())))
            .andExpect(jsonPath("$.[*].delivrele").value(hasItem(sameInstant(DEFAULT_DELIVRELE))))
            .andExpect(jsonPath("$.[*].delivrea").value(hasItem(DEFAULT_DELIVREA.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.doubleValue())))
            .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())))
            .andExpect(jsonPath("$.[*].rib").value(hasItem(DEFAULT_RIB.doubleValue())))
            .andExpect(jsonPath("$.[*].nsc").value(hasItem(DEFAULT_NSC.doubleValue())))
            .andExpect(jsonPath("$.[*].competence").value(hasItem(DEFAULT_COMPETENCE.toString())))
            .andExpect(jsonPath("$.[*].diplome").value(hasItem(DEFAULT_DIPLOME.toString())))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE.toString())))
            .andExpect(jsonPath("$.[*].aptphy").value(hasItem(DEFAULT_APTPHY.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].cvContentType").value(hasItem(DEFAULT_CV_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cv").value(hasItem(Base64Utils.encodeToString(DEFAULT_CV))))
            .andExpect(jsonPath("$.[*].etatDoc").value(hasItem(DEFAULT_ETAT_DOC.toString())))
            .andExpect(jsonPath("$.[*].signatureContentType").value(hasItem(DEFAULT_SIGNATURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].signature").value(hasItem(Base64Utils.encodeToString(DEFAULT_SIGNATURE))));
    }

    @Test
    @Transactional
    public void getEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        // Get the employe
        restEmployeMockMvc.perform(get("/api/employes/{id}", employe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employe.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.dateN").value(sameInstant(DEFAULT_DATE_N)))
            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN.doubleValue()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS.toString()))
            .andExpect(jsonPath("$.gsm").value(DEFAULT_GSM.doubleValue()))
            .andExpect(jsonPath("$.lieuNais").value(DEFAULT_LIEU_NAIS.toString()))
            .andExpect(jsonPath("$.telMais").value(DEFAULT_TEL_MAIS.doubleValue()))
            .andExpect(jsonPath("$.teleph").value(DEFAULT_TELEPH.doubleValue()))
            .andExpect(jsonPath("$.delivrele").value(sameInstant(DEFAULT_DELIVRELE)))
            .andExpect(jsonPath("$.delivrea").value(DEFAULT_DELIVREA.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE.doubleValue()))
            .andExpect(jsonPath("$.email2").value(DEFAULT_EMAIL_2.toString()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.toString()))
            .andExpect(jsonPath("$.rib").value(DEFAULT_RIB.doubleValue()))
            .andExpect(jsonPath("$.nsc").value(DEFAULT_NSC.doubleValue()))
            .andExpect(jsonPath("$.competence").value(DEFAULT_COMPETENCE.toString()))
            .andExpect(jsonPath("$.diplome").value(DEFAULT_DIPLOME.toString()))
            .andExpect(jsonPath("$.experience").value(DEFAULT_EXPERIENCE.toString()))
            .andExpect(jsonPath("$.aptphy").value(DEFAULT_APTPHY.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.cvContentType").value(DEFAULT_CV_CONTENT_TYPE))
            .andExpect(jsonPath("$.cv").value(Base64Utils.encodeToString(DEFAULT_CV)))
            .andExpect(jsonPath("$.etatDoc").value(DEFAULT_ETAT_DOC.toString()))
            .andExpect(jsonPath("$.signatureContentType").value(DEFAULT_SIGNATURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.signature").value(Base64Utils.encodeToString(DEFAULT_SIGNATURE)));
    }

    @Test
    @Transactional
    public void getNonExistingEmploye() throws Exception {
        // Get the employe
        restEmployeMockMvc.perform(get("/api/employes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);
        employeSearchRepository.save(employe);
        int databaseSizeBeforeUpdate = employeRepository.findAll().size();

        // Update the employe
        Employe updatedEmploye = employeRepository.findOne(employe.getId());
        updatedEmploye
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateN(UPDATED_DATE_N)
            .cin(UPDATED_CIN)
            .sexe(UPDATED_SEXE)
            .adress(UPDATED_ADRESS)
            .gsm(UPDATED_GSM)
            .lieuNais(UPDATED_LIEU_NAIS)
            .telMais(UPDATED_TEL_MAIS)
            .teleph(UPDATED_TELEPH)
            .delivrele(UPDATED_DELIVRELE)
            .delivrea(UPDATED_DELIVREA)
            .email(UPDATED_EMAIL)
            .matricule(UPDATED_MATRICULE)
            .email2(UPDATED_EMAIL_2)
            .grade(UPDATED_GRADE)
            .rib(UPDATED_RIB)
            .nsc(UPDATED_NSC)
            .competence(UPDATED_COMPETENCE)
            .diplome(UPDATED_DIPLOME)
            .experience(UPDATED_EXPERIENCE)
            .aptphy(UPDATED_APTPHY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .cv(UPDATED_CV)
            .cvContentType(UPDATED_CV_CONTENT_TYPE)
            .etatDoc(UPDATED_ETAT_DOC)
            .signature(UPDATED_SIGNATURE)
            .signatureContentType(UPDATED_SIGNATURE_CONTENT_TYPE);

        restEmployeMockMvc.perform(put("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmploye)))
            .andExpect(status().isOk());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeUpdate);
        Employe testEmploye = employeList.get(employeList.size() - 1);
        assertThat(testEmploye.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEmploye.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEmploye.getDateN()).isEqualTo(UPDATED_DATE_N);
        assertThat(testEmploye.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testEmploye.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEmploye.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testEmploye.getGsm()).isEqualTo(UPDATED_GSM);
        assertThat(testEmploye.getLieuNais()).isEqualTo(UPDATED_LIEU_NAIS);
        assertThat(testEmploye.getTelMais()).isEqualTo(UPDATED_TEL_MAIS);
        assertThat(testEmploye.getTeleph()).isEqualTo(UPDATED_TELEPH);
        assertThat(testEmploye.getDelivrele()).isEqualTo(UPDATED_DELIVRELE);
        assertThat(testEmploye.getDelivrea()).isEqualTo(UPDATED_DELIVREA);
        assertThat(testEmploye.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmploye.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testEmploye.getEmail2()).isEqualTo(UPDATED_EMAIL_2);
        assertThat(testEmploye.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testEmploye.getRib()).isEqualTo(UPDATED_RIB);
        assertThat(testEmploye.getNsc()).isEqualTo(UPDATED_NSC);
        assertThat(testEmploye.getCompetence()).isEqualTo(UPDATED_COMPETENCE);
        assertThat(testEmploye.getDiplome()).isEqualTo(UPDATED_DIPLOME);
        assertThat(testEmploye.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testEmploye.getAptphy()).isEqualTo(UPDATED_APTPHY);
        assertThat(testEmploye.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testEmploye.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testEmploye.getCv()).isEqualTo(UPDATED_CV);
        assertThat(testEmploye.getCvContentType()).isEqualTo(UPDATED_CV_CONTENT_TYPE);
        assertThat(testEmploye.getEtatDoc()).isEqualTo(UPDATED_ETAT_DOC);
        assertThat(testEmploye.getSignature()).isEqualTo(UPDATED_SIGNATURE);
        assertThat(testEmploye.getSignatureContentType()).isEqualTo(UPDATED_SIGNATURE_CONTENT_TYPE);

        // Validate the Employe in Elasticsearch
        Employe employeEs = employeSearchRepository.findOne(testEmploye.getId());
        assertThat(employeEs).isEqualToComparingFieldByField(testEmploye);
    }

    @Test
    @Transactional
    public void updateNonExistingEmploye() throws Exception {
        int databaseSizeBeforeUpdate = employeRepository.findAll().size();

        // Create the Employe

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmployeMockMvc.perform(put("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isCreated());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);
        employeSearchRepository.save(employe);
        int databaseSizeBeforeDelete = employeRepository.findAll().size();

        // Get the employe
        restEmployeMockMvc.perform(delete("/api/employes/{id}", employe.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean employeExistsInEs = employeSearchRepository.exists(employe.getId());
        assertThat(employeExistsInEs).isFalse();

        // Validate the database is empty
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);
        employeSearchRepository.save(employe);

        // Search the employe
        restEmployeMockMvc.perform(get("/api/_search/employes?query=id:" + employe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].dateN").value(hasItem(sameInstant(DEFAULT_DATE_N))))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN.doubleValue())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
            .andExpect(jsonPath("$.[*].gsm").value(hasItem(DEFAULT_GSM.doubleValue())))
            .andExpect(jsonPath("$.[*].lieuNais").value(hasItem(DEFAULT_LIEU_NAIS.toString())))
            .andExpect(jsonPath("$.[*].telMais").value(hasItem(DEFAULT_TEL_MAIS.doubleValue())))
            .andExpect(jsonPath("$.[*].teleph").value(hasItem(DEFAULT_TELEPH.doubleValue())))
            .andExpect(jsonPath("$.[*].delivrele").value(hasItem(sameInstant(DEFAULT_DELIVRELE))))
            .andExpect(jsonPath("$.[*].delivrea").value(hasItem(DEFAULT_DELIVREA.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.doubleValue())))
            .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())))
            .andExpect(jsonPath("$.[*].rib").value(hasItem(DEFAULT_RIB.doubleValue())))
            .andExpect(jsonPath("$.[*].nsc").value(hasItem(DEFAULT_NSC.doubleValue())))
            .andExpect(jsonPath("$.[*].competence").value(hasItem(DEFAULT_COMPETENCE.toString())))
            .andExpect(jsonPath("$.[*].diplome").value(hasItem(DEFAULT_DIPLOME.toString())))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE.toString())))
            .andExpect(jsonPath("$.[*].aptphy").value(hasItem(DEFAULT_APTPHY.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].cvContentType").value(hasItem(DEFAULT_CV_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cv").value(hasItem(Base64Utils.encodeToString(DEFAULT_CV))))
            .andExpect(jsonPath("$.[*].etatDoc").value(hasItem(DEFAULT_ETAT_DOC.toString())))
            .andExpect(jsonPath("$.[*].signatureContentType").value(hasItem(DEFAULT_SIGNATURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].signature").value(hasItem(Base64Utils.encodeToString(DEFAULT_SIGNATURE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employe.class);
    }
}
