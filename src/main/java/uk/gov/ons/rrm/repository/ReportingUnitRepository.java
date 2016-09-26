package uk.gov.ons.rrm.repository;

import uk.gov.ons.rrm.domain.ReportingUnit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReportingUnit entity.
 */
@SuppressWarnings("unused")
public interface ReportingUnitRepository extends JpaRepository<ReportingUnit,Long> {

}
