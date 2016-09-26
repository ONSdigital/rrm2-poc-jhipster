package uk.gov.ons.rrm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import uk.gov.ons.rrm.domain.enumeration.CollectionExerciseStatusKind;

/**
 * Time bound set of activities intended to acquiredata about Statistical Units via Collection Instruments.
 * 
 */
@ApiModel(description = ""
    + "Time bound set of activities intended to acquiredata about Statistical Units via Collection Instruments."
    + "")
@Entity
@Table(name = "collection_exercise")
public class CollectionExercise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CollectionExerciseStatusKind status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    private Survey survey;

    @OneToMany(mappedBy = "collectionExercise")
    @JsonIgnore
    private Set<SampleSelection> samples = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CollectionExerciseStatusKind getStatus() {
        return status;
    }

    public CollectionExercise status(CollectionExerciseStatusKind status) {
        this.status = status;
        return this;
    }

    public void setStatus(CollectionExerciseStatusKind status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public CollectionExercise startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public CollectionExercise endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Survey getSurvey() {
        return survey;
    }

    public CollectionExercise survey(Survey survey) {
        this.survey = survey;
        return this;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Set<SampleSelection> getSamples() {
        return samples;
    }

    public CollectionExercise samples(Set<SampleSelection> sampleSelections) {
        this.samples = sampleSelections;
        return this;
    }

    public CollectionExercise addSample(SampleSelection sampleSelection) {
        samples.add(sampleSelection);
        sampleSelection.setCollectionExercise(this);
        return this;
    }

    public CollectionExercise removeSample(SampleSelection sampleSelection) {
        samples.remove(sampleSelection);
        sampleSelection.setCollectionExercise(null);
        return this;
    }

    public void setSamples(Set<SampleSelection> sampleSelections) {
        this.samples = sampleSelections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CollectionExercise collectionExercise = (CollectionExercise) o;
        if(collectionExercise.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, collectionExercise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CollectionExercise{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            '}';
    }
}
