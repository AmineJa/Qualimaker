package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.repository.EventsRepository;
import org.qualimaker.repository.search.EventsSearchRepository;
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
 * Test class for the EventsResource REST controller.
 *
 * @see EventsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class EventsResourceIntTest {

    private static final ZonedDateTime DEFAULT_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EventsSearchRepository eventsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventsMockMvc;

    private Events events;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventsResource eventsResource = new EventsResource(eventsRepository, eventsSearchRepository);
        this.restEventsMockMvc = MockMvcBuilders.standaloneSetup(eventsResource)
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
    public static Events createEntity(EntityManager em) {
        Events events = new Events()
            .debut(DEFAULT_DEBUT)
            .fin(DEFAULT_FIN)
            .text(DEFAULT_TEXT);
        return events;
    }

    @Before
    public void initTest() {
        eventsSearchRepository.deleteAll();
        events = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvents() throws Exception {
        int databaseSizeBeforeCreate = eventsRepository.findAll().size();

        // Create the Events
        restEventsMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(events)))
            .andExpect(status().isCreated());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeCreate + 1);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getDebut()).isEqualTo(DEFAULT_DEBUT);
        assertThat(testEvents.getFin()).isEqualTo(DEFAULT_FIN);
        assertThat(testEvents.getText()).isEqualTo(DEFAULT_TEXT);

        // Validate the Events in Elasticsearch
        Events eventsEs = eventsSearchRepository.findOne(testEvents.getId());
        assertThat(eventsEs).isEqualToComparingFieldByField(testEvents);
    }

    @Test
    @Transactional
    public void createEventsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventsRepository.findAll().size();

        // Create the Events with an existing ID
        events.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventsMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(events)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList
        restEventsMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(events.getId().intValue())))
            .andExpect(jsonPath("$.[*].debut").value(hasItem(sameInstant(DEFAULT_DEBUT))))
            .andExpect(jsonPath("$.[*].fin").value(hasItem(sameInstant(DEFAULT_FIN))))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get the events
        restEventsMockMvc.perform(get("/api/events/{id}", events.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(events.getId().intValue()))
            .andExpect(jsonPath("$.debut").value(sameInstant(DEFAULT_DEBUT)))
            .andExpect(jsonPath("$.fin").value(sameInstant(DEFAULT_FIN)))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvents() throws Exception {
        // Get the events
        restEventsMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);
        eventsSearchRepository.save(events);
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();

        // Update the events
        Events updatedEvents = eventsRepository.findOne(events.getId());
        updatedEvents
            .debut(UPDATED_DEBUT)
            .fin(UPDATED_FIN)
            .text(UPDATED_TEXT);

        restEventsMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvents)))
            .andExpect(status().isOk());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getDebut()).isEqualTo(UPDATED_DEBUT);
        assertThat(testEvents.getFin()).isEqualTo(UPDATED_FIN);
        assertThat(testEvents.getText()).isEqualTo(UPDATED_TEXT);

        // Validate the Events in Elasticsearch
        Events eventsEs = eventsSearchRepository.findOne(testEvents.getId());
        assertThat(eventsEs).isEqualToComparingFieldByField(testEvents);
    }

    @Test
    @Transactional
    public void updateNonExistingEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();

        // Create the Events

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEventsMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(events)))
            .andExpect(status().isCreated());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);
        eventsSearchRepository.save(events);
        int databaseSizeBeforeDelete = eventsRepository.findAll().size();

        // Get the events
        restEventsMockMvc.perform(delete("/api/events/{id}", events.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean eventsExistsInEs = eventsSearchRepository.exists(events.getId());
        assertThat(eventsExistsInEs).isFalse();

        // Validate the database is empty
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);
        eventsSearchRepository.save(events);

        // Search the events
        restEventsMockMvc.perform(get("/api/_search/events?query=id:" + events.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(events.getId().intValue())))
            .andExpect(jsonPath("$.[*].debut").value(hasItem(sameInstant(DEFAULT_DEBUT))))
            .andExpect(jsonPath("$.[*].fin").value(hasItem(sameInstant(DEFAULT_FIN))))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Events.class);
    }
}
