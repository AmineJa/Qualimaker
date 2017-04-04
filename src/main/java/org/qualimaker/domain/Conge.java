package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Conge.
 */
@Entity
@Table(name = "conge")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "conge")
public class Conge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "statut")
    private String statut;

    @Column(name = "date_d")
    private ZonedDateTime dateD;

    @Column(name = "date_f")
    private ZonedDateTime dateF;

    @ManyToOne
    private Employe employe;

    @ManyToOne
    private TypeConge typeconge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatut() {
        return statut;
    }

    public Conge statut(String statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public ZonedDateTime getDateD() {
        return dateD;
    }

    public Conge dateD(ZonedDateTime dateD) {
        this.dateD = dateD;
        return this;
    }

    public void setDateD(ZonedDateTime dateD) {
        this.dateD = dateD;
    }

    public ZonedDateTime getDateF() {
        return dateF;
    }

    public Conge dateF(ZonedDateTime dateF) {
        this.dateF = dateF;
        return this;
    }

    public void setDateF(ZonedDateTime dateF) {
        this.dateF = dateF;
    }

    public Employe getEmploye() {
        return employe;
    }

    public Conge employe(Employe employe) {
        this.employe = employe;
        return this;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public TypeConge getTypeconge() {
        return typeconge;
    }

    public Conge typeconge(TypeConge typeConge) {
        this.typeconge = typeConge;
        return this;
    }

    public void setTypeconge(TypeConge typeConge) {
        this.typeconge = typeConge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conge conge = (Conge) o;
        if (conge.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, conge.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Conge{" +
            "id=" + id +
            ", statut='" + statut + "'" +
            ", dateD='" + dateD + "'" +
            ", dateF='" + dateF + "'" +
            '}';
    }
}
