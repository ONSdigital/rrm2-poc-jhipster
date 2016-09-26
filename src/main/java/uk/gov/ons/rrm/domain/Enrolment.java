package uk.gov.ons.rrm.domain;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import uk.gov.ons.rrm.domain.enumeration.EnrolmentStatusKind;

/**
 * Permission granted to provide data for a particular Survey on behalfof a Reporting Unit, by virtue of the nature of a related RespondentUnit Association.
 * 
 */
@ApiModel(description = ""
    + "Permission granted to provide data for a particular Survey on behalfof a Reporting Unit, by virtue of the nature of a related RespondentUnit Association."
    + "")
@Entity
@Table(name = "enrolment")
public class Enrolment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrolment_status")
    private EnrolmentStatusKind enrolmentStatus;

    @ManyToOne
    private Respondent respondent;

    @ManyToOne
    private ReportingUnitAssociation reportingUnitAssociation;

    @OneToOne
    @JoinColumn(unique = true)
    private Survey participatesIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnrolmentStatusKind getEnrolmentStatus() {
        return enrolmentStatus;
    }

    public Enrolment enrolmentStatus(EnrolmentStatusKind enrolmentStatus) {
        this.enrolmentStatus = enrolmentStatus;
        return this;
    }

    public void setEnrolmentStatus(EnrolmentStatusKind enrolmentStatus) {
        this.enrolmentStatus = enrolmentStatus;
    }

    public Respondent getRespondent() {
        return respondent;
    }

    public Enrolment respondent(Respondent respondent) {
        this.respondent = respondent;
        return this;
    }

    public void setRespondent(Respondent respondent) {
        this.respondent = respondent;
    }

    public ReportingUnitAssociation getReportingUnitAssociation() {
        return reportingUnitAssociation;
    }

    public Enrolment reportingUnitAssociation(ReportingUnitAssociation reportingUnitAssociation) {
        this.reportingUnitAssociation = reportingUnitAssociation;
        return this;
    }

    public void setReportingUnitAssociation(ReportingUnitAssociation reportingUnitAssociation) {
        this.reportingUnitAssociation = reportingUnitAssociation;
    }

    public Survey getParticipatesIn() {
        return participatesIn;
    }

    public Enrolment participatesIn(Survey survey) {
        this.participatesIn = survey;
        return this;
    }

    public void setParticipatesIn(Survey survey) {
        this.participatesIn = survey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enrolment enrolment = (Enrolment) o;
        if(enrolment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, enrolment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Enrolment{" +
            "id=" + id +
            ", enrolmentStatus='" + enrolmentStatus + "'" +
            '}';
    }
}
