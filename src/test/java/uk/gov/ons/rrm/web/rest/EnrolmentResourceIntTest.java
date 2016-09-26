package uk.gov.ons.rrm.web.rest;

import uk.gov.ons.rrm.RrmApp;

import uk.gov.ons.rrm.domain.Enrolment;
import uk.gov.ons.rrm.repository.EnrolmentRepository;

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

import uk.gov.ons.rrm.domain.enumeration.EnrolmentStatusKind;
/**
 * Test class for the EnrolmentResource REST controller.
 *
 * @see EnrolmentResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = RrmApp.class)

public class EnrolmentResourceIntTest {

    private static final EnrolmentStatusKind DEFAULT_ENROLMENT_STATUS = EnrolmentStatusKind.ENABLED;
    private static final EnrolmentStatusKind UPDATED_ENROLMENT_STATUS = EnrolmentStatusKind.DISABLED;

    @Inject
    private EnrolmentRepository enrolmentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEnrolmentMockMvc;

    private Enrolment enrolment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnrolmentResource enrolmentResource = new EnrolmentResource();
        ReflectionTestUtils.setField(enrolmentResource, "enrolmentRepository", enrolmentRepository);
        this.restEnrolmentMockMvc = MockMvcBuilders.standaloneSetup(enrolmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enrolment createEntity(EntityManager em) {
        Enrolment enrolment = new Enrolment()
                .enrolmentStatus(DEFAULT_ENROLMENT_STATUS);
        return enrolment;
    }

    @Before
    public void initTest() {
        enrolment = createEntity(em);
    }

    @Test
    @Transactional
    public void createEnrolment() throws Exception {
        int databaseSizeBeforeCreate = enrolmentRepository.findAll().size();

        // Create the Enrolment

        restEnrolmentMockMvc.perform(post("/api/enrolments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enrolment)))
                .andExpect(status().isCreated());

        // Validate the Enrolment in the database
        List<Enrolment> enrolments = enrolmentRepository.findAll();
        assertThat(enrolments).hasSize(databaseSizeBeforeCreate + 1);
        Enrolment testEnrolment = enrolments.get(enrolments.size() - 1);
        assertThat(testEnrolment.getEnrolmentStatus()).isEqualTo(DEFAULT_ENROLMENT_STATUS);
    }

    @Test
    @Transactional
    public void getAllEnrolments() throws Exception {
        // Initialize the database
        enrolmentRepository.saveAndFlush(enrolment);

        // Get all the enrolments
        restEnrolmentMockMvc.perform(get("/api/enrolments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(enrolment.getId().intValue())))
                .andExpect(jsonPath("$.[*].enrolmentStatus").value(hasItem(DEFAULT_ENROLMENT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getEnrolment() throws Exception {
        // Initialize the database
        enrolmentRepository.saveAndFlush(enrolment);

        // Get the enrolment
        restEnrolmentMockMvc.perform(get("/api/enrolments/{id}", enrolment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(enrolment.getId().intValue()))
            .andExpect(jsonPath("$.enrolmentStatus").value(DEFAULT_ENROLMENT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnrolment() throws Exception {
        // Get the enrolment
        restEnrolmentMockMvc.perform(get("/api/enrolments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnrolment() throws Exception {
        // Initialize the database
        enrolmentRepository.saveAndFlush(enrolment);
        int databaseSizeBeforeUpdate = enrolmentRepository.findAll().size();

        // Update the enrolment
        Enrolment updatedEnrolment = enrolmentRepository.findOne(enrolment.getId());
        updatedEnrolment
                .enrolmentStatus(UPDATED_ENROLMENT_STATUS);

        restEnrolmentMockMvc.perform(put("/api/enrolments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEnrolment)))
                .andExpect(status().isOk());

        // Validate the Enrolment in the database
        List<Enrolment> enrolments = enrolmentRepository.findAll();
        assertThat(enrolments).hasSize(databaseSizeBeforeUpdate);
        Enrolment testEnrolment = enrolments.get(enrolments.size() - 1);
        assertThat(testEnrolment.getEnrolmentStatus()).isEqualTo(UPDATED_ENROLMENT_STATUS);
    }

    @Test
    @Transactional
    public void deleteEnrolment() throws Exception {
        // Initialize the database
        enrolmentRepository.saveAndFlush(enrolment);
        int databaseSizeBeforeDelete = enrolmentRepository.findAll().size();

        // Get the enrolment
        restEnrolmentMockMvc.perform(delete("/api/enrolments/{id}", enrolment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Enrolment> enrolments = enrolmentRepository.findAll();
        assertThat(enrolments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
