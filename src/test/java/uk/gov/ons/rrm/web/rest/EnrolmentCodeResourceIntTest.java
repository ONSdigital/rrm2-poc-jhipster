package uk.gov.ons.rrm.web.rest;

import uk.gov.ons.rrm.RrmApp;

import uk.gov.ons.rrm.domain.EnrolmentCode;
import uk.gov.ons.rrm.repository.EnrolmentCodeRepository;

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
 * Test class for the EnrolmentCodeResource REST controller.
 *
 * @see EnrolmentCodeResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = RrmApp.class)

public class EnrolmentCodeResourceIntTest {
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    @Inject
    private EnrolmentCodeRepository enrolmentCodeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEnrolmentCodeMockMvc;

    private EnrolmentCode enrolmentCode;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnrolmentCodeResource enrolmentCodeResource = new EnrolmentCodeResource();
        ReflectionTestUtils.setField(enrolmentCodeResource, "enrolmentCodeRepository", enrolmentCodeRepository);
        this.restEnrolmentCodeMockMvc = MockMvcBuilders.standaloneSetup(enrolmentCodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EnrolmentCode createEntity(EntityManager em) {
        EnrolmentCode enrolmentCode = new EnrolmentCode()
                .code(DEFAULT_CODE);
        return enrolmentCode;
    }

    @Before
    public void initTest() {
        enrolmentCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createEnrolmentCode() throws Exception {
        int databaseSizeBeforeCreate = enrolmentCodeRepository.findAll().size();

        // Create the EnrolmentCode

        restEnrolmentCodeMockMvc.perform(post("/api/enrolment-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enrolmentCode)))
                .andExpect(status().isCreated());

        // Validate the EnrolmentCode in the database
        List<EnrolmentCode> enrolmentCodes = enrolmentCodeRepository.findAll();
        assertThat(enrolmentCodes).hasSize(databaseSizeBeforeCreate + 1);
        EnrolmentCode testEnrolmentCode = enrolmentCodes.get(enrolmentCodes.size() - 1);
        assertThat(testEnrolmentCode.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = enrolmentCodeRepository.findAll().size();
        // set the field null
        enrolmentCode.setCode(null);

        // Create the EnrolmentCode, which fails.

        restEnrolmentCodeMockMvc.perform(post("/api/enrolment-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enrolmentCode)))
                .andExpect(status().isBadRequest());

        List<EnrolmentCode> enrolmentCodes = enrolmentCodeRepository.findAll();
        assertThat(enrolmentCodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnrolmentCodes() throws Exception {
        // Initialize the database
        enrolmentCodeRepository.saveAndFlush(enrolmentCode);

        // Get all the enrolmentCodes
        restEnrolmentCodeMockMvc.perform(get("/api/enrolment-codes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(enrolmentCode.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getEnrolmentCode() throws Exception {
        // Initialize the database
        enrolmentCodeRepository.saveAndFlush(enrolmentCode);

        // Get the enrolmentCode
        restEnrolmentCodeMockMvc.perform(get("/api/enrolment-codes/{id}", enrolmentCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(enrolmentCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnrolmentCode() throws Exception {
        // Get the enrolmentCode
        restEnrolmentCodeMockMvc.perform(get("/api/enrolment-codes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnrolmentCode() throws Exception {
        // Initialize the database
        enrolmentCodeRepository.saveAndFlush(enrolmentCode);
        int databaseSizeBeforeUpdate = enrolmentCodeRepository.findAll().size();

        // Update the enrolmentCode
        EnrolmentCode updatedEnrolmentCode = enrolmentCodeRepository.findOne(enrolmentCode.getId());
        updatedEnrolmentCode
                .code(UPDATED_CODE);

        restEnrolmentCodeMockMvc.perform(put("/api/enrolment-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEnrolmentCode)))
                .andExpect(status().isOk());

        // Validate the EnrolmentCode in the database
        List<EnrolmentCode> enrolmentCodes = enrolmentCodeRepository.findAll();
        assertThat(enrolmentCodes).hasSize(databaseSizeBeforeUpdate);
        EnrolmentCode testEnrolmentCode = enrolmentCodes.get(enrolmentCodes.size() - 1);
        assertThat(testEnrolmentCode.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void deleteEnrolmentCode() throws Exception {
        // Initialize the database
        enrolmentCodeRepository.saveAndFlush(enrolmentCode);
        int databaseSizeBeforeDelete = enrolmentCodeRepository.findAll().size();

        // Get the enrolmentCode
        restEnrolmentCodeMockMvc.perform(delete("/api/enrolment-codes/{id}", enrolmentCode.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EnrolmentCode> enrolmentCodes = enrolmentCodeRepository.findAll();
        assertThat(enrolmentCodes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
