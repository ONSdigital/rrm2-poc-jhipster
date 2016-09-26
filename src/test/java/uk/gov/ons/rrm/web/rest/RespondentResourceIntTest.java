package uk.gov.ons.rrm.web.rest;

import uk.gov.ons.rrm.RrmApp;

import uk.gov.ons.rrm.domain.Respondent;
import uk.gov.ons.rrm.repository.RespondentRepository;

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
 * Test class for the RespondentResource REST controller.
 *
 * @see RespondentResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = RrmApp.class)

public class RespondentResourceIntTest {
    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBB";
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    @Inject
    private RespondentRepository respondentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRespondentMockMvc;

    private Respondent respondent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RespondentResource respondentResource = new RespondentResource();
        ReflectionTestUtils.setField(respondentResource, "respondentRepository", respondentRepository);
        this.restRespondentMockMvc = MockMvcBuilders.standaloneSetup(respondentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Respondent createEntity(EntityManager em) {
        Respondent respondent = new Respondent()
                .emailAddress(DEFAULT_EMAIL_ADDRESS)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME);
        return respondent;
    }

    @Before
    public void initTest() {
        respondent = createEntity(em);
    }

    @Test
    @Transactional
    public void createRespondent() throws Exception {
        int databaseSizeBeforeCreate = respondentRepository.findAll().size();

        // Create the Respondent

        restRespondentMockMvc.perform(post("/api/respondents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(respondent)))
                .andExpect(status().isCreated());

        // Validate the Respondent in the database
        List<Respondent> respondents = respondentRepository.findAll();
        assertThat(respondents).hasSize(databaseSizeBeforeCreate + 1);
        Respondent testRespondent = respondents.get(respondents.size() - 1);
        assertThat(testRespondent.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testRespondent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testRespondent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    public void checkEmailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = respondentRepository.findAll().size();
        // set the field null
        respondent.setEmailAddress(null);

        // Create the Respondent, which fails.

        restRespondentMockMvc.perform(post("/api/respondents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(respondent)))
                .andExpect(status().isBadRequest());

        List<Respondent> respondents = respondentRepository.findAll();
        assertThat(respondents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRespondents() throws Exception {
        // Initialize the database
        respondentRepository.saveAndFlush(respondent);

        // Get all the respondents
        restRespondentMockMvc.perform(get("/api/respondents?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(respondent.getId().intValue())))
                .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRespondent() throws Exception {
        // Initialize the database
        respondentRepository.saveAndFlush(respondent);

        // Get the respondent
        restRespondentMockMvc.perform(get("/api/respondents/{id}", respondent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(respondent.getId().intValue()))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRespondent() throws Exception {
        // Get the respondent
        restRespondentMockMvc.perform(get("/api/respondents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRespondent() throws Exception {
        // Initialize the database
        respondentRepository.saveAndFlush(respondent);
        int databaseSizeBeforeUpdate = respondentRepository.findAll().size();

        // Update the respondent
        Respondent updatedRespondent = respondentRepository.findOne(respondent.getId());
        updatedRespondent
                .emailAddress(UPDATED_EMAIL_ADDRESS)
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME);

        restRespondentMockMvc.perform(put("/api/respondents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRespondent)))
                .andExpect(status().isOk());

        // Validate the Respondent in the database
        List<Respondent> respondents = respondentRepository.findAll();
        assertThat(respondents).hasSize(databaseSizeBeforeUpdate);
        Respondent testRespondent = respondents.get(respondents.size() - 1);
        assertThat(testRespondent.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testRespondent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testRespondent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void deleteRespondent() throws Exception {
        // Initialize the database
        respondentRepository.saveAndFlush(respondent);
        int databaseSizeBeforeDelete = respondentRepository.findAll().size();

        // Get the respondent
        restRespondentMockMvc.perform(delete("/api/respondents/{id}", respondent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Respondent> respondents = respondentRepository.findAll();
        assertThat(respondents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
