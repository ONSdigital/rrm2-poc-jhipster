package uk.gov.ons.rrm.domain;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import uk.gov.ons.rrm.domain.enumeration.CollectionInstrumentKind;

/**
 * Tool for requesting data from a Reporting Unit                              
 * 
 */
@ApiModel(description = ""
    + "Tool for requesting data from a Reporting Unit                         "
    + "")
@Entity
@Table(name = "collection_instrument")
public class CollectionInstrument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "instrument_type")
    private CollectionInstrumentKind instrumentType;

    @Column(name = "form_type")
    private String formType;

    @Column(name = "urn")
    private String urn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CollectionInstrumentKind getInstrumentType() {
        return instrumentType;
    }

    public CollectionInstrument instrumentType(CollectionInstrumentKind instrumentType) {
        this.instrumentType = instrumentType;
        return this;
    }

    public void setInstrumentType(CollectionInstrumentKind instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getFormType() {
        return formType;
    }

    public CollectionInstrument formType(String formType) {
        this.formType = formType;
        return this;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getUrn() {
        return urn;
    }

    public CollectionInstrument urn(String urn) {
        this.urn = urn;
        return this;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CollectionInstrument collectionInstrument = (CollectionInstrument) o;
        if(collectionInstrument.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, collectionInstrument.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CollectionInstrument{" +
            "id=" + id +
            ", instrumentType='" + instrumentType + "'" +
            ", formType='" + formType + "'" +
            ", urn='" + urn + "'" +
            '}';
    }
}
