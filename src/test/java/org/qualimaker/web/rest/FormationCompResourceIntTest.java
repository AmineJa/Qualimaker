package org.qualimaker.web.rest;

import org.qualimaker.QualiMakerApp;

import org.qualimaker.domain.FormationComp;
import org.qualimaker.repository.FormationCompRepository;
import org.qualimaker.repository.search.FormationCompSearchRepository;
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
 * Test class for the FormationCompResource REST controller.
 *
 * @see FormationCompResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QualiMakerApp.class)
public class FormationCompResourceIntTest {

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    @Autowired
    private FormationCompRepository formationCompRepository;

    @Autowired
    private FormationCompSearchRepository formationCompSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFormationCompMockMvc;

    private FormationComp formationComp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FormationCompResource formationCompResource = new FormationCompResource(formationCompRepository, formationCompSearchRepository);
        this.restFormationCompMockMvc = MockMvcBuilders.standaloneSetup(formationCompResource)
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
    public static FormationComp createEntity(EntityManager em) {
        FormationComp formationComp = new FormationComp()
            .commentaire(DEFAULT_COMMENTAIRE);
        return formationComp;
    }

    @Before
    public void initTest() {
        formationCompSearchRepository.deleteAll();
        formationComp = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormationComp() throws Exception {
        int databaseSizeBeforeCreate = formationCompRepository.findAll().size();

        // Create the FormationComp
        restFormationCompMockMvc.perform(post("/api/formation-comps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formationComp)))
            .andExpect(status().isCreated());

        // Validate the FormationComp in the database
        List<FormationComp> formationCompList = formationCompRepository.findAll();
        assertThat(formationCompList).hasSize(databaseSizeBeforeCreate + 1);
        FormationComp testFormationComp = formationCompList.get(formationCompList.size() - 1);
        assertThat(testFormationComp.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);

        // Validate the FormationComp in Elasticsearch
        FormationComp formationCompEs = formationCompSearchRepository.findOne(testFormationComp.getId());
        assertThat(formationCompEs).isEqualToComparingFieldByField(testFormationComp);
    }

    @Test
    @Transactional
    public void createFormationCompWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formationCompRepository.findAll().size();

        // Create the FormationComp with an existing ID
        formationComp.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationCompMockMvc.perform(post("/api/formation-comps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formationComp)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FormationComp> formationCompList = formationCompRepository.findAll();
        assertThat(formationCompList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFormationComps() throws Exception {
        // Initialize the database
        formationCompRepository.saveAndFlush(formationComp);

        // Get all the formationCompList
        restFormationCompMockMvc.perform(get("/api/formation-comps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationComp.getId().intValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }

    @Test
    @Transactional
    public void getFormationComp() throws Exception {
        // Initialize the database
        formationCompRepository.saveAndFlush(formationComp);

        // Get the formationComp
        restFormationCompMockMvc.perform(get("/api/formation-comps/{id}", formationComp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formationComp.getId().intValue()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFormationComp() throws Exception {
        // Get the formationComp
        restFormationCompMockMvc.perform(get("/api/formation-comps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormationComp() throws Exception {
        // Initialize the database
        formationCompRepository.saveAndFlush(formationComp);
        formationCompSearchRepository.save(formationComp);
        int databaseSizeBeforeUpdate = formationCompRepository.findAll().size();

        // Update the formationComp
        FormationComp updatedFormationComp = formationCompRepository.findOne(formationComp.getId());
        updatedFormationComp
            .commentaire(UPDATED_COMMENTAIRE);

        restFormationCompMockMvc.perform(put("/api/formation-comps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormationComp)))
            .andExpect(status().isOk());

        // Validate the FormationComp in the database
        List<FormationComp> formationCompList = formationCompRepository.findAll();
        assertThat(formationCompList).hasSize(databaseSizeBeforeUpdate);
        FormationComp testFormationComp = formationCompList.get(formationCompList.size() - 1);
        assertThat(testFormationComp.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);

        // Validate the FormationComp in Elasticsearch
        FormationComp formationCompEs = formationCompSearchRepository.findOne(testFormationComp.getId());
        assertThat(formationCompEs).isEqualToComparingFieldByField(testFormationComp);
    }

    @Test
    @Transactional
    public void updateNonExistingFormationComp() throws Exception {
        int databaseSizeBeforeUpdate = formationCompRepository.findAll().size();

        // Create the FormationComp

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFormationCompMockMvc.perform(put("/api/formation-comps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formationComp)))
            .andExpect(status().isCreated());

        // Validate the FormationComp in the database
        List<FormationComp> formationCompList = formationCompRepository.findAll();
        assertThat(formationCompList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFormationComp() throws Exception {
        // Initialize the database
        formationCompRepository.saveAndFlush(formationComp);
        formationCompSearchRepository.save(formationComp);
        int databaseSizeBeforeDelete = formationCompRepository.findAll().size();

        // Get the formationComp
        restFormationCompMockMvc.perform(delete("/api/formation-comps/{id}", formationComp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean formationCompExistsInEs = formationCompSearchRepository.exists(formationComp.getId());
        assertThat(formationCompExistsInEs).isFalse();

        // Validate the database is empty
        List<FormationComp> formationCompList = formationCompRepository.findAll();
        assertThat(formationCompList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFormationComp() throws Exception {
        // Initialize the database
        formationCompRepository.saveAndFlush(formationComp);
        formationCompSearchRepository.save(formationComp);

        // Search the formationComp
        restFormationCompMockMvc.perform(get("/api/_search/formation-comps?query=id:" + formationComp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationComp.getId().intValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationComp.class);
    }
}
