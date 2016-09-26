package uk.gov.ons.rrm.web.rest;

import uk.gov.ons.rrm.RrmApp;

import uk.gov.ons.rrm.domain.ReportingUnitAssociation;
import uk.gov.ons.rrm.repository.ReportingUnitAssociationRepository;

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

import uk.gov.ons.rrm.domain.enumeration.AssociationStatusKind;
/**
 * Test class for the ReportingUnitAssociationResource REST controller.
 *
 * @see ReportingUnitAssociationResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = RrmApp.class)

public class ReportingUnitAssociationResourceIntTest {

    private static final AssociationStatusKind DEFAULT_ASSOCIATION_STATUS = AssociationStatusKind.ACTIVE;
    private static final AssociationStatusKind UPDATED_ASSOCIATION_STATUS = AssociationStatusKind.SUSPENDED;

    @Inject
    private ReportingUnitAssociationRepository reportingUnitAssociationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restReportingUnitAssociationMockMvc;

    private ReportingUnitAssociation reportingUnitAssociation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportingUnitAssociationResource reportingUnitAssociationResource = new ReportingUnitAssociationResource();
        ReflectionTestUtils.setField(reportingUnitAssociationResource, "reportingUnitAssociationRepository", reportingUnitAssociationRepository);
        this.restReportingUnitAssociationMockMvc = MockMvcBuilders.standaloneSetup(reportingUnitAssociationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingUnitAssociation createEntity(EntityManager em) {
        ReportingUnitAssociation reportingUnitAssociation = new ReportingUnitAssociation()
                .associationStatus(DEFAULT_ASSOCIATION_STATUS);
        return reportingUnitAssociation;
    }

    @Before
    public void initTest() {
        reportingUnitAssociation = createEntity(em);
    }

    @Test
    @Transactional
    public void createReportingUnitAssociation() throws Exception {
        int databaseSizeBeforeCreate = reportingUnitAssociationRepository.findAll().size();

        // Create the ReportingUnitAssociation

        restReportingUnitAssociationMockMvc.perform(post("/api/reporting-unit-associations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reportingUnitAssociation)))
                .andExpect(status().isCreated());

        // Validate the ReportingUnitAssociation in the database
        List<ReportingUnitAssociation> reportingUnitAssociations = reportingUnitAssociationRepository.findAll();
        assertThat(reportingUnitAssociations).hasSize(databaseSizeBeforeCreate + 1);
        ReportingUnitAssociation testReportingUnitAssociation = reportingUnitAssociations.get(reportingUnitAssociations.size() - 1);
        assertThat(testReportingUnitAssociation.getAssociationStatus()).isEqualTo(DEFAULT_ASSOCIATION_STATUS);
    }

    @Test
    @Transactional
    public void getAllReportingUnitAssociations() throws Exception {
        // Initialize the database
        reportingUnitAssociationRepository.saveAndFlush(reportingUnitAssociation);

        // Get all the reportingUnitAssociations
        restReportingUnitAssociationMockMvc.perform(get("/api/reporting-unit-associations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reportingUnitAssociation.getId().intValue())))
                .andExpect(jsonPath("$.[*].associationStatus").value(hasItem(DEFAULT_ASSOCIATION_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getReportingUnitAssociation() throws Exception {
        // Initialize the database
        reportingUnitAssociationRepository.saveAndFlush(reportingUnitAssociation);

        // Get the reportingUnitAssociation
        restReportingUnitAssociationMockMvc.perform(get("/api/reporting-unit-associations/{id}", reportingUnitAssociation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reportingUnitAssociation.getId().intValue()))
            .andExpect(jsonPath("$.associationStatus").value(DEFAULT_ASSOCIATION_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReportingUnitAssociation() throws Exception {
        // Get the reportingUnitAssociation
        restReportingUnitAssociationMockMvc.perform(get("/api/reporting-unit-associations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportingUnitAssociation() throws Exception {
        // Initialize the database
        reportingUnitAssociationRepository.saveAndFlush(reportingUnitAssociation);
        int databaseSizeBeforeUpdate = reportingUnitAssociationRepository.findAll().size();

        // Update the reportingUnitAssociation
        ReportingUnitAssociation updatedReportingUnitAssociation = reportingUnitAssociationRepository.findOne(reportingUnitAssociation.getId());
        updatedReportingUnitAssociation
                .associationStatus(UPDATED_ASSOCIATION_STATUS);

        restReportingUnitAssociationMockMvc.perform(put("/api/reporting-unit-associations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedReportingUnitAssociation)))
                .andExpect(status().isOk());

        // Validate the ReportingUnitAssociation in the database
        List<ReportingUnitAssociation> reportingUnitAssociations = reportingUnitAssociationRepository.findAll();
        assertThat(reportingUnitAssociations).hasSize(databaseSizeBeforeUpdate);
        ReportingUnitAssociation testReportingUnitAssociation = reportingUnitAssociations.get(reportingUnitAssociations.size() - 1);
        assertThat(testReportingUnitAssociation.getAssociationStatus()).isEqualTo(UPDATED_ASSOCIATION_STATUS);
    }

    @Test
    @Transactional
    public void deleteReportingUnitAssociation() throws Exception {
        // Initialize the database
        reportingUnitAssociationRepository.saveAndFlush(reportingUnitAssociation);
        int databaseSizeBeforeDelete = reportingUnitAssociationRepository.findAll().size();

        // Get the reportingUnitAssociation
        restReportingUnitAssociationMockMvc.perform(delete("/api/reporting-unit-associations/{id}", reportingUnitAssociation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ReportingUnitAssociation> reportingUnitAssociations = reportingUnitAssociationRepository.findAll();
        assertThat(reportingUnitAssociations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
