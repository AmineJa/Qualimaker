package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Evaluation.
 */
@Entity
@Table(name = "evaluation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "evaluation")
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "anne")
    private ZonedDateTime anne;

    @Column(name = "description")
    private String description;

    @Column(name = "valeurmax")
    private Double valeurmax;

    @Column(name = "seuil_pol")
    private String seuilPol;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "competence")
    private String competence;

    @Column(name = "polyvalence")
    private String polyvalence;

    @Column(name = "critere")
    private String critere;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getAnne() {
        return anne;
    }

    public Evaluation anne(ZonedDateTime anne) {
        this.anne = anne;
        return this;
    }

    public void setAnne(ZonedDateTime anne) {
        this.anne = anne;
    }

    public String getDescription() {
        return description;
    }

    public Evaluation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValeurmax() {
        return valeurmax;
    }

    public Evaluation valeurmax(Double valeurmax) {
        this.valeurmax = valeurmax;
        return this;
    }

    public void setValeurmax(Double valeurmax) {
        this.valeurmax = valeurmax;
    }

    public String getSeuilPol() {
        return seuilPol;
    }

    public Evaluation seuilPol(String seuilPol) {
        this.seuilPol = seuilPol;
        return this;
    }

    public void setSeuilPol(String seuilPol) {
        this.seuilPol = seuilPol;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Evaluation date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getCompetence() {
        return competence;
    }

    public Evaluation competence(String competence) {
        this.competence = competence;
        return this;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    public String getPolyvalence() {
        return polyvalence;
    }

    public Evaluation polyvalence(String polyvalence) {
        this.polyvalence = polyvalence;
        return this;
    }

    public void setPolyvalence(String polyvalence) {
        this.polyvalence = polyvalence;
    }

    public String getCritere() {
        return critere;
    }

    public Evaluation critere(String critere) {
        this.critere = critere;
        return this;
    }

    public void setCritere(String critere) {
        this.critere = critere;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Evaluation evaluation = (Evaluation) o;
        if (evaluation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, evaluation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Evaluation{" +
            "id=" + id +
            ", anne='" + anne + "'" +
            ", description='" + description + "'" +
            ", valeurmax='" + valeurmax + "'" +
            ", seuilPol='" + seuilPol + "'" +
            ", date='" + date + "'" +
            ", competence='" + competence + "'" +
            ", polyvalence='" + polyvalence + "'" +
            ", critere='" + critere + "'" +
            '}';
    }
}
