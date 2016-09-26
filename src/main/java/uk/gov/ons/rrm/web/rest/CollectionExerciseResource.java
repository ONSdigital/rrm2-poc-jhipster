package uk.gov.ons.rrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ons.rrm.domain.CollectionExercise;

import uk.gov.ons.rrm.repository.CollectionExerciseRepository;
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
 * REST controller for managing CollectionExercise.
 */
@RestController
@RequestMapping("/api")
public class CollectionExerciseResource {

    private final Logger log = LoggerFactory.getLogger(CollectionExerciseResource.class);
        
    @Inject
    private CollectionExerciseRepository collectionExerciseRepository;

    /**
     * POST  /collection-exercises : Create a new collectionExercise.
     *
     * @param collectionExercise the collectionExercise to create
     * @return the ResponseEntity with status 201 (Created) and with body the new collectionExercise, or with status 400 (Bad Request) if the collectionExercise has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/collection-exercises",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CollectionExercise> createCollectionExercise(@RequestBody CollectionExercise collectionExercise) throws URISyntaxException {
        log.debug("REST request to save CollectionExercise : {}", collectionExercise);
        if (collectionExercise.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("collectionExercise", "idexists", "A new collectionExercise cannot already have an ID")).body(null);
        }
        CollectionExercise result = collectionExerciseRepository.save(collectionExercise);
        return ResponseEntity.created(new URI("/api/collection-exercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("collectionExercise", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collection-exercises : Updates an existing collectionExercise.
     *
     * @param collectionExercise the collectionExercise to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated collectionExercise,
     * or with status 400 (Bad Request) if the collectionExercise is not valid,
     * or with status 500 (Internal Server Error) if the collectionExercise couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/collection-exercises",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CollectionExercise> updateCollectionExercise(@RequestBody CollectionExercise collectionExercise) throws URISyntaxException {
        log.debug("REST request to update CollectionExercise : {}", collectionExercise);
        if (collectionExercise.getId() == null) {
            return createCollectionExercise(collectionExercise);
        }
        CollectionExercise result = collectionExerciseRepository.save(collectionExercise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("collectionExercise", collectionExercise.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collection-exercises : get all the collectionExercises.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of collectionExercises in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/collection-exercises",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CollectionExercise>> getAllCollectionExercises(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CollectionExercises");
        Page<CollectionExercise> page = collectionExerciseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/collection-exercises");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /collection-exercises/:id : get the "id" collectionExercise.
     *
     * @param id the id of the collectionExercise to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the collectionExercise, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/collection-exercises/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CollectionExercise> getCollectionExercise(@PathVariable Long id) {
        log.debug("REST request to get CollectionExercise : {}", id);
        CollectionExercise collectionExercise = collectionExerciseRepository.findOne(id);
        return Optional.ofNullable(collectionExercise)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /collection-exercises/:id : delete the "id" collectionExercise.
     *
     * @param id the id of the collectionExercise to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/collection-exercises/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCollectionExercise(@PathVariable Long id) {
        log.debug("REST request to delete CollectionExercise : {}", id);
        collectionExerciseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("collectionExercise", id.toString())).build();
    }

}
