package uk.gov.ons.rrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ons.rrm.domain.SampleSelection;

import uk.gov.ons.rrm.repository.SampleSelectionRepository;
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
 * REST controller for managing SampleSelection.
 */
@RestController
@RequestMapping("/api")
public class SampleSelectionResource {

    private final Logger log = LoggerFactory.getLogger(SampleSelectionResource.class);
        
    @Inject
    private SampleSelectionRepository sampleSelectionRepository;

    /**
     * POST  /sample-selections : Create a new sampleSelection.
     *
     * @param sampleSelection the sampleSelection to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sampleSelection, or with status 400 (Bad Request) if the sampleSelection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sample-selections",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SampleSelection> createSampleSelection(@RequestBody SampleSelection sampleSelection) throws URISyntaxException {
        log.debug("REST request to save SampleSelection : {}", sampleSelection);
        if (sampleSelection.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sampleSelection", "idexists", "A new sampleSelection cannot already have an ID")).body(null);
        }
        SampleSelection result = sampleSelectionRepository.save(sampleSelection);
        return ResponseEntity.created(new URI("/api/sample-selections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sampleSelection", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sample-selections : Updates an existing sampleSelection.
     *
     * @param sampleSelection the sampleSelection to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sampleSelection,
     * or with status 400 (Bad Request) if the sampleSelection is not valid,
     * or with status 500 (Internal Server Error) if the sampleSelection couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sample-selections",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SampleSelection> updateSampleSelection(@RequestBody SampleSelection sampleSelection) throws URISyntaxException {
        log.debug("REST request to update SampleSelection : {}", sampleSelection);
        if (sampleSelection.getId() == null) {
            return createSampleSelection(sampleSelection);
        }
        SampleSelection result = sampleSelectionRepository.save(sampleSelection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sampleSelection", sampleSelection.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sample-selections : get all the sampleSelections.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sampleSelections in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/sample-selections",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SampleSelection>> getAllSampleSelections(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SampleSelections");
        Page<SampleSelection> page = sampleSelectionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sample-selections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sample-selections/:id : get the "id" sampleSelection.
     *
     * @param id the id of the sampleSelection to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sampleSelection, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sample-selections/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SampleSelection> getSampleSelection(@PathVariable Long id) {
        log.debug("REST request to get SampleSelection : {}", id);
        SampleSelection sampleSelection = sampleSelectionRepository.findOne(id);
        return Optional.ofNullable(sampleSelection)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sample-selections/:id : delete the "id" sampleSelection.
     *
     * @param id the id of the sampleSelection to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sample-selections/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSampleSelection(@PathVariable Long id) {
        log.debug("REST request to delete SampleSelection : {}", id);
        sampleSelectionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sampleSelection", id.toString())).build();
    }

}
