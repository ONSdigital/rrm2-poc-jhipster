package uk.gov.ons.rrm.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import uk.gov.ons.rrm.domain.enumeration.ResponseStatusKind;

/**
 * A CollectionResponse.
 */
@Entity
@Table(name = "collection_response")
public class CollectionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ResponseStatusKind status;

    @OneToOne
    @JoinColumn(unique = true)
    private SampleSelection relatesTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResponseStatusKind getStatus() {
        return status;
    }

    public CollectionResponse status(ResponseStatusKind status) {
        this.status = status;
        return this;
    }

    public void setStatus(ResponseStatusKind status) {
        this.status = status;
    }

    public SampleSelection getRelatesTo() {
        return relatesTo;
    }

    public CollectionResponse relatesTo(SampleSelection sampleSelection) {
        this.relatesTo = sampleSelection;
        return this;
    }

    public void setRelatesTo(SampleSelection sampleSelection) {
        this.relatesTo = sampleSelection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CollectionResponse collectionResponse = (CollectionResponse) o;
        if(collectionResponse.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, collectionResponse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CollectionResponse{" +
            "id=" + id +
            ", status='" + status + "'" +
            '}';
    }
}
