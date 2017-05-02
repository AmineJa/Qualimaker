package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Poste;
import org.qualimaker.repository.PosteRepository;
import org.qualimaker.repository.search.PosteSearchRepository;
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
 * Test class for the PosteResource REST controller.
 *
 * @see PosteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class PosteResourceIntTest {

    private static final String DEFAULT_POST = "AAAAAAAAAA";
    private static final String UPDATED_POST = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT = "AAAAAAAAAA";
    private static final String UPDATED_PARENT = "BBBBBBBBBB";

    private static final String DEFAULT_SUPP = "AAAAAAAAAA";
    private static final String UPDATED_SUPP = "BBBBBBBBBB";

    @Autowired
    private PosteRepository posteRepository;

    @Autowired
    private PosteSearchRepository posteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPosteMockMvc;

    private Poste poste;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PosteResource posteResource = new PosteResource(posteRepository, posteSearchRepository);
        this.restPosteMockMvc = MockMvcBuilders.standaloneSetup(posteResource)
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
    public static Poste createEntity(EntityManager em) {
        Poste poste = new Poste()
            .post(DEFAULT_POST)
            .parent(DEFAULT_PARENT)
            .supp(DEFAULT_SUPP);
        return poste;
    }

    @Before
    public void initTest() {
        posteSearchRepository.deleteAll();
        poste = createEntity(em);
    }

    @Test
    @Transactional
    public void createPoste() throws Exception {
        int databaseSizeBeforeCreate = posteRepository.findAll().size();

        // Create the Poste
        restPosteMockMvc.perform(post("/api/postes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(poste)))
            .andExpect(status().isCreated());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeCreate + 1);
        Poste testPoste = posteList.get(posteList.size() - 1);
        assertThat(testPoste.getPost()).isEqualTo(DEFAULT_POST);
        assertThat(testPoste.getParent()).isEqualTo(DEFAULT_PARENT);
        assertThat(testPoste.getSupp()).isEqualTo(DEFAULT_SUPP);

        // Validate the Poste in Elasticsearch
        Poste posteEs = posteSearchRepository.findOne(testPoste.getId());
        assertThat(posteEs).isEqualToComparingFieldByField(testPoste);
    }

    @Test
    @Transactional
    public void createPosteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = posteRepository.findAll().size();

        // Create the Poste with an existing ID
        poste.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPosteMockMvc.perform(post("/api/postes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(poste)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPostes() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get all the posteList
        restPosteMockMvc.perform(get("/api/postes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poste.getId().intValue())))
            .andExpect(jsonPath("$.[*].post").value(hasItem(DEFAULT_POST.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.toString())))
            .andExpect(jsonPath("$.[*].supp").value(hasItem(DEFAULT_SUPP.toString())));
    }

    @Test
    @Transactional
    public void getPoste() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);

        // Get the poste
        restPosteMockMvc.perform(get("/api/postes/{id}", poste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(poste.getId().intValue()))
            .andExpect(jsonPath("$.post").value(DEFAULT_POST.toString()))
            .andExpect(jsonPath("$.parent").value(DEFAULT_PARENT.toString()))
            .andExpect(jsonPath("$.supp").value(DEFAULT_SUPP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPoste() throws Exception {
        // Get the poste
        restPosteMockMvc.perform(get("/api/postes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePoste() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);
        posteSearchRepository.save(poste);
        int databaseSizeBeforeUpdate = posteRepository.findAll().size();

        // Update the poste
        Poste updatedPoste = posteRepository.findOne(poste.getId());
        updatedPoste
            .post(UPDATED_POST)
            .parent(UPDATED_PARENT)
            .supp(UPDATED_SUPP);

        restPosteMockMvc.perform(put("/api/postes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPoste)))
            .andExpect(status().isOk());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate);
        Poste testPoste = posteList.get(posteList.size() - 1);
        assertThat(testPoste.getPost()).isEqualTo(UPDATED_POST);
        assertThat(testPoste.getParent()).isEqualTo(UPDATED_PARENT);
        assertThat(testPoste.getSupp()).isEqualTo(UPDATED_SUPP);

        // Validate the Poste in Elasticsearch
        Poste posteEs = posteSearchRepository.findOne(testPoste.getId());
        assertThat(posteEs).isEqualToComparingFieldByField(testPoste);
    }

    @Test
    @Transactional
    public void updateNonExistingPoste() throws Exception {
        int databaseSizeBeforeUpdate = posteRepository.findAll().size();

        // Create the Poste

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPosteMockMvc.perform(put("/api/postes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(poste)))
            .andExpect(status().isCreated());

        // Validate the Poste in the database
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePoste() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);
        posteSearchRepository.save(poste);
        int databaseSizeBeforeDelete = posteRepository.findAll().size();

        // Get the poste
        restPosteMockMvc.perform(delete("/api/postes/{id}", poste.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean posteExistsInEs = posteSearchRepository.exists(poste.getId());
        assertThat(posteExistsInEs).isFalse();

        // Validate the database is empty
        List<Poste> posteList = posteRepository.findAll();
        assertThat(posteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPoste() throws Exception {
        // Initialize the database
        posteRepository.saveAndFlush(poste);
        posteSearchRepository.save(poste);

        // Search the poste
        restPosteMockMvc.perform(get("/api/_search/postes?query=id:" + poste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poste.getId().intValue())))
            .andExpect(jsonPath("$.[*].post").value(hasItem(DEFAULT_POST.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.toString())))
            .andExpect(jsonPath("$.[*].supp").value(hasItem(DEFAULT_SUPP.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Poste.class);
    }
}
