package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Origine;
import org.qualimaker.repository.OrigineRepository;
import org.qualimaker.repository.search.OrigineSearchRepository;
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
 * Test class for the OrigineResource REST controller.
 *
 * @see OrigineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class OrigineResourceIntTest {

    private static final String DEFAULT_ORIGINE = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINE = "BBBBBBBBBB";

    @Autowired
    private OrigineRepository origineRepository;

    @Autowired
    private OrigineSearchRepository origineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrigineMockMvc;

    private Origine origine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrigineResource origineResource = new OrigineResource(origineRepository, origineSearchRepository);
        this.restOrigineMockMvc = MockMvcBuilders.standaloneSetup(origineResource)
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
    public static Origine createEntity(EntityManager em) {
        Origine origine = new Origine()
            .origine(DEFAULT_ORIGINE);
        return origine;
    }

    @Before
    public void initTest() {
        origineSearchRepository.deleteAll();
        origine = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrigine() throws Exception {
        int databaseSizeBeforeCreate = origineRepository.findAll().size();

        // Create the Origine
        restOrigineMockMvc.perform(post("/api/origines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(origine)))
            .andExpect(status().isCreated());

        // Validate the Origine in the database
        List<Origine> origineList = origineRepository.findAll();
        assertThat(origineList).hasSize(databaseSizeBeforeCreate + 1);
        Origine testOrigine = origineList.get(origineList.size() - 1);
        assertThat(testOrigine.getOrigine()).isEqualTo(DEFAULT_ORIGINE);

        // Validate the Origine in Elasticsearch
        Origine origineEs = origineSearchRepository.findOne(testOrigine.getId());
        assertThat(origineEs).isEqualToComparingFieldByField(testOrigine);
    }

    @Test
    @Transactional
    public void createOrigineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = origineRepository.findAll().size();

        // Create the Origine with an existing ID
        origine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrigineMockMvc.perform(post("/api/origines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(origine)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Origine> origineList = origineRepository.findAll();
        assertThat(origineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOrigines() throws Exception {
        // Initialize the database
        origineRepository.saveAndFlush(origine);

        // Get all the origineList
        restOrigineMockMvc.perform(get("/api/origines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(origine.getId().intValue())))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE.toString())));
    }

    @Test
    @Transactional
    public void getOrigine() throws Exception {
        // Initialize the database
        origineRepository.saveAndFlush(origine);

        // Get the origine
        restOrigineMockMvc.perform(get("/api/origines/{id}", origine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(origine.getId().intValue()))
            .andExpect(jsonPath("$.origine").value(DEFAULT_ORIGINE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrigine() throws Exception {
        // Get the origine
        restOrigineMockMvc.perform(get("/api/origines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrigine() throws Exception {
        // Initialize the database
        origineRepository.saveAndFlush(origine);
        origineSearchRepository.save(origine);
        int databaseSizeBeforeUpdate = origineRepository.findAll().size();

        // Update the origine
        Origine updatedOrigine = origineRepository.findOne(origine.getId());
        updatedOrigine
            .origine(UPDATED_ORIGINE);

        restOrigineMockMvc.perform(put("/api/origines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrigine)))
            .andExpect(status().isOk());

        // Validate the Origine in the database
        List<Origine> origineList = origineRepository.findAll();
        assertThat(origineList).hasSize(databaseSizeBeforeUpdate);
        Origine testOrigine = origineList.get(origineList.size() - 1);
        assertThat(testOrigine.getOrigine()).isEqualTo(UPDATED_ORIGINE);

        // Validate the Origine in Elasticsearch
        Origine origineEs = origineSearchRepository.findOne(testOrigine.getId());
        assertThat(origineEs).isEqualToComparingFieldByField(testOrigine);
    }

    @Test
    @Transactional
    public void updateNonExistingOrigine() throws Exception {
        int databaseSizeBeforeUpdate = origineRepository.findAll().size();

        // Create the Origine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrigineMockMvc.perform(put("/api/origines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(origine)))
            .andExpect(status().isCreated());

        // Validate the Origine in the database
        List<Origine> origineList = origineRepository.findAll();
        assertThat(origineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrigine() throws Exception {
        // Initialize the database
        origineRepository.saveAndFlush(origine);
        origineSearchRepository.save(origine);
        int databaseSizeBeforeDelete = origineRepository.findAll().size();

        // Get the origine
        restOrigineMockMvc.perform(delete("/api/origines/{id}", origine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean origineExistsInEs = origineSearchRepository.exists(origine.getId());
        assertThat(origineExistsInEs).isFalse();

        // Validate the database is empty
        List<Origine> origineList = origineRepository.findAll();
        assertThat(origineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrigine() throws Exception {
        // Initialize the database
        origineRepository.saveAndFlush(origine);
        origineSearchRepository.save(origine);

        // Search the origine
        restOrigineMockMvc.perform(get("/api/_search/origines?query=id:" + origine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(origine.getId().intValue())))
            .andExpect(jsonPath("$.[*].origine").value(hasItem(DEFAULT_ORIGINE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Origine.class);
    }
}
