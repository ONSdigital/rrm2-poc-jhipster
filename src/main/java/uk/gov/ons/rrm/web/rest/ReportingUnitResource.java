package uk.gov.ons.rrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ons.rrm.domain.ReportingUnit;

import uk.gov.ons.rrm.repository.ReportingUnitRepository;
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
 * REST controller for managing ReportingUnit.
 */
@RestController
@RequestMapping("/api")
public class ReportingUnitResource {

    private final Logger log = LoggerFactory.getLogger(ReportingUnitResource.class);
        
    @Inject
    private ReportingUnitRepository reportingUnitRepository;

    /**
     * POST  /reporting-units : Create a new reportingUnit.
     *
     * @param reportingUnit the reportingUnit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reportingUnit, or with status 400 (Bad Request) if the reportingUnit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/reporting-units",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportingUnit> createReportingUnit(@Valid @RequestBody ReportingUnit reportingUnit) throws URISyntaxException {
        log.debug("REST request to save ReportingUnit : {}", reportingUnit);
        if (reportingUnit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("reportingUnit", "idexists", "A new reportingUnit cannot already have an ID")).body(null);
        }
        ReportingUnit result = reportingUnitRepository.save(reportingUnit);
        return ResponseEntity.created(new URI("/api/reporting-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("reportingUnit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reporting-units : Updates an existing reportingUnit.
     *
     * @param reportingUnit the reportingUnit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reportingUnit,
     * or with status 400 (Bad Request) if the reportingUnit is not valid,
     * or with status 500 (Internal Server Error) if the reportingUnit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/reporting-units",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportingUnit> updateReportingUnit(@Valid @RequestBody ReportingUnit reportingUnit) throws URISyntaxException {
        log.debug("REST request to update ReportingUnit : {}", reportingUnit);
        if (reportingUnit.getId() == null) {
            return createReportingUnit(reportingUnit);
        }
        ReportingUnit result = reportingUnitRepository.save(reportingUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("reportingUnit", reportingUnit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reporting-units : get all the reportingUnits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reportingUnits in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/reporting-units",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ReportingUnit>> getAllReportingUnits(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ReportingUnits");
        Page<ReportingUnit> page = reportingUnitRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reporting-units");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reporting-units/:id : get the "id" reportingUnit.
     *
     * @param id the id of the reportingUnit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reportingUnit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/reporting-units/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportingUnit> getReportingUnit(@PathVariable Long id) {
        log.debug("REST request to get ReportingUnit : {}", id);
        ReportingUnit reportingUnit = reportingUnitRepository.findOne(id);
        return Optional.ofNullable(reportingUnit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reporting-units/:id : delete the "id" reportingUnit.
     *
     * @param id the id of the reportingUnit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/reporting-units/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReportingUnit(@PathVariable Long id) {
        log.debug("REST request to delete ReportingUnit : {}", id);
        reportingUnitRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("reportingUnit", id.toString())).build();
    }

}
