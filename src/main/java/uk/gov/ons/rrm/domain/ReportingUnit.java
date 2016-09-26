package uk.gov.ons.rrm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Statistical Unit approached in connection with provision of a Response(potentially on behalf of other Statistical Units as well as itself).May be an individual or organisation.
 * 
 */
@ApiModel(description = ""
    + "A Statistical Unit approached in connection with provision of a Response(potentially on behalf of other Statistical Units as well as itself).May be an individual or organisation."
    + "")
@Entity
@Table(name = "reporting_unit")
public class ReportingUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "unique_reference", nullable = false)
    private String uniqueReference;

    @Column(name = "business_name")
    private String businessName;

    @OneToMany(mappedBy = "reportingUnit")
    @JsonIgnore
    private Set<ReportingUnitAssociation> associatedWiths = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueReference() {
        return uniqueReference;
    }

    public ReportingUnit uniqueReference(String uniqueReference) {
        this.uniqueReference = uniqueReference;
        return this;
    }

    public void setUniqueReference(String uniqueReference) {
        this.uniqueReference = uniqueReference;
    }

    public String getBusinessName() {
        return businessName;
    }

    public ReportingUnit businessName(String businessName) {
        this.businessName = businessName;
        return this;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Set<ReportingUnitAssociation> getAssociatedWiths() {
        return associatedWiths;
    }

    public ReportingUnit associatedWiths(Set<ReportingUnitAssociation> reportingUnitAssociations) {
        this.associatedWiths = reportingUnitAssociations;
        return this;
    }

    public ReportingUnit addAssociatedWith(ReportingUnitAssociation reportingUnitAssociation) {
        associatedWiths.add(reportingUnitAssociation);
        reportingUnitAssociation.setReportingUnit(this);
        return this;
    }

    public ReportingUnit removeAssociatedWith(ReportingUnitAssociation reportingUnitAssociation) {
        associatedWiths.remove(reportingUnitAssociation);
        reportingUnitAssociation.setReportingUnit(null);
        return this;
    }

    public void setAssociatedWiths(Set<ReportingUnitAssociation> reportingUnitAssociations) {
        this.associatedWiths = reportingUnitAssociations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportingUnit reportingUnit = (ReportingUnit) o;
        if(reportingUnit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reportingUnit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReportingUnit{" +
            "id=" + id +
            ", uniqueReference='" + uniqueReference + "'" +
            ", businessName='" + businessName + "'" +
            '}';
    }
}
