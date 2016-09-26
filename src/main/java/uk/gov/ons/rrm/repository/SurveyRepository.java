package uk.gov.ons.rrm.repository;

import uk.gov.ons.rrm.domain.Survey;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Survey entity.
 */
@SuppressWarnings("unused")
public interface SurveyRepository extends JpaRepository<Survey,Long> {

}
