package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Profilsfonction.
 */
@Entity
@Table(name = "profilsfonction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "profilsfonction")
public class Profilsfonction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "profil", nullable = false)
    private String profil;

    @ManyToOne
    private Fonction fonction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfil() {
        return profil;
    }

    public Profilsfonction profil(String profil) {
        this.profil = profil;
        return this;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public Fonction getFonction() {
        return fonction;
    }

    public Profilsfonction fonction(Fonction fonction) {
        this.fonction = fonction;
        return this;
    }

    public void setFonction(Fonction fonction) {
        this.fonction = fonction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Profilsfonction profilsfonction = (Profilsfonction) o;
        if (profilsfonction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, profilsfonction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Profilsfonction{" +
            "id=" + id +
            ", profil='" + profil + "'" +
            '}';
    }
}
