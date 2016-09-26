package uk.gov.ons.rrm.repository;

import uk.gov.ons.rrm.domain.CollectionInstrument;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CollectionInstrument entity.
 */
@SuppressWarnings("unused")
public interface CollectionInstrumentRepository extends JpaRepository<CollectionInstrument,Long> {

}
