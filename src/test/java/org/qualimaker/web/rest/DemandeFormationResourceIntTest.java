package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.DemandeFormation;
import org.qualimaker.repository.DemandeFormationRepository;
import org.qualimaker.repository.search.DemandeFormationSearchRepository;
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
 * Test class for the DemandeFormationResource REST controller.
 *
 * @see DemandeFormationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class DemandeFormationResourceIntTest {

    private static final String DEFAULT_THEME = "AAAAAAAAAA";
    private static final String UPDATED_THEME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_DEMANDE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEMANDE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATESOUHAITE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATESOUHAITE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NOMBRESJOURS = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRESJOURS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_JUSTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICATION = "BBBBBBBBBB";

    @Autowired
    private DemandeFormationRepository demandeFormationRepository;

    @Autowired
    private DemandeFormationSearchRepository demandeFormationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDemandeFormationMockMvc;

    private DemandeFormation demandeFormation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DemandeFormationResource demandeFormationResource = new DemandeFormationResource(demandeFormationRepository, demandeFormationSearchRepository);
        this.restDemandeFormationMockMvc = MockMvcBuilders.standaloneSetup(demandeFormationResource)
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
    public static DemandeFormation createEntity(EntityManager em) {
        DemandeFormation demandeFormation = new DemandeFormation()
            .theme(DEFAULT_THEME)
            .dateDemande(DEFAULT_DATE_DEMANDE)
            .datesouhaite(DEFAULT_DATESOUHAITE)
            .nombresjours(DEFAULT_NOMBRESJOURS)
            .description(DEFAULT_DESCRIPTION)
            .justification(DEFAULT_JUSTIFICATION);
        return demandeFormation;
    }

    @Before
    public void initTest() {
        demandeFormationSearchRepository.deleteAll();
        demandeFormation = createEntity(em);
    }

    @Test
    @Transactional
    public void createDemandeFormation() throws Exception {
        int databaseSizeBeforeCreate = demandeFormationRepository.findAll().size();

        // Create the DemandeFormation
        restDemandeFormationMockMvc.perform(post("/api/demande-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demandeFormation)))
            .andExpect(status().isCreated());

        // Validate the DemandeFormation in the database
        List<DemandeFormation> demandeFormationList = demandeFormationRepository.findAll();
        assertThat(demandeFormationList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeFormation testDemandeFormation = demandeFormationList.get(demandeFormationList.size() - 1);
        assertThat(testDemandeFormation.getTheme()).isEqualTo(DEFAULT_THEME);
        assertThat(testDemandeFormation.getDateDemande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testDemandeFormation.getDatesouhaite()).isEqualTo(DEFAULT_DATESOUHAITE);
        assertThat(testDemandeFormation.getNombresjours()).isEqualTo(DEFAULT_NOMBRESJOURS);
        assertThat(testDemandeFormation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDemandeFormation.getJustification()).isEqualTo(DEFAULT_JUSTIFICATION);

        // Validate the DemandeFormation in Elasticsearch
        DemandeFormation demandeFormationEs = demandeFormationSearchRepository.findOne(testDemandeFormation.getId());
        assertThat(demandeFormationEs).isEqualToComparingFieldByField(testDemandeFormation);
    }

    @Test
    @Transactional
    public void createDemandeFormationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = demandeFormationRepository.findAll().size();

        // Create the DemandeFormation with an existing ID
        demandeFormation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeFormationMockMvc.perform(post("/api/demande-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demandeFormation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DemandeFormation> demandeFormationList = demandeFormationRepository.findAll();
        assertThat(demandeFormationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDemandeFormations() throws Exception {
        // Initialize the database
        demandeFormationRepository.saveAndFlush(demandeFormation);

        // Get all the demandeFormationList
        restDemandeFormationMockMvc.perform(get("/api/demande-formations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME.toString())))
            .andExpect(jsonPath("$.[*].dateDemande").value(hasItem(sameInstant(DEFAULT_DATE_DEMANDE))))
            .andExpect(jsonPath("$.[*].datesouhaite").value(hasItem(sameInstant(DEFAULT_DATESOUHAITE))))
            .andExpect(jsonPath("$.[*].nombresjours").value(hasItem(DEFAULT_NOMBRESJOURS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].justification").value(hasItem(DEFAULT_JUSTIFICATION.toString())));
    }

    @Test
    @Transactional
    public void getDemandeFormation() throws Exception {
        // Initialize the database
        demandeFormationRepository.saveAndFlush(demandeFormation);

        // Get the demandeFormation
        restDemandeFormationMockMvc.perform(get("/api/demande-formations/{id}", demandeFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(demandeFormation.getId().intValue()))
            .andExpect(jsonPath("$.theme").value(DEFAULT_THEME.toString()))
            .andExpect(jsonPath("$.dateDemande").value(sameInstant(DEFAULT_DATE_DEMANDE)))
            .andExpect(jsonPath("$.datesouhaite").value(sameInstant(DEFAULT_DATESOUHAITE)))
            .andExpect(jsonPath("$.nombresjours").value(DEFAULT_NOMBRESJOURS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.justification").value(DEFAULT_JUSTIFICATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDemandeFormation() throws Exception {
        // Get the demandeFormation
        restDemandeFormationMockMvc.perform(get("/api/demande-formations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDemandeFormation() throws Exception {
        // Initialize the database
        demandeFormationRepository.saveAndFlush(demandeFormation);
        demandeFormationSearchRepository.save(demandeFormation);
        int databaseSizeBeforeUpdate = demandeFormationRepository.findAll().size();

        // Update the demandeFormation
        DemandeFormation updatedDemandeFormation = demandeFormationRepository.findOne(demandeFormation.getId());
        updatedDemandeFormation
            .theme(UPDATED_THEME)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .datesouhaite(UPDATED_DATESOUHAITE)
            .nombresjours(UPDATED_NOMBRESJOURS)
            .description(UPDATED_DESCRIPTION)
            .justification(UPDATED_JUSTIFICATION);

        restDemandeFormationMockMvc.perform(put("/api/demande-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDemandeFormation)))
            .andExpect(status().isOk());

        // Validate the DemandeFormation in the database
        List<DemandeFormation> demandeFormationList = demandeFormationRepository.findAll();
        assertThat(demandeFormationList).hasSize(databaseSizeBeforeUpdate);
        DemandeFormation testDemandeFormation = demandeFormationList.get(demandeFormationList.size() - 1);
        assertThat(testDemandeFormation.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testDemandeFormation.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testDemandeFormation.getDatesouhaite()).isEqualTo(UPDATED_DATESOUHAITE);
        assertThat(testDemandeFormation.getNombresjours()).isEqualTo(UPDATED_NOMBRESJOURS);
        assertThat(testDemandeFormation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDemandeFormation.getJustification()).isEqualTo(UPDATED_JUSTIFICATION);

        // Validate the DemandeFormation in Elasticsearch
        DemandeFormation demandeFormationEs = demandeFormationSearchRepository.findOne(testDemandeFormation.getId());
        assertThat(demandeFormationEs).isEqualToComparingFieldByField(testDemandeFormation);
    }

    @Test
    @Transactional
    public void updateNonExistingDemandeFormation() throws Exception {
        int databaseSizeBeforeUpdate = demandeFormationRepository.findAll().size();

        // Create the DemandeFormation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDemandeFormationMockMvc.perform(put("/api/demande-formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demandeFormation)))
            .andExpect(status().isCreated());

        // Validate the DemandeFormation in the database
        List<DemandeFormation> demandeFormationList = demandeFormationRepository.findAll();
        assertThat(demandeFormationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDemandeFormation() throws Exception {
        // Initialize the database
        demandeFormationRepository.saveAndFlush(demandeFormation);
        demandeFormationSearchRepository.save(demandeFormation);
        int databaseSizeBeforeDelete = demandeFormationRepository.findAll().size();

        // Get the demandeFormation
        restDemandeFormationMockMvc.perform(delete("/api/demande-formations/{id}", demandeFormation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean demandeFormationExistsInEs = demandeFormationSearchRepository.exists(demandeFormation.getId());
        assertThat(demandeFormationExistsInEs).isFalse();

        // Validate the database is empty
        List<DemandeFormation> demandeFormationList = demandeFormationRepository.findAll();
        assertThat(demandeFormationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDemandeFormation() throws Exception {
        // Initialize the database
        demandeFormationRepository.saveAndFlush(demandeFormation);
        demandeFormationSearchRepository.save(demandeFormation);

        // Search the demandeFormation
        restDemandeFormationMockMvc.perform(get("/api/_search/demande-formations?query=id:" + demandeFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME.toString())))
            .andExpect(jsonPath("$.[*].dateDemande").value(hasItem(sameInstant(DEFAULT_DATE_DEMANDE))))
            .andExpect(jsonPath("$.[*].datesouhaite").value(hasItem(sameInstant(DEFAULT_DATESOUHAITE))))
            .andExpect(jsonPath("$.[*].nombresjours").value(hasItem(DEFAULT_NOMBRESJOURS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].justification").value(hasItem(DEFAULT_JUSTIFICATION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeFormation.class);
    }
}
