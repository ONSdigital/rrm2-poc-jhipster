package uk.gov.ons.rrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ons.rrm.domain.ReportingUnitAssociation;

import uk.gov.ons.rrm.repository.ReportingUnitAssociationRepository;
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
 * REST controller for managing ReportingUnitAssociation.
 */
@RestController
@RequestMapping("/api")
public class ReportingUnitAssociationResource {

    private final Logger log = LoggerFactory.getLogger(ReportingUnitAssociationResource.class);
        
    @Inject
    private ReportingUnitAssociationRepository reportingUnitAssociationRepository;

    /**
     * POST  /reporting-unit-associations : Create a new reportingUnitAssociation.
     *
     * @param reportingUnitAssociation the reportingUnitAssociation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reportingUnitAssociation, or with status 400 (Bad Request) if the reportingUnitAssociation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/reporting-unit-associations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportingUnitAssociation> createReportingUnitAssociation(@RequestBody ReportingUnitAssociation reportingUnitAssociation) throws URISyntaxException {
        log.debug("REST request to save ReportingUnitAssociation : {}", reportingUnitAssociation);
        if (reportingUnitAssociation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("reportingUnitAssociation", "idexists", "A new reportingUnitAssociation cannot already have an ID")).body(null);
        }
        ReportingUnitAssociation result = reportingUnitAssociationRepository.save(reportingUnitAssociation);
        return ResponseEntity.created(new URI("/api/reporting-unit-associations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("reportingUnitAssociation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reporting-unit-associations : Updates an existing reportingUnitAssociation.
     *
     * @param reportingUnitAssociation the reportingUnitAssociation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reportingUnitAssociation,
     * or with status 400 (Bad Request) if the reportingUnitAssociation is not valid,
     * or with status 500 (Internal Server Error) if the reportingUnitAssociation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/reporting-unit-associations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportingUnitAssociation> updateReportingUnitAssociation(@RequestBody ReportingUnitAssociation reportingUnitAssociation) throws URISyntaxException {
        log.debug("REST request to update ReportingUnitAssociation : {}", reportingUnitAssociation);
        if (reportingUnitAssociation.getId() == null) {
            return createReportingUnitAssociation(reportingUnitAssociation);
        }
        ReportingUnitAssociation result = reportingUnitAssociationRepository.save(reportingUnitAssociation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("reportingUnitAssociation", reportingUnitAssociation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reporting-unit-associations : get all the reportingUnitAssociations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reportingUnitAssociations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/reporting-unit-associations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ReportingUnitAssociation>> getAllReportingUnitAssociations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ReportingUnitAssociations");
        Page<ReportingUnitAssociation> page = reportingUnitAssociationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reporting-unit-associations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reporting-unit-associations/:id : get the "id" reportingUnitAssociation.
     *
     * @param id the id of the reportingUnitAssociation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reportingUnitAssociation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/reporting-unit-associations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportingUnitAssociation> getReportingUnitAssociation(@PathVariable Long id) {
        log.debug("REST request to get ReportingUnitAssociation : {}", id);
        ReportingUnitAssociation reportingUnitAssociation = reportingUnitAssociationRepository.findOne(id);
        return Optional.ofNullable(reportingUnitAssociation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reporting-unit-associations/:id : delete the "id" reportingUnitAssociation.
     *
     * @param id the id of the reportingUnitAssociation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/reporting-unit-associations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReportingUnitAssociation(@PathVariable Long id) {
        log.debug("REST request to delete ReportingUnitAssociation : {}", id);
        reportingUnitAssociationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("reportingUnitAssociation", id.toString())).build();
    }

}
