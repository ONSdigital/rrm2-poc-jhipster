package uk.gov.ons.rrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ons.rrm.domain.Enrolment;
import uk.gov.ons.rrm.domain.Respondent;

import uk.gov.ons.rrm.repository.EnrolmentRepository;
import uk.gov.ons.rrm.repository.RespondentRepository;
import uk.gov.ons.rrm.web.rest.util.HeaderUtil;
import uk.gov.ons.rrm.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Enrolment.
 */
@RestController
@RequestMapping("/api")
public class EnrolmentResource {

    private final Logger log = LoggerFactory.getLogger(EnrolmentResource.class);
        
    @Inject
    private EnrolmentRepository enrolmentRepository;

    @Inject
    private RespondentRepository respondentRepository;

    /**
     * POST  /enrolments : Create a new enrolment.
     *
     * @param enrolment the enrolment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new enrolment, or with status 400 (Bad Request) if the enrolment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enrolments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enrolment> createEnrolment(@RequestBody Enrolment enrolment) throws URISyntaxException {
        log.debug("REST request to save Enrolment : {}", enrolment);
        if (enrolment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("enrolment", "idexists", "A new enrolment cannot already have an ID")).body(null);
        }
        Enrolment result = enrolmentRepository.save(enrolment);
        return ResponseEntity.created(new URI("/api/enrolments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("enrolment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enrolments : Updates an existing enrolment.
     *
     * @param enrolment the enrolment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated enrolment,
     * or with status 400 (Bad Request) if the enrolment is not valid,
     * or with status 500 (Internal Server Error) if the enrolment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enrolments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enrolment> updateEnrolment(@RequestBody Enrolment enrolment) throws URISyntaxException {
        log.debug("REST request to update Enrolment : {}", enrolment);
        if (enrolment.getId() == null) {
            return createEnrolment(enrolment);
        }
        Enrolment result = enrolmentRepository.save(enrolment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("enrolment", enrolment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enrolments : get all the enrolments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of enrolments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/enrolments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Enrolment>> getAllEnrolments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Enrolments");
        Page<Enrolment> page = enrolmentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/enrolments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /enrolments/:id : get the "id" enrolment.
     *
     * @param id the id of the enrolment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the enrolment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/enrolments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enrolment> getEnrolment(@PathVariable Long id) {
        log.debug("REST request to get Enrolment : {}", id);
        Enrolment enrolment = enrolmentRepository.findOne(id);
        return Optional.ofNullable(enrolment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /respondents/:id/enrolments : get the "id" enrolment.
     *
     * @param id the id of the enrolment to retrieve
     * @return the ResponseEntity with status 200 (OK) or with status 404 (Not Found)
     */
    @RequestMapping(value = "/respondents/{respondentId}/enrolments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Enrolment>> getEnrolmentsByRespondent(@PathVariable Long respondentId, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get Enrolments for Respondent: {}", respondentId);
        Respondent respondent = respondentRepository.findOne(respondentId);
	if(respondent == null) {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	} else {
        	Page<Enrolment> page = enrolmentRepository.findByRespondent(respondent, pageable);
        	HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/respondents/" + respondentId + "/enrolments");
        	return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

    }

    /**
     * DELETE  /enrolments/:id : delete the "id" enrolment.
     *
     * @param id the id of the enrolment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/enrolments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEnrolment(@PathVariable Long id) {
        log.debug("REST request to delete Enrolment : {}", id);
        enrolmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("enrolment", id.toString())).build();
    }

}
