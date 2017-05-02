package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Discipline.
 */
@Entity
@Table(name = "discipline")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "discipline")
public class Discipline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_d")
    private ZonedDateTime dateD;

    @Column(name = "date_f")
    private ZonedDateTime dateF;

    @Column(name = "duree")
    private Integer duree;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "cause")
    private String cause;

    @ManyToOne
    private Naturediscipline naturediscipline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateD() {
        return dateD;
    }

    public Discipline dateD(ZonedDateTime dateD) {
        this.dateD = dateD;
        return this;
    }

    public void setDateD(ZonedDateTime dateD) {
        this.dateD = dateD;
    }

    public ZonedDateTime getDateF() {
        return dateF;
    }

    public Discipline dateF(ZonedDateTime dateF) {
        this.dateF = dateF;
        return this;
    }

    public void setDateF(ZonedDateTime dateF) {
        this.dateF = dateF;
    }

    public Integer getDuree() {
        return duree;
    }

    public Discipline duree(Integer duree) {
        this.duree = duree;
        return this;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Discipline commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getCause() {
        return cause;
    }

    public Discipline cause(String cause) {
        this.cause = cause;
        return this;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Naturediscipline getNaturediscipline() {
        return naturediscipline;
    }

    public Discipline naturediscipline(Naturediscipline naturediscipline) {
        this.naturediscipline = naturediscipline;
        return this;
    }

    public void setNaturediscipline(Naturediscipline naturediscipline) {
        this.naturediscipline = naturediscipline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Discipline discipline = (Discipline) o;
        if (discipline.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, discipline.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Discipline{" +
            "id=" + id +
            ", dateD='" + dateD + "'" +
            ", dateF='" + dateF + "'" +
            ", duree='" + duree + "'" +
            ", commentaire='" + commentaire + "'" +
            ", cause='" + cause + "'" +
            '}';
    }
}
