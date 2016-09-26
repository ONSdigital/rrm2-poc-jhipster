package uk.gov.ons.rrm.web.rest;

import uk.gov.ons.rrm.RrmApp;

import uk.gov.ons.rrm.domain.SampleSelection;
import uk.gov.ons.rrm.repository.SampleSelectionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SampleSelectionResource REST controller.
 *
 * @see SampleSelectionResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = RrmApp.class)

public class SampleSelectionResourceIntTest {

    @Inject
    private SampleSelectionRepository sampleSelectionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSampleSelectionMockMvc;

    private SampleSelection sampleSelection;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SampleSelectionResource sampleSelectionResource = new SampleSelectionResource();
        ReflectionTestUtils.setField(sampleSelectionResource, "sampleSelectionRepository", sampleSelectionRepository);
        this.restSampleSelectionMockMvc = MockMvcBuilders.standaloneSetup(sampleSelectionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SampleSelection createEntity(EntityManager em) {
        SampleSelection sampleSelection = new SampleSelection();
        return sampleSelection;
    }

    @Before
    public void initTest() {
        sampleSelection = createEntity(em);
    }

    @Test
    @Transactional
    public void createSampleSelection() throws Exception {
        int databaseSizeBeforeCreate = sampleSelectionRepository.findAll().size();

        // Create the SampleSelection

        restSampleSelectionMockMvc.perform(post("/api/sample-selections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sampleSelection)))
                .andExpect(status().isCreated());

        // Validate the SampleSelection in the database
        List<SampleSelection> sampleSelections = sampleSelectionRepository.findAll();
        assertThat(sampleSelections).hasSize(databaseSizeBeforeCreate + 1);
        SampleSelection testSampleSelection = sampleSelections.get(sampleSelections.size() - 1);
    }

    @Test
    @Transactional
    public void getAllSampleSelections() throws Exception {
        // Initialize the database
        sampleSelectionRepository.saveAndFlush(sampleSelection);

        // Get all the sampleSelections
        restSampleSelectionMockMvc.perform(get("/api/sample-selections?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sampleSelection.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSampleSelection() throws Exception {
        // Initialize the database
        sampleSelectionRepository.saveAndFlush(sampleSelection);

        // Get the sampleSelection
        restSampleSelectionMockMvc.perform(get("/api/sample-selections/{id}", sampleSelection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sampleSelection.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSampleSelection() throws Exception {
        // Get the sampleSelection
        restSampleSelectionMockMvc.perform(get("/api/sample-selections/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSampleSelection() throws Exception {
        // Initialize the database
        sampleSelectionRepository.saveAndFlush(sampleSelection);
        int databaseSizeBeforeUpdate = sampleSelectionRepository.findAll().size();

        // Update the sampleSelection
        SampleSelection updatedSampleSelection = sampleSelectionRepository.findOne(sampleSelection.getId());

        restSampleSelectionMockMvc.perform(put("/api/sample-selections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSampleSelection)))
                .andExpect(status().isOk());

        // Validate the SampleSelection in the database
        List<SampleSelection> sampleSelections = sampleSelectionRepository.findAll();
        assertThat(sampleSelections).hasSize(databaseSizeBeforeUpdate);
        SampleSelection testSampleSelection = sampleSelections.get(sampleSelections.size() - 1);
    }

    @Test
    @Transactional
    public void deleteSampleSelection() throws Exception {
        // Initialize the database
        sampleSelectionRepository.saveAndFlush(sampleSelection);
        int databaseSizeBeforeDelete = sampleSelectionRepository.findAll().size();

        // Get the sampleSelection
        restSampleSelectionMockMvc.perform(delete("/api/sample-selections/{id}", sampleSelection.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SampleSelection> sampleSelections = sampleSelectionRepository.findAll();
        assertThat(sampleSelections).hasSize(databaseSizeBeforeDelete - 1);
    }
}
