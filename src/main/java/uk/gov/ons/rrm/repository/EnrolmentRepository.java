package uk.gov.ons.rrm.repository;

import uk.gov.ons.rrm.domain.Enrolment;
import uk.gov.ons.rrm.domain.Respondent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Enrolment entity.
 */
@SuppressWarnings("unused")
public interface EnrolmentRepository extends JpaRepository<Enrolment,Long> {

	// @Query("select id, enrolmentStatus, respondent, reportingUnitAssociation, participatesIn from Enrolment where respondent = ?1")
	Page<Enrolment> findByRespondent(@Param("respondent") Respondent respondent, Pageable pageable);

}
