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
 * A Remplacer.
 */
@Entity
@Table(name = "remplacer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "remplacer")
public class Remplacer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "debut", nullable = false)
    private ZonedDateTime debut;

    @Column(name = "fin")
    private ZonedDateTime fin;

    @ManyToOne(optional = false)
    @NotNull
    private Employe employe;

    @ManyToOne(optional = false)
    @NotNull
    private Employe nouveau;

    @ManyToOne
    private Profilsfonction profilsfonction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDebut() {
        return debut;
    }

    public Remplacer debut(ZonedDateTime debut) {
        this.debut = debut;
        return this;
    }

    public void setDebut(ZonedDateTime debut) {
        this.debut = debut;
    }

    public ZonedDateTime getFin() {
        return fin;
    }

    public Remplacer fin(ZonedDateTime fin) {
        this.fin = fin;
        return this;
    }

    public void setFin(ZonedDateTime fin) {
        this.fin = fin;
    }

    public Employe getEmploye() {
        return employe;
    }

    public Remplacer employe(Employe employe) {
        this.employe = employe;
        return this;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Employe getNouveau() {
        return nouveau;
    }

    public Remplacer nouveau(Employe employe) {
        this.nouveau = employe;
        return this;
    }

    public void setNouveau(Employe employe) {
        this.nouveau = employe;
    }

    public Profilsfonction getProfilsfonction() {
        return profilsfonction;
    }

    public Remplacer profilsfonction(Profilsfonction profilsfonction) {
        this.profilsfonction = profilsfonction;
        return this;
    }

    public void setProfilsfonction(Profilsfonction profilsfonction) {
        this.profilsfonction = profilsfonction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Remplacer remplacer = (Remplacer) o;
        if (remplacer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, remplacer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Remplacer{" +
            "id=" + id +
            ", debut='" + debut + "'" +
            ", fin='" + fin + "'" +
            '}';
    }
}
