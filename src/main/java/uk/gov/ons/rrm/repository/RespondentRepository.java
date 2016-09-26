package uk.gov.ons.rrm.repository;

import uk.gov.ons.rrm.domain.Respondent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Respondent entity.
 */
@SuppressWarnings("unused")
public interface RespondentRepository extends JpaRepository<Respondent,Long> {

}
