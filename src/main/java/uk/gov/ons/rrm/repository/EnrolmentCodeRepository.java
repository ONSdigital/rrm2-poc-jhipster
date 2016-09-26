package uk.gov.ons.rrm.repository;

import uk.gov.ons.rrm.domain.EnrolmentCode;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EnrolmentCode entity.
 */
@SuppressWarnings("unused")
public interface EnrolmentCodeRepository extends JpaRepository<EnrolmentCode,Long> {

}
