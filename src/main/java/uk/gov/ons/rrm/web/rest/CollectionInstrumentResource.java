package uk.gov.ons.rrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ons.rrm.domain.CollectionInstrument;

import uk.gov.ons.rrm.repository.CollectionInstrumentRepository;
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
 * REST controller for managing CollectionInstrument.
 */
@RestController
@RequestMapping("/api")
public class CollectionInstrumentResource {

    private final Logger log = LoggerFactory.getLogger(CollectionInstrumentResource.class);
        
    @Inject
    private CollectionInstrumentRepository collectionInstrumentRepository;

    /**
     * POST  /collection-instruments : Create a new collectionInstrument.
     *
     * @param collectionInstrument the collectionInstrument to create
     * @return the ResponseEntity with status 201 (Created) and with body the new collectionInstrument, or with status 400 (Bad Request) if the collectionInstrument has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/collection-instruments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CollectionInstrument> createCollectionInstrument(@RequestBody CollectionInstrument collectionInstrument) throws URISyntaxException {
        log.debug("REST request to save CollectionInstrument : {}", collectionInstrument);
        if (collectionInstrument.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("collectionInstrument", "idexists", "A new collectionInstrument cannot already have an ID")).body(null);
        }
        CollectionInstrument result = collectionInstrumentRepository.save(collectionInstrument);
        return ResponseEntity.created(new URI("/api/collection-instruments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("collectionInstrument", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collection-instruments : Updates an existing collectionInstrument.
     *
     * @param collectionInstrument the collectionInstrument to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated collectionInstrument,
     * or with status 400 (Bad Request) if the collectionInstrument is not valid,
     * or with status 500 (Internal Server Error) if the collectionInstrument couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/collection-instruments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CollectionInstrument> updateCollectionInstrument(@RequestBody CollectionInstrument collectionInstrument) throws URISyntaxException {
        log.debug("REST request to update CollectionInstrument : {}", collectionInstrument);
        if (collectionInstrument.getId() == null) {
            return createCollectionInstrument(collectionInstrument);
        }
        CollectionInstrument result = collectionInstrumentRepository.save(collectionInstrument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("collectionInstrument", collectionInstrument.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collection-instruments : get all the collectionInstruments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of collectionInstruments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/collection-instruments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CollectionInstrument>> getAllCollectionInstruments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CollectionInstruments");
        Page<CollectionInstrument> page = collectionInstrumentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/collection-instruments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /collection-instruments/:id : get the "id" collectionInstrument.
     *
     * @param id the id of the collectionInstrument to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the collectionInstrument, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/collection-instruments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CollectionInstrument> getCollectionInstrument(@PathVariable Long id) {
        log.debug("REST request to get CollectionInstrument : {}", id);
        CollectionInstrument collectionInstrument = collectionInstrumentRepository.findOne(id);
        return Optional.ofNullable(collectionInstrument)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /collection-instruments/:id : delete the "id" collectionInstrument.
     *
     * @param id the id of the collectionInstrument to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/collection-instruments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCollectionInstrument(@PathVariable Long id) {
        log.debug("REST request to delete CollectionInstrument : {}", id);
        collectionInstrumentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("collectionInstrument", id.toString())).build();
    }

}
