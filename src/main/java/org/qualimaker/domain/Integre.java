package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Integre.
 */
@Entity
@Table(name = "integre")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "integre")
public class Integre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_d")
    private ZonedDateTime dateD;

    @Column(name = "date_f")
    private ZonedDateTime dateF;

    @Column(name = "pointfort")
    private String pointfort;

    @Column(name = "poitaibl")
    private String poitaibl;

    @Column(name = "info")
    private String info;

    @Column(name = "etat")
    private String etat;

    @ManyToOne(optional = false)
    @NotNull
    private Employe employe;

    @ManyToOne(optional = false)
    @NotNull
    private Employe responsable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateD() {
        return dateD;
    }

    public Integre dateD(ZonedDateTime dateD) {
        this.dateD = dateD;
        return this;
    }

    public void setDateD(ZonedDateTime dateD) {
        this.dateD = dateD;
    }

    public ZonedDateTime getDateF() {
        return dateF;
    }

    public Integre dateF(ZonedDateTime dateF) {
        this.dateF = dateF;
        return this;
    }

    public void setDateF(ZonedDateTime dateF) {
        this.dateF = dateF;
    }

    public String getPointfort() {
        return pointfort;
    }

    public Integre pointfort(String pointfort) {
        this.pointfort = pointfort;
        return this;
    }

    public void setPointfort(String pointfort) {
        this.pointfort = pointfort;
    }

    public String getPoitaibl() {
        return poitaibl;
    }

    public Integre poitaibl(String poitaibl) {
        this.poitaibl = poitaibl;
        return this;
    }

    public void setPoitaibl(String poitaibl) {
        this.poitaibl = poitaibl;
    }

    public String getInfo() {
        return info;
    }

    public Integre info(String info) {
        this.info = info;
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getEtat() {
        return etat;
    }

    public Integre etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Employe getEmploye() {
        return employe;
    }

    public Integre employe(Employe employe) {
        this.employe = employe;
        return this;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Employe getResponsable() {
        return responsable;
    }

    public Integre responsable(Employe employe) {
        this.responsable = employe;
        return this;
    }

    public void setResponsable(Employe employe) {
        this.responsable = employe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Integre integre = (Integre) o;
        if (integre.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, integre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Integre{" +
            "id=" + id +
            ", dateD='" + dateD + "'" +
            ", dateF='" + dateF + "'" +
            ", pointfort='" + pointfort + "'" +
            ", poitaibl='" + poitaibl + "'" +
            ", info='" + info + "'" +
            ", etat='" + etat + "'" +
            '}';
    }
}
