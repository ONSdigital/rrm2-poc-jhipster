package uk.gov.ons.rrm.web.rest;

import uk.gov.ons.rrm.RrmApp;

import uk.gov.ons.rrm.domain.CollectionResponse;
import uk.gov.ons.rrm.repository.CollectionResponseRepository;

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

import uk.gov.ons.rrm.domain.enumeration.ResponseStatusKind;
/**
 * Test class for the CollectionResponseResource REST controller.
 *
 * @see CollectionResponseResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = RrmApp.class)

public class CollectionResponseResourceIntTest {

    private static final ResponseStatusKind DEFAULT_STATUS = ResponseStatusKind.NOT_STARTED;
    private static final ResponseStatusKind UPDATED_STATUS = ResponseStatusKind.IN_PROGRESS;

    @Inject
    private CollectionResponseRepository collectionResponseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCollectionResponseMockMvc;

    private CollectionResponse collectionResponse;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CollectionResponseResource collectionResponseResource = new CollectionResponseResource();
        ReflectionTestUtils.setField(collectionResponseResource, "collectionResponseRepository", collectionResponseRepository);
        this.restCollectionResponseMockMvc = MockMvcBuilders.standaloneSetup(collectionResponseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionResponse createEntity(EntityManager em) {
        CollectionResponse collectionResponse = new CollectionResponse()
                .status(DEFAULT_STATUS);
        return collectionResponse;
    }

    @Before
    public void initTest() {
        collectionResponse = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollectionResponse() throws Exception {
        int databaseSizeBeforeCreate = collectionResponseRepository.findAll().size();

        // Create the CollectionResponse

        restCollectionResponseMockMvc.perform(post("/api/collection-responses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collectionResponse)))
                .andExpect(status().isCreated());

        // Validate the CollectionResponse in the database
        List<CollectionResponse> collectionResponses = collectionResponseRepository.findAll();
        assertThat(collectionResponses).hasSize(databaseSizeBeforeCreate + 1);
        CollectionResponse testCollectionResponse = collectionResponses.get(collectionResponses.size() - 1);
        assertThat(testCollectionResponse.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllCollectionResponses() throws Exception {
        // Initialize the database
        collectionResponseRepository.saveAndFlush(collectionResponse);

        // Get all the collectionResponses
        restCollectionResponseMockMvc.perform(get("/api/collection-responses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(collectionResponse.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getCollectionResponse() throws Exception {
        // Initialize the database
        collectionResponseRepository.saveAndFlush(collectionResponse);

        // Get the collectionResponse
        restCollectionResponseMockMvc.perform(get("/api/collection-responses/{id}", collectionResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collectionResponse.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCollectionResponse() throws Exception {
        // Get the collectionResponse
        restCollectionResponseMockMvc.perform(get("/api/collection-responses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollectionResponse() throws Exception {
        // Initialize the database
        collectionResponseRepository.saveAndFlush(collectionResponse);
        int databaseSizeBeforeUpdate = collectionResponseRepository.findAll().size();

        // Update the collectionResponse
        CollectionResponse updatedCollectionResponse = collectionResponseRepository.findOne(collectionResponse.getId());
        updatedCollectionResponse
                .status(UPDATED_STATUS);

        restCollectionResponseMockMvc.perform(put("/api/collection-responses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCollectionResponse)))
                .andExpect(status().isOk());

        // Validate the CollectionResponse in the database
        List<CollectionResponse> collectionResponses = collectionResponseRepository.findAll();
        assertThat(collectionResponses).hasSize(databaseSizeBeforeUpdate);
        CollectionResponse testCollectionResponse = collectionResponses.get(collectionResponses.size() - 1);
        assertThat(testCollectionResponse.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteCollectionResponse() throws Exception {
        // Initialize the database
        collectionResponseRepository.saveAndFlush(collectionResponse);
        int databaseSizeBeforeDelete = collectionResponseRepository.findAll().size();

        // Get the collectionResponse
        restCollectionResponseMockMvc.perform(delete("/api/collection-responses/{id}", collectionResponse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CollectionResponse> collectionResponses = collectionResponseRepository.findAll();
        assertThat(collectionResponses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
