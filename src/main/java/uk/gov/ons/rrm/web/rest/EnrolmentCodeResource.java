package uk.gov.ons.rrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ons.rrm.domain.EnrolmentCode;

import uk.gov.ons.rrm.repository.EnrolmentCodeRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EnrolmentCode.
 */
@RestController
@RequestMapping("/api")
public class EnrolmentCodeResource {

    private final Logger log = LoggerFactory.getLogger(EnrolmentCodeResource.class);
        
    @Inject
    private EnrolmentCodeRepository enrolmentCodeRepository;

    /**
     * POST  /enrolment-codes : Create a new enrolmentCode.
     *
     * @param enrolmentCode the enrolmentCode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new enrolmentCode, or with status 400 (Bad Request) if the enrolmentCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enrolment-codes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EnrolmentCode> createEnrolmentCode(@Valid @RequestBody EnrolmentCode enrolmentCode) throws URISyntaxException {
        log.debug("REST request to save EnrolmentCode : {}", enrolmentCode);
        if (enrolmentCode.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("enrolmentCode", "idexists", "A new enrolmentCode cannot already have an ID")).body(null);
        }
        EnrolmentCode result = enrolmentCodeRepository.save(enrolmentCode);
        return ResponseEntity.created(new URI("/api/enrolment-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("enrolmentCode", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enrolment-codes : Updates an existing enrolmentCode.
     *
     * @param enrolmentCode the enrolmentCode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated enrolmentCode,
     * or with status 400 (Bad Request) if the enrolmentCode is not valid,
     * or with status 500 (Internal Server Error) if the enrolmentCode couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enrolment-codes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EnrolmentCode> updateEnrolmentCode(@Valid @RequestBody EnrolmentCode enrolmentCode) throws URISyntaxException {
        log.debug("REST request to update EnrolmentCode : {}", enrolmentCode);
        if (enrolmentCode.getId() == null) {
            return createEnrolmentCode(enrolmentCode);
        }
        EnrolmentCode result = enrolmentCodeRepository.save(enrolmentCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("enrolmentCode", enrolmentCode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enrolment-codes : get all the enrolmentCodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of enrolmentCodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/enrolment-codes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EnrolmentCode>> getAllEnrolmentCodes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EnrolmentCodes");
        Page<EnrolmentCode> page = enrolmentCodeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/enrolment-codes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /enrolment-codes/:id : get the "id" enrolmentCode.
     *
     * @param id the id of the enrolmentCode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the enrolmentCode, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/enrolment-codes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EnrolmentCode> getEnrolmentCode(@PathVariable Long id) {
        log.debug("REST request to get EnrolmentCode : {}", id);
        EnrolmentCode enrolmentCode = enrolmentCodeRepository.findOne(id);
        return Optional.ofNullable(enrolmentCode)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /enrolment-codes/:id : delete the "id" enrolmentCode.
     *
     * @param id the id of the enrolmentCode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/enrolment-codes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEnrolmentCode(@PathVariable Long id) {
        log.debug("REST request to delete EnrolmentCode : {}", id);
        enrolmentCodeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("enrolmentCode", id.toString())).build();
    }

}
