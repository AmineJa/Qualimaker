package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Servicepost;
import org.qualimaker.repository.ServicepostRepository;
import org.qualimaker.repository.search.ServicepostSearchRepository;
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
 * Test class for the ServicepostResource REST controller.
 *
 * @see ServicepostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class ServicepostResourceIntTest {

    @Autowired
    private ServicepostRepository servicepostRepository;

    @Autowired
    private ServicepostSearchRepository servicepostSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServicepostMockMvc;

    private Servicepost servicepost;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServicepostResource servicepostResource = new ServicepostResource(servicepostRepository, servicepostSearchRepository);
        this.restServicepostMockMvc = MockMvcBuilders.standaloneSetup(servicepostResource)
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
    public static Servicepost createEntity(EntityManager em) {
        Servicepost servicepost = new Servicepost();
        return servicepost;
    }

    @Before
    public void initTest() {
        servicepostSearchRepository.deleteAll();
        servicepost = createEntity(em);
    }

    @Test
    @Transactional
    public void createServicepost() throws Exception {
        int databaseSizeBeforeCreate = servicepostRepository.findAll().size();

        // Create the Servicepost
        restServicepostMockMvc.perform(post("/api/serviceposts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servicepost)))
            .andExpect(status().isCreated());

        // Validate the Servicepost in the database
        List<Servicepost> servicepostList = servicepostRepository.findAll();
        assertThat(servicepostList).hasSize(databaseSizeBeforeCreate + 1);
        Servicepost testServicepost = servicepostList.get(servicepostList.size() - 1);

        // Validate the Servicepost in Elasticsearch
        Servicepost servicepostEs = servicepostSearchRepository.findOne(testServicepost.getId());
        assertThat(servicepostEs).isEqualToComparingFieldByField(testServicepost);
    }

    @Test
    @Transactional
    public void createServicepostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = servicepostRepository.findAll().size();

        // Create the Servicepost with an existing ID
        servicepost.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicepostMockMvc.perform(post("/api/serviceposts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servicepost)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Servicepost> servicepostList = servicepostRepository.findAll();
        assertThat(servicepostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllServiceposts() throws Exception {
        // Initialize the database
        servicepostRepository.saveAndFlush(servicepost);

        // Get all the servicepostList
        restServicepostMockMvc.perform(get("/api/serviceposts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicepost.getId().intValue())));
    }

    @Test
    @Transactional
    public void getServicepost() throws Exception {
        // Initialize the database
        servicepostRepository.saveAndFlush(servicepost);

        // Get the servicepost
        restServicepostMockMvc.perform(get("/api/serviceposts/{id}", servicepost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(servicepost.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingServicepost() throws Exception {
        // Get the servicepost
        restServicepostMockMvc.perform(get("/api/serviceposts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServicepost() throws Exception {
        // Initialize the database
        servicepostRepository.saveAndFlush(servicepost);
        servicepostSearchRepository.save(servicepost);
        int databaseSizeBeforeUpdate = servicepostRepository.findAll().size();

        // Update the servicepost
        Servicepost updatedServicepost = servicepostRepository.findOne(servicepost.getId());

        restServicepostMockMvc.perform(put("/api/serviceposts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServicepost)))
            .andExpect(status().isOk());

        // Validate the Servicepost in the database
        List<Servicepost> servicepostList = servicepostRepository.findAll();
        assertThat(servicepostList).hasSize(databaseSizeBeforeUpdate);
        Servicepost testServicepost = servicepostList.get(servicepostList.size() - 1);

        // Validate the Servicepost in Elasticsearch
        Servicepost servicepostEs = servicepostSearchRepository.findOne(testServicepost.getId());
        assertThat(servicepostEs).isEqualToComparingFieldByField(testServicepost);
    }

    @Test
    @Transactional
    public void updateNonExistingServicepost() throws Exception {
        int databaseSizeBeforeUpdate = servicepostRepository.findAll().size();

        // Create the Servicepost

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restServicepostMockMvc.perform(put("/api/serviceposts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servicepost)))
            .andExpect(status().isCreated());

        // Validate the Servicepost in the database
        List<Servicepost> servicepostList = servicepostRepository.findAll();
        assertThat(servicepostList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteServicepost() throws Exception {
        // Initialize the database
        servicepostRepository.saveAndFlush(servicepost);
        servicepostSearchRepository.save(servicepost);
        int databaseSizeBeforeDelete = servicepostRepository.findAll().size();

        // Get the servicepost
        restServicepostMockMvc.perform(delete("/api/serviceposts/{id}", servicepost.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean servicepostExistsInEs = servicepostSearchRepository.exists(servicepost.getId());
        assertThat(servicepostExistsInEs).isFalse();

        // Validate the database is empty
        List<Servicepost> servicepostList = servicepostRepository.findAll();
        assertThat(servicepostList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServicepost() throws Exception {
        // Initialize the database
        servicepostRepository.saveAndFlush(servicepost);
        servicepostSearchRepository.save(servicepost);

        // Search the servicepost
        restServicepostMockMvc.perform(get("/api/_search/serviceposts?query=id:" + servicepost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicepost.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Servicepost.class);
    }
}
