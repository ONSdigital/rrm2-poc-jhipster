package uk.gov.ons.rrm.web.rest;

import uk.gov.ons.rrm.RrmApp;

import uk.gov.ons.rrm.domain.CollectionExercise;
import uk.gov.ons.rrm.repository.CollectionExerciseRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import uk.gov.ons.rrm.domain.enumeration.CollectionExerciseStatusKind;
/**
 * Test class for the CollectionExerciseResource REST controller.
 *
 * @see CollectionExerciseResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = RrmApp.class)

public class CollectionExerciseResourceIntTest {

    private static final CollectionExerciseStatusKind DEFAULT_STATUS = CollectionExerciseStatusKind.UNPUBLISHED;
    private static final CollectionExerciseStatusKind UPDATED_STATUS = CollectionExerciseStatusKind.PUBLISHED;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private CollectionExerciseRepository collectionExerciseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCollectionExerciseMockMvc;

    private CollectionExercise collectionExercise;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CollectionExerciseResource collectionExerciseResource = new CollectionExerciseResource();
        ReflectionTestUtils.setField(collectionExerciseResource, "collectionExerciseRepository", collectionExerciseRepository);
        this.restCollectionExerciseMockMvc = MockMvcBuilders.standaloneSetup(collectionExerciseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionExercise createEntity(EntityManager em) {
        CollectionExercise collectionExercise = new CollectionExercise()
                .status(DEFAULT_STATUS)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE);
        return collectionExercise;
    }

    @Before
    public void initTest() {
        collectionExercise = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollectionExercise() throws Exception {
        int databaseSizeBeforeCreate = collectionExerciseRepository.findAll().size();

        // Create the CollectionExercise

        restCollectionExerciseMockMvc.perform(post("/api/collection-exercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collectionExercise)))
                .andExpect(status().isCreated());

        // Validate the CollectionExercise in the database
        List<CollectionExercise> collectionExercises = collectionExerciseRepository.findAll();
        assertThat(collectionExercises).hasSize(databaseSizeBeforeCreate + 1);
        CollectionExercise testCollectionExercise = collectionExercises.get(collectionExercises.size() - 1);
        assertThat(testCollectionExercise.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCollectionExercise.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCollectionExercise.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void getAllCollectionExercises() throws Exception {
        // Initialize the database
        collectionExerciseRepository.saveAndFlush(collectionExercise);

        // Get all the collectionExercises
        restCollectionExerciseMockMvc.perform(get("/api/collection-exercises?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(collectionExercise.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getCollectionExercise() throws Exception {
        // Initialize the database
        collectionExerciseRepository.saveAndFlush(collectionExercise);

        // Get the collectionExercise
        restCollectionExerciseMockMvc.perform(get("/api/collection-exercises/{id}", collectionExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collectionExercise.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCollectionExercise() throws Exception {
        // Get the collectionExercise
        restCollectionExerciseMockMvc.perform(get("/api/collection-exercises/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollectionExercise() throws Exception {
        // Initialize the database
        collectionExerciseRepository.saveAndFlush(collectionExercise);
        int databaseSizeBeforeUpdate = collectionExerciseRepository.findAll().size();

        // Update the collectionExercise
        CollectionExercise updatedCollectionExercise = collectionExerciseRepository.findOne(collectionExercise.getId());
        updatedCollectionExercise
                .status(UPDATED_STATUS)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE);

        restCollectionExerciseMockMvc.perform(put("/api/collection-exercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCollectionExercise)))
                .andExpect(status().isOk());

        // Validate the CollectionExercise in the database
        List<CollectionExercise> collectionExercises = collectionExerciseRepository.findAll();
        assertThat(collectionExercises).hasSize(databaseSizeBeforeUpdate);
        CollectionExercise testCollectionExercise = collectionExercises.get(collectionExercises.size() - 1);
        assertThat(testCollectionExercise.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCollectionExercise.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCollectionExercise.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteCollectionExercise() throws Exception {
        // Initialize the database
        collectionExerciseRepository.saveAndFlush(collectionExercise);
        int databaseSizeBeforeDelete = collectionExerciseRepository.findAll().size();

        // Get the collectionExercise
        restCollectionExerciseMockMvc.perform(delete("/api/collection-exercises/{id}", collectionExercise.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CollectionExercise> collectionExercises = collectionExerciseRepository.findAll();
        assertThat(collectionExercises).hasSize(databaseSizeBeforeDelete - 1);
    }
}
