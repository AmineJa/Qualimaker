package org.qualimaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Fonction.
 */
@Entity
@Table(name = "fonction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fonction")
public class Fonction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "fonction", nullable = false)
    private String fonction;

    @OneToMany(mappedBy = "fonction")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Profilsfonction> profilsfonctions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFonction() {
        return fonction;
    }

    public Fonction fonction(String fonction) {
        this.fonction = fonction;
        return this;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public Set<Profilsfonction> getProfilsfonctions() {
        return profilsfonctions;
    }

    public Fonction profilsfonctions(Set<Profilsfonction> profilsfonctions) {
        this.profilsfonctions = profilsfonctions;
        return this;
    }

    public Fonction addProfilsfonction(Profilsfonction profilsfonction) {
        this.profilsfonctions.add(profilsfonction);
        profilsfonction.setFonction(this);
        return this;
    }

    public Fonction removeProfilsfonction(Profilsfonction profilsfonction) {
        this.profilsfonctions.remove(profilsfonction);
        profilsfonction.setFonction(null);
        return this;
    }

    public void setProfilsfonctions(Set<Profilsfonction> profilsfonctions) {
        this.profilsfonctions = profilsfonctions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fonction fonction = (Fonction) o;
        if (fonction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fonction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Fonction{" +
            "id=" + id +
            ", fonction='" + fonction + "'" +
            '}';
    }
}
