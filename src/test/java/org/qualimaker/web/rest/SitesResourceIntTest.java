package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Sites;
import org.qualimaker.repository.SitesRepository;
import org.qualimaker.repository.search.SitesSearchRepository;
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
 * Test class for the SitesResource REST controller.
 *
 * @see SitesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class SitesResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private SitesRepository sitesRepository;

    @Autowired
    private SitesSearchRepository sitesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSitesMockMvc;

    private Sites sites;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SitesResource sitesResource = new SitesResource(sitesRepository, sitesSearchRepository);
        this.restSitesMockMvc = MockMvcBuilders.standaloneSetup(sitesResource)
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
    public static Sites createEntity(EntityManager em) {
        Sites sites = new Sites()
            .nom(DEFAULT_NOM);
        return sites;
    }

    @Before
    public void initTest() {
        sitesSearchRepository.deleteAll();
        sites = createEntity(em);
    }

    @Test
    @Transactional
    public void createSites() throws Exception {
        int databaseSizeBeforeCreate = sitesRepository.findAll().size();

        // Create the Sites
        restSitesMockMvc.perform(post("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sites)))
            .andExpect(status().isCreated());

        // Validate the Sites in the database
        List<Sites> sitesList = sitesRepository.findAll();
        assertThat(sitesList).hasSize(databaseSizeBeforeCreate + 1);
        Sites testSites = sitesList.get(sitesList.size() - 1);
        assertThat(testSites.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Sites in Elasticsearch
        Sites sitesEs = sitesSearchRepository.findOne(testSites.getId());
        assertThat(sitesEs).isEqualToComparingFieldByField(testSites);
    }

    @Test
    @Transactional
    public void createSitesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sitesRepository.findAll().size();

        // Create the Sites with an existing ID
        sites.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSitesMockMvc.perform(post("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sites)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Sites> sitesList = sitesRepository.findAll();
        assertThat(sitesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSites() throws Exception {
        // Initialize the database
        sitesRepository.saveAndFlush(sites);

        // Get all the sitesList
        restSitesMockMvc.perform(get("/api/sites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sites.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getSites() throws Exception {
        // Initialize the database
        sitesRepository.saveAndFlush(sites);

        // Get the sites
        restSitesMockMvc.perform(get("/api/sites/{id}", sites.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sites.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSites() throws Exception {
        // Get the sites
        restSitesMockMvc.perform(get("/api/sites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSites() throws Exception {
        // Initialize the database
        sitesRepository.saveAndFlush(sites);
        sitesSearchRepository.save(sites);
        int databaseSizeBeforeUpdate = sitesRepository.findAll().size();

        // Update the sites
        Sites updatedSites = sitesRepository.findOne(sites.getId());
        updatedSites
            .nom(UPDATED_NOM);

        restSitesMockMvc.perform(put("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSites)))
            .andExpect(status().isOk());

        // Validate the Sites in the database
        List<Sites> sitesList = sitesRepository.findAll();
        assertThat(sitesList).hasSize(databaseSizeBeforeUpdate);
        Sites testSites = sitesList.get(sitesList.size() - 1);
        assertThat(testSites.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Sites in Elasticsearch
        Sites sitesEs = sitesSearchRepository.findOne(testSites.getId());
        assertThat(sitesEs).isEqualToComparingFieldByField(testSites);
    }

    @Test
    @Transactional
    public void updateNonExistingSites() throws Exception {
        int databaseSizeBeforeUpdate = sitesRepository.findAll().size();

        // Create the Sites

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSitesMockMvc.perform(put("/api/sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sites)))
            .andExpect(status().isCreated());

        // Validate the Sites in the database
        List<Sites> sitesList = sitesRepository.findAll();
        assertThat(sitesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSites() throws Exception {
        // Initialize the database
        sitesRepository.saveAndFlush(sites);
        sitesSearchRepository.save(sites);
        int databaseSizeBeforeDelete = sitesRepository.findAll().size();

        // Get the sites
        restSitesMockMvc.perform(delete("/api/sites/{id}", sites.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sitesExistsInEs = sitesSearchRepository.exists(sites.getId());
        assertThat(sitesExistsInEs).isFalse();

        // Validate the database is empty
        List<Sites> sitesList = sitesRepository.findAll();
        assertThat(sitesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSites() throws Exception {
        // Initialize the database
        sitesRepository.saveAndFlush(sites);
        sitesSearchRepository.save(sites);

        // Search the sites
        restSitesMockMvc.perform(get("/api/_search/sites?query=id:" + sites.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sites.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sites.class);
    }
}
