package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Events;
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
 * Test class for the EventsResource REST controller.
 *
 * @see EventsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class EventsResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ALL_DAY = false;
    private static final Boolean UPDATED_ALL_DAY = true;

    private static final ZonedDateTime DEFAULT_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_ICON = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ICON = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ICON_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ICON_CONTENT_TYPE = "image/png";

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
            .title(DEFAULT_TITLE)
            .start(DEFAULT_START)
            .description(DEFAULT_DESCRIPTION)
            .allDay(DEFAULT_ALL_DAY)
            .end(DEFAULT_END)
            .icon(DEFAULT_ICON)
            .iconContentType(DEFAULT_ICON_CONTENT_TYPE);
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
        assertThat(testEvents.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEvents.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testEvents.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvents.isAllDay()).isEqualTo(DEFAULT_ALL_DAY);
        assertThat(testEvents.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testEvents.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testEvents.getIconContentType()).isEqualTo(DEFAULT_ICON_CONTENT_TYPE);

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
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(sameInstant(DEFAULT_START))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].allDay").value(hasItem(DEFAULT_ALL_DAY.booleanValue())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(sameInstant(DEFAULT_END))))
            .andExpect(jsonPath("$.[*].iconContentType").value(hasItem(DEFAULT_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICON))));
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
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.start").value(sameInstant(DEFAULT_START)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.allDay").value(DEFAULT_ALL_DAY.booleanValue()))
            .andExpect(jsonPath("$.end").value(sameInstant(DEFAULT_END)))
            .andExpect(jsonPath("$.iconContentType").value(DEFAULT_ICON_CONTENT_TYPE))
            .andExpect(jsonPath("$.icon").value(Base64Utils.encodeToString(DEFAULT_ICON)));
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
            .title(UPDATED_TITLE)
            .start(UPDATED_START)
            .description(UPDATED_DESCRIPTION)
            .allDay(UPDATED_ALL_DAY)
            .end(UPDATED_END)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE);

        restEventsMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvents)))
            .andExpect(status().isOk());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvents.getStart()).isEqualTo(UPDATED_START);
        assertThat(testEvents.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvents.isAllDay()).isEqualTo(UPDATED_ALL_DAY);
        assertThat(testEvents.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testEvents.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testEvents.getIconContentType()).isEqualTo(UPDATED_ICON_CONTENT_TYPE);

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
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(sameInstant(DEFAULT_START))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].allDay").value(hasItem(DEFAULT_ALL_DAY.booleanValue())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(sameInstant(DEFAULT_END))))
            .andExpect(jsonPath("$.[*].iconContentType").value(hasItem(DEFAULT_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICON))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Events.class);
    }
}
