package uk.gov.ons.rrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ons.rrm.domain.CollectionResponse;

import uk.gov.ons.rrm.repository.CollectionResponseRepository;
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
 * REST controller for managing CollectionResponse.
 */
@RestController
@RequestMapping("/api")
public class CollectionResponseResource {

    private final Logger log = LoggerFactory.getLogger(CollectionResponseResource.class);
        
    @Inject
    private CollectionResponseRepository collectionResponseRepository;

    /**
     * POST  /collection-responses : Create a new collectionResponse.
     *
     * @param collectionResponse the collectionResponse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new collectionResponse, or with status 400 (Bad Request) if the collectionResponse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/collection-responses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CollectionResponse> createCollectionResponse(@RequestBody CollectionResponse collectionResponse) throws URISyntaxException {
        log.debug("REST request to save CollectionResponse : {}", collectionResponse);
        if (collectionResponse.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("collectionResponse", "idexists", "A new collectionResponse cannot already have an ID")).body(null);
        }
        CollectionResponse result = collectionResponseRepository.save(collectionResponse);
        return ResponseEntity.created(new URI("/api/collection-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("collectionResponse", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collection-responses : Updates an existing collectionResponse.
     *
     * @param collectionResponse the collectionResponse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated collectionResponse,
     * or with status 400 (Bad Request) if the collectionResponse is not valid,
     * or with status 500 (Internal Server Error) if the collectionResponse couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/collection-responses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CollectionResponse> updateCollectionResponse(@RequestBody CollectionResponse collectionResponse) throws URISyntaxException {
        log.debug("REST request to update CollectionResponse : {}", collectionResponse);
        if (collectionResponse.getId() == null) {
            return createCollectionResponse(collectionResponse);
        }
        CollectionResponse result = collectionResponseRepository.save(collectionResponse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("collectionResponse", collectionResponse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collection-responses : get all the collectionResponses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of collectionResponses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/collection-responses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CollectionResponse>> getAllCollectionResponses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CollectionResponses");
        Page<CollectionResponse> page = collectionResponseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/collection-responses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /collection-responses/:id : get the "id" collectionResponse.
     *
     * @param id the id of the collectionResponse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the collectionResponse, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/collection-responses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CollectionResponse> getCollectionResponse(@PathVariable Long id) {
        log.debug("REST request to get CollectionResponse : {}", id);
        CollectionResponse collectionResponse = collectionResponseRepository.findOne(id);
        return Optional.ofNullable(collectionResponse)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /collection-responses/:id : delete the "id" collectionResponse.
     *
     * @param id the id of the collectionResponse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/collection-responses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCollectionResponse(@PathVariable Long id) {
        log.debug("REST request to delete CollectionResponse : {}", id);
        collectionResponseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("collectionResponse", id.toString())).build();
    }

}
