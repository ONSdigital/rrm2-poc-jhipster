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
 * The use of a Collection Instrument to obtain information on allor part of a population of interest.
 * 
 */
@ApiModel(description = ""
    + "The use of a Collection Instrument to obtain information on allor part of a population of interest."
    + "")
@Entity
@Table(name = "survey")
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "survey")
    @JsonIgnore
    private Set<CollectionExercise> enactedThroughs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Survey name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CollectionExercise> getEnactedThroughs() {
        return enactedThroughs;
    }

    public Survey enactedThroughs(Set<CollectionExercise> collectionExercises) {
        this.enactedThroughs = collectionExercises;
        return this;
    }

    public Survey addEnactedThrough(CollectionExercise collectionExercise) {
        enactedThroughs.add(collectionExercise);
        collectionExercise.setSurvey(this);
        return this;
    }

    public Survey removeEnactedThrough(CollectionExercise collectionExercise) {
        enactedThroughs.remove(collectionExercise);
        collectionExercise.setSurvey(null);
        return this;
    }

    public void setEnactedThroughs(Set<CollectionExercise> collectionExercises) {
        this.enactedThroughs = collectionExercises;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Survey survey = (Survey) o;
        if(survey.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, survey.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Survey{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
