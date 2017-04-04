package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.NatureAbs;
import org.qualimaker.repository.NatureAbsRepository;
import org.qualimaker.repository.search.NatureAbsSearchRepository;
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
 * Test class for the NatureAbsResource REST controller.
 *
 * @see NatureAbsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class NatureAbsResourceIntTest {

    private static final String DEFAULT_NATURE = "AAAAAAAAAA";
    private static final String UPDATED_NATURE = "BBBBBBBBBB";

    @Autowired
    private NatureAbsRepository natureAbsRepository;

    @Autowired
    private NatureAbsSearchRepository natureAbsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNatureAbsMockMvc;

    private NatureAbs natureAbs;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NatureAbsResource natureAbsResource = new NatureAbsResource(natureAbsRepository, natureAbsSearchRepository);
        this.restNatureAbsMockMvc = MockMvcBuilders.standaloneSetup(natureAbsResource)
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
    public static NatureAbs createEntity(EntityManager em) {
        NatureAbs natureAbs = new NatureAbs()
            .nature(DEFAULT_NATURE);
        return natureAbs;
    }

    @Before
    public void initTest() {
        natureAbsSearchRepository.deleteAll();
        natureAbs = createEntity(em);
    }

    @Test
    @Transactional
    public void createNatureAbs() throws Exception {
        int databaseSizeBeforeCreate = natureAbsRepository.findAll().size();

        // Create the NatureAbs
        restNatureAbsMockMvc.perform(post("/api/nature-abs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(natureAbs)))
            .andExpect(status().isCreated());

        // Validate the NatureAbs in the database
        List<NatureAbs> natureAbsList = natureAbsRepository.findAll();
        assertThat(natureAbsList).hasSize(databaseSizeBeforeCreate + 1);
        NatureAbs testNatureAbs = natureAbsList.get(natureAbsList.size() - 1);
        assertThat(testNatureAbs.getNature()).isEqualTo(DEFAULT_NATURE);

        // Validate the NatureAbs in Elasticsearch
        NatureAbs natureAbsEs = natureAbsSearchRepository.findOne(testNatureAbs.getId());
        assertThat(natureAbsEs).isEqualToComparingFieldByField(testNatureAbs);
    }

    @Test
    @Transactional
    public void createNatureAbsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = natureAbsRepository.findAll().size();

        // Create the NatureAbs with an existing ID
        natureAbs.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNatureAbsMockMvc.perform(post("/api/nature-abs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(natureAbs)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<NatureAbs> natureAbsList = natureAbsRepository.findAll();
        assertThat(natureAbsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNatureAbs() throws Exception {
        // Initialize the database
        natureAbsRepository.saveAndFlush(natureAbs);

        // Get all the natureAbsList
        restNatureAbsMockMvc.perform(get("/api/nature-abs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(natureAbs.getId().intValue())))
            .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE.toString())));
    }

    @Test
    @Transactional
    public void getNatureAbs() throws Exception {
        // Initialize the database
        natureAbsRepository.saveAndFlush(natureAbs);

        // Get the natureAbs
        restNatureAbsMockMvc.perform(get("/api/nature-abs/{id}", natureAbs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(natureAbs.getId().intValue()))
            .andExpect(jsonPath("$.nature").value(DEFAULT_NATURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNatureAbs() throws Exception {
        // Get the natureAbs
        restNatureAbsMockMvc.perform(get("/api/nature-abs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNatureAbs() throws Exception {
        // Initialize the database
        natureAbsRepository.saveAndFlush(natureAbs);
        natureAbsSearchRepository.save(natureAbs);
        int databaseSizeBeforeUpdate = natureAbsRepository.findAll().size();

        // Update the natureAbs
        NatureAbs updatedNatureAbs = natureAbsRepository.findOne(natureAbs.getId());
        updatedNatureAbs
            .nature(UPDATED_NATURE);

        restNatureAbsMockMvc.perform(put("/api/nature-abs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNatureAbs)))
            .andExpect(status().isOk());

        // Validate the NatureAbs in the database
        List<NatureAbs> natureAbsList = natureAbsRepository.findAll();
        assertThat(natureAbsList).hasSize(databaseSizeBeforeUpdate);
        NatureAbs testNatureAbs = natureAbsList.get(natureAbsList.size() - 1);
        assertThat(testNatureAbs.getNature()).isEqualTo(UPDATED_NATURE);

        // Validate the NatureAbs in Elasticsearch
        NatureAbs natureAbsEs = natureAbsSearchRepository.findOne(testNatureAbs.getId());
        assertThat(natureAbsEs).isEqualToComparingFieldByField(testNatureAbs);
    }

    @Test
    @Transactional
    public void updateNonExistingNatureAbs() throws Exception {
        int databaseSizeBeforeUpdate = natureAbsRepository.findAll().size();

        // Create the NatureAbs

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNatureAbsMockMvc.perform(put("/api/nature-abs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(natureAbs)))
            .andExpect(status().isCreated());

        // Validate the NatureAbs in the database
        List<NatureAbs> natureAbsList = natureAbsRepository.findAll();
        assertThat(natureAbsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNatureAbs() throws Exception {
        // Initialize the database
        natureAbsRepository.saveAndFlush(natureAbs);
        natureAbsSearchRepository.save(natureAbs);
        int databaseSizeBeforeDelete = natureAbsRepository.findAll().size();

        // Get the natureAbs
        restNatureAbsMockMvc.perform(delete("/api/nature-abs/{id}", natureAbs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean natureAbsExistsInEs = natureAbsSearchRepository.exists(natureAbs.getId());
        assertThat(natureAbsExistsInEs).isFalse();

        // Validate the database is empty
        List<NatureAbs> natureAbsList = natureAbsRepository.findAll();
        assertThat(natureAbsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNatureAbs() throws Exception {
        // Initialize the database
        natureAbsRepository.saveAndFlush(natureAbs);
        natureAbsSearchRepository.save(natureAbs);

        // Search the natureAbs
        restNatureAbsMockMvc.perform(get("/api/_search/nature-abs?query=id:" + natureAbs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(natureAbs.getId().intValue())))
            .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NatureAbs.class);
    }
}
