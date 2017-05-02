package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Integre;
import org.qualimaker.domain.Employe;
import org.qualimaker.domain.Employe;
import org.qualimaker.repository.IntegreRepository;
import org.qualimaker.repository.search.IntegreSearchRepository;
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
 * Test class for the IntegreResource REST controller.
 *
 * @see IntegreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class IntegreResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_D = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_D = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_F = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_F = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_POINTFORT = "AAAAAAAAAA";
    private static final String UPDATED_POINTFORT = "BBBBBBBBBB";

    private static final String DEFAULT_POITAIBL = "AAAAAAAAAA";
    private static final String UPDATED_POITAIBL = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    @Autowired
    private IntegreRepository integreRepository;

    @Autowired
    private IntegreSearchRepository integreSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIntegreMockMvc;

    private Integre integre;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IntegreResource integreResource = new IntegreResource(integreRepository, integreSearchRepository);
        this.restIntegreMockMvc = MockMvcBuilders.standaloneSetup(integreResource)
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
    public static Integre createEntity(EntityManager em) {
        Integre integre = new Integre()
            .dateD(DEFAULT_DATE_D)
            .dateF(DEFAULT_DATE_F)
            .pointfort(DEFAULT_POINTFORT)
            .poitaibl(DEFAULT_POITAIBL)
            .info(DEFAULT_INFO)
            .etat(DEFAULT_ETAT);
        // Add required entity
        Employe employe = EmployeResourceIntTest.createEntity(em);
        em.persist(employe);
        em.flush();
        integre.setEmploye(employe);
        // Add required entity
        Employe responsable = EmployeResourceIntTest.createEntity(em);
        em.persist(responsable);
        em.flush();
        integre.setResponsable(responsable);
        return integre;
    }

    @Before
    public void initTest() {
        integreSearchRepository.deleteAll();
        integre = createEntity(em);
    }

    @Test
    @Transactional
    public void createIntegre() throws Exception {
        int databaseSizeBeforeCreate = integreRepository.findAll().size();

        // Create the Integre
        restIntegreMockMvc.perform(post("/api/integres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integre)))
            .andExpect(status().isCreated());

        // Validate the Integre in the database
        List<Integre> integreList = integreRepository.findAll();
        assertThat(integreList).hasSize(databaseSizeBeforeCreate + 1);
        Integre testIntegre = integreList.get(integreList.size() - 1);
        assertThat(testIntegre.getDateD()).isEqualTo(DEFAULT_DATE_D);
        assertThat(testIntegre.getDateF()).isEqualTo(DEFAULT_DATE_F);
        assertThat(testIntegre.getPointfort()).isEqualTo(DEFAULT_POINTFORT);
        assertThat(testIntegre.getPoitaibl()).isEqualTo(DEFAULT_POITAIBL);
        assertThat(testIntegre.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testIntegre.getEtat()).isEqualTo(DEFAULT_ETAT);

        // Validate the Integre in Elasticsearch
        Integre integreEs = integreSearchRepository.findOne(testIntegre.getId());
        assertThat(integreEs).isEqualToComparingFieldByField(testIntegre);
    }

    @Test
    @Transactional
    public void createIntegreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = integreRepository.findAll().size();

        // Create the Integre with an existing ID
        integre.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntegreMockMvc.perform(post("/api/integres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integre)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Integre> integreList = integreRepository.findAll();
        assertThat(integreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIntegres() throws Exception {
        // Initialize the database
        integreRepository.saveAndFlush(integre);

        // Get all the integreList
        restIntegreMockMvc.perform(get("/api/integres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integre.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateD").value(hasItem(sameInstant(DEFAULT_DATE_D))))
            .andExpect(jsonPath("$.[*].dateF").value(hasItem(sameInstant(DEFAULT_DATE_F))))
            .andExpect(jsonPath("$.[*].pointfort").value(hasItem(DEFAULT_POINTFORT.toString())))
            .andExpect(jsonPath("$.[*].poitaibl").value(hasItem(DEFAULT_POITAIBL.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    public void getIntegre() throws Exception {
        // Initialize the database
        integreRepository.saveAndFlush(integre);

        // Get the integre
        restIntegreMockMvc.perform(get("/api/integres/{id}", integre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(integre.getId().intValue()))
            .andExpect(jsonPath("$.dateD").value(sameInstant(DEFAULT_DATE_D)))
            .andExpect(jsonPath("$.dateF").value(sameInstant(DEFAULT_DATE_F)))
            .andExpect(jsonPath("$.pointfort").value(DEFAULT_POINTFORT.toString()))
            .andExpect(jsonPath("$.poitaibl").value(DEFAULT_POITAIBL.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIntegre() throws Exception {
        // Get the integre
        restIntegreMockMvc.perform(get("/api/integres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIntegre() throws Exception {
        // Initialize the database
        integreRepository.saveAndFlush(integre);
        integreSearchRepository.save(integre);
        int databaseSizeBeforeUpdate = integreRepository.findAll().size();

        // Update the integre
        Integre updatedIntegre = integreRepository.findOne(integre.getId());
        updatedIntegre
            .dateD(UPDATED_DATE_D)
            .dateF(UPDATED_DATE_F)
            .pointfort(UPDATED_POINTFORT)
            .poitaibl(UPDATED_POITAIBL)
            .info(UPDATED_INFO)
            .etat(UPDATED_ETAT);

        restIntegreMockMvc.perform(put("/api/integres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIntegre)))
            .andExpect(status().isOk());

        // Validate the Integre in the database
        List<Integre> integreList = integreRepository.findAll();
        assertThat(integreList).hasSize(databaseSizeBeforeUpdate);
        Integre testIntegre = integreList.get(integreList.size() - 1);
        assertThat(testIntegre.getDateD()).isEqualTo(UPDATED_DATE_D);
        assertThat(testIntegre.getDateF()).isEqualTo(UPDATED_DATE_F);
        assertThat(testIntegre.getPointfort()).isEqualTo(UPDATED_POINTFORT);
        assertThat(testIntegre.getPoitaibl()).isEqualTo(UPDATED_POITAIBL);
        assertThat(testIntegre.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testIntegre.getEtat()).isEqualTo(UPDATED_ETAT);

        // Validate the Integre in Elasticsearch
        Integre integreEs = integreSearchRepository.findOne(testIntegre.getId());
        assertThat(integreEs).isEqualToComparingFieldByField(testIntegre);
    }

    @Test
    @Transactional
    public void updateNonExistingIntegre() throws Exception {
        int databaseSizeBeforeUpdate = integreRepository.findAll().size();

        // Create the Integre

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIntegreMockMvc.perform(put("/api/integres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integre)))
            .andExpect(status().isCreated());

        // Validate the Integre in the database
        List<Integre> integreList = integreRepository.findAll();
        assertThat(integreList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIntegre() throws Exception {
        // Initialize the database
        integreRepository.saveAndFlush(integre);
        integreSearchRepository.save(integre);
        int databaseSizeBeforeDelete = integreRepository.findAll().size();

        // Get the integre
        restIntegreMockMvc.perform(delete("/api/integres/{id}", integre.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean integreExistsInEs = integreSearchRepository.exists(integre.getId());
        assertThat(integreExistsInEs).isFalse();

        // Validate the database is empty
        List<Integre> integreList = integreRepository.findAll();
        assertThat(integreList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIntegre() throws Exception {
        // Initialize the database
        integreRepository.saveAndFlush(integre);
        integreSearchRepository.save(integre);

        // Search the integre
        restIntegreMockMvc.perform(get("/api/_search/integres?query=id:" + integre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integre.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateD").value(hasItem(sameInstant(DEFAULT_DATE_D))))
            .andExpect(jsonPath("$.[*].dateF").value(hasItem(sameInstant(DEFAULT_DATE_F))))
            .andExpect(jsonPath("$.[*].pointfort").value(hasItem(DEFAULT_POINTFORT.toString())))
            .andExpect(jsonPath("$.[*].poitaibl").value(hasItem(DEFAULT_POITAIBL.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Integre.class);
    }
}
