package uk.gov.ons.rrm.repository;

import uk.gov.ons.rrm.domain.ReportingUnitAssociation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReportingUnitAssociation entity.
 */
@SuppressWarnings("unused")
public interface ReportingUnitAssociationRepository extends JpaRepository<ReportingUnitAssociation,Long> {

}
