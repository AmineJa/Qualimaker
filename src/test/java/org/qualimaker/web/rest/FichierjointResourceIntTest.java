package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.Fichierjoint;
import org.qualimaker.repository.FichierjointRepository;
import org.qualimaker.repository.search.FichierjointSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FichierjointResource REST controller.
 *
 * @see FichierjointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class FichierjointResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FICHEJOINT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHEJOINT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FICHEJOINT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHEJOINT_CONTENT_TYPE = "image/png";

    @Autowired
    private FichierjointRepository fichierjointRepository;

    @Autowired
    private FichierjointSearchRepository fichierjointSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFichierjointMockMvc;

    private Fichierjoint fichierjoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FichierjointResource fichierjointResource = new FichierjointResource(fichierjointRepository, fichierjointSearchRepository);
        this.restFichierjointMockMvc = MockMvcBuilders.standaloneSetup(fichierjointResource)
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
    public static Fichierjoint createEntity(EntityManager em) {
        Fichierjoint fichierjoint = new Fichierjoint()
            .nom(DEFAULT_NOM)
            .commentaire(DEFAULT_COMMENTAIRE)
            .fichejoint(DEFAULT_FICHEJOINT)
            .fichejointContentType(DEFAULT_FICHEJOINT_CONTENT_TYPE);
        return fichierjoint;
    }

    @Before
    public void initTest() {
        fichierjointSearchRepository.deleteAll();
        fichierjoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createFichierjoint() throws Exception {
        int databaseSizeBeforeCreate = fichierjointRepository.findAll().size();

        // Create the Fichierjoint
        restFichierjointMockMvc.perform(post("/api/fichierjoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fichierjoint)))
            .andExpect(status().isCreated());

        // Validate the Fichierjoint in the database
        List<Fichierjoint> fichierjointList = fichierjointRepository.findAll();
        assertThat(fichierjointList).hasSize(databaseSizeBeforeCreate + 1);
        Fichierjoint testFichierjoint = fichierjointList.get(fichierjointList.size() - 1);
        assertThat(testFichierjoint.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testFichierjoint.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testFichierjoint.getFichejoint()).isEqualTo(DEFAULT_FICHEJOINT);
        assertThat(testFichierjoint.getFichejointContentType()).isEqualTo(DEFAULT_FICHEJOINT_CONTENT_TYPE);

        // Validate the Fichierjoint in Elasticsearch
        Fichierjoint fichierjointEs = fichierjointSearchRepository.findOne(testFichierjoint.getId());
        assertThat(fichierjointEs).isEqualToComparingFieldByField(testFichierjoint);
    }

    @Test
    @Transactional
    public void createFichierjointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fichierjointRepository.findAll().size();

        // Create the Fichierjoint with an existing ID
        fichierjoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFichierjointMockMvc.perform(post("/api/fichierjoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fichierjoint)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Fichierjoint> fichierjointList = fichierjointRepository.findAll();
        assertThat(fichierjointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFichierjoints() throws Exception {
        // Initialize the database
        fichierjointRepository.saveAndFlush(fichierjoint);

        // Get all the fichierjointList
        restFichierjointMockMvc.perform(get("/api/fichierjoints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichierjoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].fichejointContentType").value(hasItem(DEFAULT_FICHEJOINT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichejoint").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHEJOINT))));
    }

    @Test
    @Transactional
    public void getFichierjoint() throws Exception {
        // Initialize the database
        fichierjointRepository.saveAndFlush(fichierjoint);

        // Get the fichierjoint
        restFichierjointMockMvc.perform(get("/api/fichierjoints/{id}", fichierjoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fichierjoint.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()))
            .andExpect(jsonPath("$.fichejointContentType").value(DEFAULT_FICHEJOINT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichejoint").value(Base64Utils.encodeToString(DEFAULT_FICHEJOINT)));
    }

    @Test
    @Transactional
    public void getNonExistingFichierjoint() throws Exception {
        // Get the fichierjoint
        restFichierjointMockMvc.perform(get("/api/fichierjoints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFichierjoint() throws Exception {
        // Initialize the database
        fichierjointRepository.saveAndFlush(fichierjoint);
        fichierjointSearchRepository.save(fichierjoint);
        int databaseSizeBeforeUpdate = fichierjointRepository.findAll().size();

        // Update the fichierjoint
        Fichierjoint updatedFichierjoint = fichierjointRepository.findOne(fichierjoint.getId());
        updatedFichierjoint
            .nom(UPDATED_NOM)
            .commentaire(UPDATED_COMMENTAIRE)
            .fichejoint(UPDATED_FICHEJOINT)
            .fichejointContentType(UPDATED_FICHEJOINT_CONTENT_TYPE);

        restFichierjointMockMvc.perform(put("/api/fichierjoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFichierjoint)))
            .andExpect(status().isOk());

        // Validate the Fichierjoint in the database
        List<Fichierjoint> fichierjointList = fichierjointRepository.findAll();
        assertThat(fichierjointList).hasSize(databaseSizeBeforeUpdate);
        Fichierjoint testFichierjoint = fichierjointList.get(fichierjointList.size() - 1);
        assertThat(testFichierjoint.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFichierjoint.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testFichierjoint.getFichejoint()).isEqualTo(UPDATED_FICHEJOINT);
        assertThat(testFichierjoint.getFichejointContentType()).isEqualTo(UPDATED_FICHEJOINT_CONTENT_TYPE);

        // Validate the Fichierjoint in Elasticsearch
        Fichierjoint fichierjointEs = fichierjointSearchRepository.findOne(testFichierjoint.getId());
        assertThat(fichierjointEs).isEqualToComparingFieldByField(testFichierjoint);
    }

    @Test
    @Transactional
    public void updateNonExistingFichierjoint() throws Exception {
        int databaseSizeBeforeUpdate = fichierjointRepository.findAll().size();

        // Create the Fichierjoint

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFichierjointMockMvc.perform(put("/api/fichierjoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fichierjoint)))
            .andExpect(status().isCreated());

        // Validate the Fichierjoint in the database
        List<Fichierjoint> fichierjointList = fichierjointRepository.findAll();
        assertThat(fichierjointList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFichierjoint() throws Exception {
        // Initialize the database
        fichierjointRepository.saveAndFlush(fichierjoint);
        fichierjointSearchRepository.save(fichierjoint);
        int databaseSizeBeforeDelete = fichierjointRepository.findAll().size();

        // Get the fichierjoint
        restFichierjointMockMvc.perform(delete("/api/fichierjoints/{id}", fichierjoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean fichierjointExistsInEs = fichierjointSearchRepository.exists(fichierjoint.getId());
        assertThat(fichierjointExistsInEs).isFalse();

        // Validate the database is empty
        List<Fichierjoint> fichierjointList = fichierjointRepository.findAll();
        assertThat(fichierjointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFichierjoint() throws Exception {
        // Initialize the database
        fichierjointRepository.saveAndFlush(fichierjoint);
        fichierjointSearchRepository.save(fichierjoint);

        // Search the fichierjoint
        restFichierjointMockMvc.perform(get("/api/_search/fichierjoints?query=id:" + fichierjoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichierjoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].fichejointContentType").value(hasItem(DEFAULT_FICHEJOINT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichejoint").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHEJOINT))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fichierjoint.class);
    }
}
