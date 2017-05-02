package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Calendrier;
import org.qualimaker.repository.CalendrierRepository;
import org.qualimaker.repository.search.CalendrierSearchRepository;
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
 * Test class for the CalendrierResource REST controller.
 *
 * @see CalendrierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class CalendrierResourceIntTest {

    @Autowired
    private CalendrierRepository calendrierRepository;

    @Autowired
    private CalendrierSearchRepository calendrierSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCalendrierMockMvc;

    private Calendrier calendrier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CalendrierResource calendrierResource = new CalendrierResource(calendrierRepository, calendrierSearchRepository);
        this.restCalendrierMockMvc = MockMvcBuilders.standaloneSetup(calendrierResource)
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
    public static Calendrier createEntity(EntityManager em) {
        Calendrier calendrier = new Calendrier();
        return calendrier;
    }

    @Before
    public void initTest() {
        calendrierSearchRepository.deleteAll();
        calendrier = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalendrier() throws Exception {
        int databaseSizeBeforeCreate = calendrierRepository.findAll().size();

        // Create the Calendrier
        restCalendrierMockMvc.perform(post("/api/calendriers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calendrier)))
            .andExpect(status().isCreated());

        // Validate the Calendrier in the database
        List<Calendrier> calendrierList = calendrierRepository.findAll();
        assertThat(calendrierList).hasSize(databaseSizeBeforeCreate + 1);
        Calendrier testCalendrier = calendrierList.get(calendrierList.size() - 1);

        // Validate the Calendrier in Elasticsearch
        Calendrier calendrierEs = calendrierSearchRepository.findOne(testCalendrier.getId());
        assertThat(calendrierEs).isEqualToComparingFieldByField(testCalendrier);
    }

    @Test
    @Transactional
    public void createCalendrierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calendrierRepository.findAll().size();

        // Create the Calendrier with an existing ID
        calendrier.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendrierMockMvc.perform(post("/api/calendriers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calendrier)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Calendrier> calendrierList = calendrierRepository.findAll();
        assertThat(calendrierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCalendriers() throws Exception {
        // Initialize the database
        calendrierRepository.saveAndFlush(calendrier);

        // Get all the calendrierList
        restCalendrierMockMvc.perform(get("/api/calendriers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendrier.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCalendrier() throws Exception {
        // Initialize the database
        calendrierRepository.saveAndFlush(calendrier);

        // Get the calendrier
        restCalendrierMockMvc.perform(get("/api/calendriers/{id}", calendrier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(calendrier.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCalendrier() throws Exception {
        // Get the calendrier
        restCalendrierMockMvc.perform(get("/api/calendriers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalendrier() throws Exception {
        // Initialize the database
        calendrierRepository.saveAndFlush(calendrier);
        calendrierSearchRepository.save(calendrier);
        int databaseSizeBeforeUpdate = calendrierRepository.findAll().size();

        // Update the calendrier
        Calendrier updatedCalendrier = calendrierRepository.findOne(calendrier.getId());

        restCalendrierMockMvc.perform(put("/api/calendriers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCalendrier)))
            .andExpect(status().isOk());

        // Validate the Calendrier in the database
        List<Calendrier> calendrierList = calendrierRepository.findAll();
        assertThat(calendrierList).hasSize(databaseSizeBeforeUpdate);
        Calendrier testCalendrier = calendrierList.get(calendrierList.size() - 1);

        // Validate the Calendrier in Elasticsearch
        Calendrier calendrierEs = calendrierSearchRepository.findOne(testCalendrier.getId());
        assertThat(calendrierEs).isEqualToComparingFieldByField(testCalendrier);
    }

    @Test
    @Transactional
    public void updateNonExistingCalendrier() throws Exception {
        int databaseSizeBeforeUpdate = calendrierRepository.findAll().size();

        // Create the Calendrier

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCalendrierMockMvc.perform(put("/api/calendriers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calendrier)))
            .andExpect(status().isCreated());

        // Validate the Calendrier in the database
        List<Calendrier> calendrierList = calendrierRepository.findAll();
        assertThat(calendrierList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCalendrier() throws Exception {
        // Initialize the database
        calendrierRepository.saveAndFlush(calendrier);
        calendrierSearchRepository.save(calendrier);
        int databaseSizeBeforeDelete = calendrierRepository.findAll().size();

        // Get the calendrier
        restCalendrierMockMvc.perform(delete("/api/calendriers/{id}", calendrier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean calendrierExistsInEs = calendrierSearchRepository.exists(calendrier.getId());
        assertThat(calendrierExistsInEs).isFalse();

        // Validate the database is empty
        List<Calendrier> calendrierList = calendrierRepository.findAll();
        assertThat(calendrierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCalendrier() throws Exception {
        // Initialize the database
        calendrierRepository.saveAndFlush(calendrier);
        calendrierSearchRepository.save(calendrier);

        // Search the calendrier
        restCalendrierMockMvc.perform(get("/api/_search/calendriers?query=id:" + calendrier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendrier.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Calendrier.class);
    }
}
