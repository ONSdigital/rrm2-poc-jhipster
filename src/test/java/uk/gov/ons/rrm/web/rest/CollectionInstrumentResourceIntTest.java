package uk.gov.ons.rrm.web.rest;

import uk.gov.ons.rrm.RrmApp;

import uk.gov.ons.rrm.domain.CollectionInstrument;
import uk.gov.ons.rrm.repository.CollectionInstrumentRepository;

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

import uk.gov.ons.rrm.domain.enumeration.CollectionInstrumentKind;
/**
 * Test class for the CollectionInstrumentResource REST controller.
 *
 * @see CollectionInstrumentResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = RrmApp.class)

public class CollectionInstrumentResourceIntTest {

    private static final CollectionInstrumentKind DEFAULT_INSTRUMENT_TYPE = CollectionInstrumentKind.OFFLINE;
    private static final CollectionInstrumentKind UPDATED_INSTRUMENT_TYPE = CollectionInstrumentKind.EQ;
    private static final String DEFAULT_FORM_TYPE = "AAAAA";
    private static final String UPDATED_FORM_TYPE = "BBBBB";
    private static final String DEFAULT_URN = "AAAAA";
    private static final String UPDATED_URN = "BBBBB";

    @Inject
    private CollectionInstrumentRepository collectionInstrumentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCollectionInstrumentMockMvc;

    private CollectionInstrument collectionInstrument;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CollectionInstrumentResource collectionInstrumentResource = new CollectionInstrumentResource();
        ReflectionTestUtils.setField(collectionInstrumentResource, "collectionInstrumentRepository", collectionInstrumentRepository);
        this.restCollectionInstrumentMockMvc = MockMvcBuilders.standaloneSetup(collectionInstrumentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionInstrument createEntity(EntityManager em) {
        CollectionInstrument collectionInstrument = new CollectionInstrument()
                .instrumentType(DEFAULT_INSTRUMENT_TYPE)
                .formType(DEFAULT_FORM_TYPE)
                .urn(DEFAULT_URN);
        return collectionInstrument;
    }

    @Before
    public void initTest() {
        collectionInstrument = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollectionInstrument() throws Exception {
        int databaseSizeBeforeCreate = collectionInstrumentRepository.findAll().size();

        // Create the CollectionInstrument

        restCollectionInstrumentMockMvc.perform(post("/api/collection-instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collectionInstrument)))
                .andExpect(status().isCreated());

        // Validate the CollectionInstrument in the database
        List<CollectionInstrument> collectionInstruments = collectionInstrumentRepository.findAll();
        assertThat(collectionInstruments).hasSize(databaseSizeBeforeCreate + 1);
        CollectionInstrument testCollectionInstrument = collectionInstruments.get(collectionInstruments.size() - 1);
        assertThat(testCollectionInstrument.getInstrumentType()).isEqualTo(DEFAULT_INSTRUMENT_TYPE);
        assertThat(testCollectionInstrument.getFormType()).isEqualTo(DEFAULT_FORM_TYPE);
        assertThat(testCollectionInstrument.getUrn()).isEqualTo(DEFAULT_URN);
    }

    @Test
    @Transactional
    public void getAllCollectionInstruments() throws Exception {
        // Initialize the database
        collectionInstrumentRepository.saveAndFlush(collectionInstrument);

        // Get all the collectionInstruments
        restCollectionInstrumentMockMvc.perform(get("/api/collection-instruments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(collectionInstrument.getId().intValue())))
                .andExpect(jsonPath("$.[*].instrumentType").value(hasItem(DEFAULT_INSTRUMENT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].formType").value(hasItem(DEFAULT_FORM_TYPE.toString())))
                .andExpect(jsonPath("$.[*].urn").value(hasItem(DEFAULT_URN.toString())));
    }

    @Test
    @Transactional
    public void getCollectionInstrument() throws Exception {
        // Initialize the database
        collectionInstrumentRepository.saveAndFlush(collectionInstrument);

        // Get the collectionInstrument
        restCollectionInstrumentMockMvc.perform(get("/api/collection-instruments/{id}", collectionInstrument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collectionInstrument.getId().intValue()))
            .andExpect(jsonPath("$.instrumentType").value(DEFAULT_INSTRUMENT_TYPE.toString()))
            .andExpect(jsonPath("$.formType").value(DEFAULT_FORM_TYPE.toString()))
            .andExpect(jsonPath("$.urn").value(DEFAULT_URN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCollectionInstrument() throws Exception {
        // Get the collectionInstrument
        restCollectionInstrumentMockMvc.perform(get("/api/collection-instruments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollectionInstrument() throws Exception {
        // Initialize the database
        collectionInstrumentRepository.saveAndFlush(collectionInstrument);
        int databaseSizeBeforeUpdate = collectionInstrumentRepository.findAll().size();

        // Update the collectionInstrument
        CollectionInstrument updatedCollectionInstrument = collectionInstrumentRepository.findOne(collectionInstrument.getId());
        updatedCollectionInstrument
                .instrumentType(UPDATED_INSTRUMENT_TYPE)
                .formType(UPDATED_FORM_TYPE)
                .urn(UPDATED_URN);

        restCollectionInstrumentMockMvc.perform(put("/api/collection-instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCollectionInstrument)))
                .andExpect(status().isOk());

        // Validate the CollectionInstrument in the database
        List<CollectionInstrument> collectionInstruments = collectionInstrumentRepository.findAll();
        assertThat(collectionInstruments).hasSize(databaseSizeBeforeUpdate);
        CollectionInstrument testCollectionInstrument = collectionInstruments.get(collectionInstruments.size() - 1);
        assertThat(testCollectionInstrument.getInstrumentType()).isEqualTo(UPDATED_INSTRUMENT_TYPE);
        assertThat(testCollectionInstrument.getFormType()).isEqualTo(UPDATED_FORM_TYPE);
        assertThat(testCollectionInstrument.getUrn()).isEqualTo(UPDATED_URN);
    }

    @Test
    @Transactional
    public void deleteCollectionInstrument() throws Exception {
        // Initialize the database
        collectionInstrumentRepository.saveAndFlush(collectionInstrument);
        int databaseSizeBeforeDelete = collectionInstrumentRepository.findAll().size();

        // Get the collectionInstrument
        restCollectionInstrumentMockMvc.perform(delete("/api/collection-instruments/{id}", collectionInstrument.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CollectionInstrument> collectionInstruments = collectionInstrumentRepository.findAll();
        assertThat(collectionInstruments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
