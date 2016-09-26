package uk.gov.ons.rrm.repository;

import uk.gov.ons.rrm.domain.SampleSelection;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SampleSelection entity.
 */
@SuppressWarnings("unused")
public interface SampleSelectionRepository extends JpaRepository<SampleSelection,Long> {

}
