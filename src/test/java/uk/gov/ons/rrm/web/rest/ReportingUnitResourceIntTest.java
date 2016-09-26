package uk.gov.ons.rrm.web.rest;

import uk.gov.ons.rrm.RrmApp;

import uk.gov.ons.rrm.domain.ReportingUnit;
import uk.gov.ons.rrm.repository.ReportingUnitRepository;

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
 * Test class for the ReportingUnitResource REST controller.
 *
 * @see ReportingUnitResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = RrmApp.class)

public class ReportingUnitResourceIntTest {
    private static final String DEFAULT_UNIQUE_REFERENCE = "AAAAA";
    private static final String UPDATED_UNIQUE_REFERENCE = "BBBBB";
    private static final String DEFAULT_BUSINESS_NAME = "AAAAA";
    private static final String UPDATED_BUSINESS_NAME = "BBBBB";

    @Inject
    private ReportingUnitRepository reportingUnitRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restReportingUnitMockMvc;

    private ReportingUnit reportingUnit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportingUnitResource reportingUnitResource = new ReportingUnitResource();
        ReflectionTestUtils.setField(reportingUnitResource, "reportingUnitRepository", reportingUnitRepository);
        this.restReportingUnitMockMvc = MockMvcBuilders.standaloneSetup(reportingUnitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingUnit createEntity(EntityManager em) {
        ReportingUnit reportingUnit = new ReportingUnit()
                .uniqueReference(DEFAULT_UNIQUE_REFERENCE)
                .businessName(DEFAULT_BUSINESS_NAME);
        return reportingUnit;
    }

    @Before
    public void initTest() {
        reportingUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createReportingUnit() throws Exception {
        int databaseSizeBeforeCreate = reportingUnitRepository.findAll().size();

        // Create the ReportingUnit

        restReportingUnitMockMvc.perform(post("/api/reporting-units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reportingUnit)))
                .andExpect(status().isCreated());

        // Validate the ReportingUnit in the database
        List<ReportingUnit> reportingUnits = reportingUnitRepository.findAll();
        assertThat(reportingUnits).hasSize(databaseSizeBeforeCreate + 1);
        ReportingUnit testReportingUnit = reportingUnits.get(reportingUnits.size() - 1);
        assertThat(testReportingUnit.getUniqueReference()).isEqualTo(DEFAULT_UNIQUE_REFERENCE);
        assertThat(testReportingUnit.getBusinessName()).isEqualTo(DEFAULT_BUSINESS_NAME);
    }

    @Test
    @Transactional
    public void checkUniqueReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportingUnitRepository.findAll().size();
        // set the field null
        reportingUnit.setUniqueReference(null);

        // Create the ReportingUnit, which fails.

        restReportingUnitMockMvc.perform(post("/api/reporting-units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reportingUnit)))
                .andExpect(status().isBadRequest());

        List<ReportingUnit> reportingUnits = reportingUnitRepository.findAll();
        assertThat(reportingUnits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReportingUnits() throws Exception {
        // Initialize the database
        reportingUnitRepository.saveAndFlush(reportingUnit);

        // Get all the reportingUnits
        restReportingUnitMockMvc.perform(get("/api/reporting-units?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reportingUnit.getId().intValue())))
                .andExpect(jsonPath("$.[*].uniqueReference").value(hasItem(DEFAULT_UNIQUE_REFERENCE.toString())))
                .andExpect(jsonPath("$.[*].businessName").value(hasItem(DEFAULT_BUSINESS_NAME.toString())));
    }

    @Test
    @Transactional
    public void getReportingUnit() throws Exception {
        // Initialize the database
        reportingUnitRepository.saveAndFlush(reportingUnit);

        // Get the reportingUnit
        restReportingUnitMockMvc.perform(get("/api/reporting-units/{id}", reportingUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reportingUnit.getId().intValue()))
            .andExpect(jsonPath("$.uniqueReference").value(DEFAULT_UNIQUE_REFERENCE.toString()))
            .andExpect(jsonPath("$.businessName").value(DEFAULT_BUSINESS_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReportingUnit() throws Exception {
        // Get the reportingUnit
        restReportingUnitMockMvc.perform(get("/api/reporting-units/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportingUnit() throws Exception {
        // Initialize the database
        reportingUnitRepository.saveAndFlush(reportingUnit);
        int databaseSizeBeforeUpdate = reportingUnitRepository.findAll().size();

        // Update the reportingUnit
        ReportingUnit updatedReportingUnit = reportingUnitRepository.findOne(reportingUnit.getId());
        updatedReportingUnit
                .uniqueReference(UPDATED_UNIQUE_REFERENCE)
                .businessName(UPDATED_BUSINESS_NAME);

        restReportingUnitMockMvc.perform(put("/api/reporting-units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedReportingUnit)))
                .andExpect(status().isOk());

        // Validate the ReportingUnit in the database
        List<ReportingUnit> reportingUnits = reportingUnitRepository.findAll();
        assertThat(reportingUnits).hasSize(databaseSizeBeforeUpdate);
        ReportingUnit testReportingUnit = reportingUnits.get(reportingUnits.size() - 1);
        assertThat(testReportingUnit.getUniqueReference()).isEqualTo(UPDATED_UNIQUE_REFERENCE);
        assertThat(testReportingUnit.getBusinessName()).isEqualTo(UPDATED_BUSINESS_NAME);
    }

    @Test
    @Transactional
    public void deleteReportingUnit() throws Exception {
        // Initialize the database
        reportingUnitRepository.saveAndFlush(reportingUnit);
        int databaseSizeBeforeDelete = reportingUnitRepository.findAll().size();

        // Get the reportingUnit
        restReportingUnitMockMvc.perform(delete("/api/reporting-units/{id}", reportingUnit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ReportingUnit> reportingUnits = reportingUnitRepository.findAll();
        assertThat(reportingUnits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
