package uk.gov.ons.rrm.repository;

import uk.gov.ons.rrm.domain.CollectionResponse;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CollectionResponse entity.
 */
@SuppressWarnings("unused")
public interface CollectionResponseRepository extends JpaRepository<CollectionResponse,Long> {

}
