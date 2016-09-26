package uk.gov.ons.rrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ons.rrm.domain.Respondent;

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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Respondent.
 */
@RestController
@RequestMapping("/api")
public class RespondentResource {

    private final Logger log = LoggerFactory.getLogger(RespondentResource.class);
        
    @Inject
    private RespondentRepository respondentRepository;

    /**
     * POST  /respondents : Create a new respondent.
     *
     * @param respondent the respondent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new respondent, or with status 400 (Bad Request) if the respondent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/respondents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Respondent> createRespondent(@Valid @RequestBody Respondent respondent) throws URISyntaxException {
        log.debug("REST request to save Respondent : {}", respondent);
        if (respondent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("respondent", "idexists", "A new respondent cannot already have an ID")).body(null);
        }
        Respondent result = respondentRepository.save(respondent);
        return ResponseEntity.created(new URI("/api/respondents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("respondent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /respondents : Updates an existing respondent.
     *
     * @param respondent the respondent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated respondent,
     * or with status 400 (Bad Request) if the respondent is not valid,
     * or with status 500 (Internal Server Error) if the respondent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/respondents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Respondent> updateRespondent(@Valid @RequestBody Respondent respondent) throws URISyntaxException {
        log.debug("REST request to update Respondent : {}", respondent);
        if (respondent.getId() == null) {
            return createRespondent(respondent);
        }
        Respondent result = respondentRepository.save(respondent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("respondent", respondent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /respondents : get all the respondents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of respondents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/respondents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Respondent>> getAllRespondents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Respondents");
        Page<Respondent> page = respondentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/respondents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /respondents/:id : get the "id" respondent.
     *
     * @param id the id of the respondent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the respondent, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/respondents/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Respondent> getRespondent(@PathVariable Long id) {
        log.debug("REST request to get Respondent : {}", id);
        Respondent respondent = respondentRepository.findOne(id);
        return Optional.ofNullable(respondent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /respondents/:id : delete the "id" respondent.
     *
     * @param id the id of the respondent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/respondents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRespondent(@PathVariable Long id) {
        log.debug("REST request to delete Respondent : {}", id);
        respondentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("respondent", id.toString())).build();
    }

}
