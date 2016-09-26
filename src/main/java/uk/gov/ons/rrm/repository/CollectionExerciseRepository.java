package uk.gov.ons.rrm.repository;

import uk.gov.ons.rrm.domain.CollectionExercise;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CollectionExercise entity.
 */
@SuppressWarnings("unused")
public interface CollectionExerciseRepository extends JpaRepository<CollectionExercise,Long> {

}
