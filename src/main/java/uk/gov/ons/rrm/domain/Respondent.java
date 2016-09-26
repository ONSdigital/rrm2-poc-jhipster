package uk.gov.ons.rrm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Respondent.
 */
@Entity
@Table(name = "respondent")
public class Respondent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "respondent")
    @JsonIgnore
    private Set<ReportingUnitAssociation> associatedWiths = new HashSet<>();

    @OneToMany(mappedBy = "respondent")
    @JsonIgnore
    private Set<Enrolment> enroleds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Respondent emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public Respondent firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Respondent lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<ReportingUnitAssociation> getAssociatedWiths() {
        return associatedWiths;
    }

    public Respondent associatedWiths(Set<ReportingUnitAssociation> reportingUnitAssociations) {
        this.associatedWiths = reportingUnitAssociations;
        return this;
    }

    public Respondent addAssociatedWith(ReportingUnitAssociation reportingUnitAssociation) {
        associatedWiths.add(reportingUnitAssociation);
        reportingUnitAssociation.setRespondent(this);
        return this;
    }

    public Respondent removeAssociatedWith(ReportingUnitAssociation reportingUnitAssociation) {
        associatedWiths.remove(reportingUnitAssociation);
        reportingUnitAssociation.setRespondent(null);
        return this;
    }

    public void setAssociatedWiths(Set<ReportingUnitAssociation> reportingUnitAssociations) {
        this.associatedWiths = reportingUnitAssociations;
    }

    public Set<Enrolment> getEnroleds() {
        return enroleds;
    }

    public Respondent enroleds(Set<Enrolment> enrolments) {
        this.enroleds = enrolments;
        return this;
    }

    public Respondent addEnroled(Enrolment enrolment) {
        enroleds.add(enrolment);
        enrolment.setRespondent(this);
        return this;
    }

    public Respondent removeEnroled(Enrolment enrolment) {
        enroleds.remove(enrolment);
        enrolment.setRespondent(null);
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
        Respondent respondent = (Respondent) o;
        if(respondent.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, respondent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Respondent{" +
            "id=" + id +
            ", emailAddress='" + emailAddress + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            '}';
    }
}
