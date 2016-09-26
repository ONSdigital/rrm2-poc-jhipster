package uk.gov.ons.rrm.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SampleSelection.
 */
@Entity
@Table(name = "sample_selection")
public class SampleSelection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private CollectionExercise collectionExercise;

    @ManyToOne
    private ReportingUnit reportingUnit;

    @ManyToOne
    private CollectionInstrument collectionInstrument;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CollectionExercise getCollectionExercise() {
        return collectionExercise;
    }

    public SampleSelection collectionExercise(CollectionExercise collectionExercise) {
        this.collectionExercise = collectionExercise;
        return this;
    }

    public void setCollectionExercise(CollectionExercise collectionExercise) {
        this.collectionExercise = collectionExercise;
    }

    public ReportingUnit getReportingUnit() {
        return reportingUnit;
    }

    public SampleSelection reportingUnit(ReportingUnit reportingUnit) {
        this.reportingUnit = reportingUnit;
        return this;
    }

    public void setReportingUnit(ReportingUnit reportingUnit) {
        this.reportingUnit = reportingUnit;
    }

    public CollectionInstrument getCollectionInstrument() {
        return collectionInstrument;
    }

    public SampleSelection collectionInstrument(CollectionInstrument collectionInstrument) {
        this.collectionInstrument = collectionInstrument;
        return this;
    }

    public void setCollectionInstrument(CollectionInstrument collectionInstrument) {
        this.collectionInstrument = collectionInstrument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SampleSelection sampleSelection = (SampleSelection) o;
        if(sampleSelection.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sampleSelection.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SampleSelection{" +
            "id=" + id +
            '}';
    }
}
