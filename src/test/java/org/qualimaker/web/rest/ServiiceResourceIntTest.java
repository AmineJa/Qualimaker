package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Serviice;
import org.qualimaker.repository.ServiiceRepository;
import org.qualimaker.repository.search.ServiiceSearchRepository;
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
 * Test class for the ServiiceResource REST controller.
 *
 * @see ServiiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class ServiiceResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private ServiiceRepository serviiceRepository;

    @Autowired
    private ServiiceSearchRepository serviiceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiiceMockMvc;

    private Serviice serviice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiiceResource serviiceResource = new ServiiceResource(serviiceRepository, serviiceSearchRepository);
        this.restServiiceMockMvc = MockMvcBuilders.standaloneSetup(serviiceResource)
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
    public static Serviice createEntity(EntityManager em) {
        Serviice serviice = new Serviice()
            .nom(DEFAULT_NOM);
        return serviice;
    }

    @Before
    public void initTest() {
        serviiceSearchRepository.deleteAll();
        serviice = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiice() throws Exception {
        int databaseSizeBeforeCreate = serviiceRepository.findAll().size();

        // Create the Serviice
        restServiiceMockMvc.perform(post("/api/serviices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviice)))
            .andExpect(status().isCreated());

        // Validate the Serviice in the database
        List<Serviice> serviiceList = serviiceRepository.findAll();
        assertThat(serviiceList).hasSize(databaseSizeBeforeCreate + 1);
        Serviice testServiice = serviiceList.get(serviiceList.size() - 1);
        assertThat(testServiice.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Serviice in Elasticsearch
        Serviice serviiceEs = serviiceSearchRepository.findOne(testServiice.getId());
        assertThat(serviiceEs).isEqualToComparingFieldByField(testServiice);
    }

    @Test
    @Transactional
    public void createServiiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviiceRepository.findAll().size();

        // Create the Serviice with an existing ID
        serviice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiiceMockMvc.perform(post("/api/serviices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviice)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Serviice> serviiceList = serviiceRepository.findAll();
        assertThat(serviiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllServiices() throws Exception {
        // Initialize the database
        serviiceRepository.saveAndFlush(serviice);

        // Get all the serviiceList
        restServiiceMockMvc.perform(get("/api/serviices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviice.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getServiice() throws Exception {
        // Initialize the database
        serviiceRepository.saveAndFlush(serviice);

        // Get the serviice
        restServiiceMockMvc.perform(get("/api/serviices/{id}", serviice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviice.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiice() throws Exception {
        // Get the serviice
        restServiiceMockMvc.perform(get("/api/serviices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiice() throws Exception {
        // Initialize the database
        serviiceRepository.saveAndFlush(serviice);
        serviiceSearchRepository.save(serviice);
        int databaseSizeBeforeUpdate = serviiceRepository.findAll().size();

        // Update the serviice
        Serviice updatedServiice = serviiceRepository.findOne(serviice.getId());
        updatedServiice
            .nom(UPDATED_NOM);

        restServiiceMockMvc.perform(put("/api/serviices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiice)))
            .andExpect(status().isOk());

        // Validate the Serviice in the database
        List<Serviice> serviiceList = serviiceRepository.findAll();
        assertThat(serviiceList).hasSize(databaseSizeBeforeUpdate);
        Serviice testServiice = serviiceList.get(serviiceList.size() - 1);
        assertThat(testServiice.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Serviice in Elasticsearch
        Serviice serviiceEs = serviiceSearchRepository.findOne(testServiice.getId());
        assertThat(serviiceEs).isEqualToComparingFieldByField(testServiice);
    }

    @Test
    @Transactional
    public void updateNonExistingServiice() throws Exception {
        int databaseSizeBeforeUpdate = serviiceRepository.findAll().size();

        // Create the Serviice

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restServiiceMockMvc.perform(put("/api/serviices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviice)))
            .andExpect(status().isCreated());

        // Validate the Serviice in the database
        List<Serviice> serviiceList = serviiceRepository.findAll();
        assertThat(serviiceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteServiice() throws Exception {
        // Initialize the database
        serviiceRepository.saveAndFlush(serviice);
        serviiceSearchRepository.save(serviice);
        int databaseSizeBeforeDelete = serviiceRepository.findAll().size();

        // Get the serviice
        restServiiceMockMvc.perform(delete("/api/serviices/{id}", serviice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean serviiceExistsInEs = serviiceSearchRepository.exists(serviice.getId());
        assertThat(serviiceExistsInEs).isFalse();

        // Validate the database is empty
        List<Serviice> serviiceList = serviiceRepository.findAll();
        assertThat(serviiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServiice() throws Exception {
        // Initialize the database
        serviiceRepository.saveAndFlush(serviice);
        serviiceSearchRepository.save(serviice);

        // Search the serviice
        restServiiceMockMvc.perform(get("/api/_search/serviices?query=id:" + serviice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviice.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Serviice.class);
    }
}
