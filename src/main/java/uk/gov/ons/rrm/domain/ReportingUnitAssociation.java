package uk.gov.ons.rrm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import uk.gov.ons.rrm.domain.enumeration.AssociationStatusKind;

/**
 * A link between a Respondent and a Reporting Unit, which may be timebound. (E.g. employment, marriage).
 * 
 */
@ApiModel(description = ""
    + "A link between a Respondent and a Reporting Unit, which may be timebound. (E.g. employment, marriage)."
    + "")
@Entity
@Table(name = "reporting_unit_association")
public class ReportingUnitAssociation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "association_status")
    private AssociationStatusKind associationStatus;

    @ManyToOne
    private ReportingUnit reportingUnit;

    @ManyToOne
    private Respondent respondent;

    @OneToMany(mappedBy = "reportingUnitAssociation")
    @JsonIgnore
    private Set<Enrolment> enroleds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AssociationStatusKind getAssociationStatus() {
        return associationStatus;
    }

    public ReportingUnitAssociation associationStatus(AssociationStatusKind associationStatus) {
        this.associationStatus = associationStatus;
        return this;
    }

    public void setAssociationStatus(AssociationStatusKind associationStatus) {
        this.associationStatus = associationStatus;
    }

    public ReportingUnit getReportingUnit() {
        return reportingUnit;
    }

    public ReportingUnitAssociation reportingUnit(ReportingUnit reportingUnit) {
        this.reportingUnit = reportingUnit;
        return this;
    }

    public void setReportingUnit(ReportingUnit reportingUnit) {
        this.reportingUnit = reportingUnit;
    }

    public Respondent getRespondent() {
        return respondent;
    }

    public ReportingUnitAssociation respondent(Respondent respondent) {
        this.respondent = respondent;
        return this;
    }

    public void setRespondent(Respondent respondent) {
        this.respondent = respondent;
    }

    public Set<Enrolment> getEnroleds() {
        return enroleds;
    }

    public ReportingUnitAssociation enroleds(Set<Enrolment> enrolments) {
        this.enroleds = enrolments;
        return this;
    }

    public ReportingUnitAssociation addEnroled(Enrolment enrolment) {
        enroleds.add(enrolment);
        enrolment.setReportingUnitAssociation(this);
        return this;
    }

    public ReportingUnitAssociation removeEnroled(Enrolment enrolment) {
        enroleds.remove(enrolment);
        enrolment.setReportingUnitAssociation(null);
        return this;
    }

    public void setEnroleds(Set<Enrolment> enrolments) {
        this.enroleds = enrolments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportingUnitAssociation reportingUnitAssociation = (ReportingUnitAssociation) o;
        if(reportingUnitAssociation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reportingUnitAssociation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReportingUnitAssociation{" +
            "id=" + id +
            ", associationStatus='" + associationStatus + "'" +
            '}';
    }
}
