package uk.gov.ons.rrm.domain;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A code that is issued to a Reporting Unit to activate aReporting Unit Association.
 * 
 */
@ApiModel(description = ""
    + "A code that is issued to a Reporting Unit to activate aReporting Unit Association."
    + "")
@Entity
@Table(name = "enrolment_code")
public class EnrolmentCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne
    private SampleSelection relatesTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public EnrolmentCode code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SampleSelection getRelatesTo() {
        return relatesTo;
    }

    public EnrolmentCode relatesTo(SampleSelection sampleSelection) {
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
        EnrolmentCode enrolmentCode = (EnrolmentCode) o;
        if(enrolmentCode.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, enrolmentCode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EnrolmentCode{" +
            "id=" + id +
            ", code='" + code + "'" +
            '}';
    }
}
