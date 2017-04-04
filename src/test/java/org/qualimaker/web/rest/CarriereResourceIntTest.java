package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Carriere;
import org.qualimaker.repository.CarriereRepository;
import org.qualimaker.repository.search.CarriereSearchRepository;
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
 * Test class for the CarriereResource REST controller.
 *
 * @see CarriereResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class CarriereResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_INTEGRE = "AAAAAAAAAA";
    private static final String UPDATED_INTEGRE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DEBUT_I_NT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEBUT_I_NT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FIN_INT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FIN_INT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_REC = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_REC = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_FICH_CONT = "AAAAAAAAAA";
    private static final String UPDATED_FICH_CONT = "BBBBBBBBBB";

    @Autowired
    private CarriereRepository carriereRepository;

    @Autowired
    private CarriereSearchRepository carriereSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCarriereMockMvc;

    private Carriere carriere;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CarriereResource carriereResource = new CarriereResource(carriereRepository, carriereSearchRepository);
        this.restCarriereMockMvc = MockMvcBuilders.standaloneSetup(carriereResource)
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
    public static Carriere createEntity(EntityManager em) {
        Carriere carriere = new Carriere()
            .nom(DEFAULT_NOM)
            .etat(DEFAULT_ETAT)
            .integre(DEFAULT_INTEGRE)
            .debutINt(DEFAULT_DEBUT_I_NT)
            .finINT(DEFAULT_FIN_INT)
            .dateRec(DEFAULT_DATE_REC)
            .fichCont(DEFAULT_FICH_CONT);
        return carriere;
    }

    @Before
    public void initTest() {
        carriereSearchRepository.deleteAll();
        carriere = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarriere() throws Exception {
        int databaseSizeBeforeCreate = carriereRepository.findAll().size();

        // Create the Carriere
        restCarriereMockMvc.perform(post("/api/carrieres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carriere)))
            .andExpect(status().isCreated());

        // Validate the Carriere in the database
        List<Carriere> carriereList = carriereRepository.findAll();
        assertThat(carriereList).hasSize(databaseSizeBeforeCreate + 1);
        Carriere testCarriere = carriereList.get(carriereList.size() - 1);
        assertThat(testCarriere.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCarriere.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testCarriere.getIntegre()).isEqualTo(DEFAULT_INTEGRE);
        assertThat(testCarriere.getDebutINt()).isEqualTo(DEFAULT_DEBUT_I_NT);
        assertThat(testCarriere.getFinINT()).isEqualTo(DEFAULT_FIN_INT);
        assertThat(testCarriere.getDateRec()).isEqualTo(DEFAULT_DATE_REC);
        assertThat(testCarriere.getFichCont()).isEqualTo(DEFAULT_FICH_CONT);

        // Validate the Carriere in Elasticsearch
        Carriere carriereEs = carriereSearchRepository.findOne(testCarriere.getId());
        assertThat(carriereEs).isEqualToComparingFieldByField(testCarriere);
    }

    @Test
    @Transactional
    public void createCarriereWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carriereRepository.findAll().size();

        // Create the Carriere with an existing ID
        carriere.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarriereMockMvc.perform(post("/api/carrieres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carriere)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Carriere> carriereList = carriereRepository.findAll();
        assertThat(carriereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCarrieres() throws Exception {
        // Initialize the database
        carriereRepository.saveAndFlush(carriere);

        // Get all the carriereList
        restCarriereMockMvc.perform(get("/api/carrieres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carriere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].integre").value(hasItem(DEFAULT_INTEGRE.toString())))
            .andExpect(jsonPath("$.[*].debutINt").value(hasItem(sameInstant(DEFAULT_DEBUT_I_NT))))
            .andExpect(jsonPath("$.[*].finINT").value(hasItem(sameInstant(DEFAULT_FIN_INT))))
            .andExpect(jsonPath("$.[*].dateRec").value(hasItem(sameInstant(DEFAULT_DATE_REC))))
            .andExpect(jsonPath("$.[*].fichCont").value(hasItem(DEFAULT_FICH_CONT.toString())));
    }

    @Test
    @Transactional
    public void getCarriere() throws Exception {
        // Initialize the database
        carriereRepository.saveAndFlush(carriere);

        // Get the carriere
        restCarriereMockMvc.perform(get("/api/carrieres/{id}", carriere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carriere.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.integre").value(DEFAULT_INTEGRE.toString()))
            .andExpect(jsonPath("$.debutINt").value(sameInstant(DEFAULT_DEBUT_I_NT)))
            .andExpect(jsonPath("$.finINT").value(sameInstant(DEFAULT_FIN_INT)))
            .andExpect(jsonPath("$.dateRec").value(sameInstant(DEFAULT_DATE_REC)))
            .andExpect(jsonPath("$.fichCont").value(DEFAULT_FICH_CONT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarriere() throws Exception {
        // Get the carriere
        restCarriereMockMvc.perform(get("/api/carrieres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarriere() throws Exception {
        // Initialize the database
        carriereRepository.saveAndFlush(carriere);
        carriereSearchRepository.save(carriere);
        int databaseSizeBeforeUpdate = carriereRepository.findAll().size();

        // Update the carriere
        Carriere updatedCarriere = carriereRepository.findOne(carriere.getId());
        updatedCarriere
            .nom(UPDATED_NOM)
            .etat(UPDATED_ETAT)
            .integre(UPDATED_INTEGRE)
            .debutINt(UPDATED_DEBUT_I_NT)
            .finINT(UPDATED_FIN_INT)
            .dateRec(UPDATED_DATE_REC)
            .fichCont(UPDATED_FICH_CONT);

        restCarriereMockMvc.perform(put("/api/carrieres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCarriere)))
            .andExpect(status().isOk());

        // Validate the Carriere in the database
        List<Carriere> carriereList = carriereRepository.findAll();
        assertThat(carriereList).hasSize(databaseSizeBeforeUpdate);
        Carriere testCarriere = carriereList.get(carriereList.size() - 1);
        assertThat(testCarriere.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCarriere.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testCarriere.getIntegre()).isEqualTo(UPDATED_INTEGRE);
        assertThat(testCarriere.getDebutINt()).isEqualTo(UPDATED_DEBUT_I_NT);
        assertThat(testCarriere.getFinINT()).isEqualTo(UPDATED_FIN_INT);
        assertThat(testCarriere.getDateRec()).isEqualTo(UPDATED_DATE_REC);
        assertThat(testCarriere.getFichCont()).isEqualTo(UPDATED_FICH_CONT);

        // Validate the Carriere in Elasticsearch
        Carriere carriereEs = carriereSearchRepository.findOne(testCarriere.getId());
        assertThat(carriereEs).isEqualToComparingFieldByField(testCarriere);
    }

    @Test
    @Transactional
    public void updateNonExistingCarriere() throws Exception {
        int databaseSizeBeforeUpdate = carriereRepository.findAll().size();

        // Create the Carriere

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCarriereMockMvc.perform(put("/api/carrieres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carriere)))
            .andExpect(status().isCreated());

        // Validate the Carriere in the database
        List<Carriere> carriereList = carriereRepository.findAll();
        assertThat(carriereList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCarriere() throws Exception {
        // Initialize the database
        carriereRepository.saveAndFlush(carriere);
        carriereSearchRepository.save(carriere);
        int databaseSizeBeforeDelete = carriereRepository.findAll().size();

        // Get the carriere
        restCarriereMockMvc.perform(delete("/api/carrieres/{id}", carriere.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean carriereExistsInEs = carriereSearchRepository.exists(carriere.getId());
        assertThat(carriereExistsInEs).isFalse();

        // Validate the database is empty
        List<Carriere> carriereList = carriereRepository.findAll();
        assertThat(carriereList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCarriere() throws Exception {
        // Initialize the database
        carriereRepository.saveAndFlush(carriere);
        carriereSearchRepository.save(carriere);

        // Search the carriere
        restCarriereMockMvc.perform(get("/api/_search/carrieres?query=id:" + carriere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carriere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].integre").value(hasItem(DEFAULT_INTEGRE.toString())))
            .andExpect(jsonPath("$.[*].debutINt").value(hasItem(sameInstant(DEFAULT_DEBUT_I_NT))))
            .andExpect(jsonPath("$.[*].finINT").value(hasItem(sameInstant(DEFAULT_FIN_INT))))
            .andExpect(jsonPath("$.[*].dateRec").value(hasItem(sameInstant(DEFAULT_DATE_REC))))
            .andExpect(jsonPath("$.[*].fichCont").value(hasItem(DEFAULT_FICH_CONT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Carriere.class);
    }
}
