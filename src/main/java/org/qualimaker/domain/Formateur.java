package org.qualimaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Formateur.
 */
@Entity
@Table(name = "formateur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "formateur")
public class Formateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "cv")
    private String cv;

    @OneToMany(mappedBy = "formateur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TypeFormateur> typeformateurs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Formateur nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Formateur prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCv() {
        return cv;
    }

    public Formateur cv(String cv) {
        this.cv = cv;
        return this;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public Set<TypeFormateur> getTypeformateurs() {
        return typeformateurs;
    }

    public Formateur typeformateurs(Set<TypeFormateur> typeFormateurs) {
        this.typeformateurs = typeFormateurs;
        return this;
    }

    public Formateur addTypeformateur(TypeFormateur typeFormateur) {
        this.typeformateurs.add(typeFormateur);
        typeFormateur.setFormateur(this);
        return this;
    }

    public Formateur removeTypeformateur(TypeFormateur typeFormateur) {
        this.typeformateurs.remove(typeFormateur);
        typeFormateur.setFormateur(null);
        return this;
    }

    public void setTypeformateurs(Set<TypeFormateur> typeFormateurs) {
        this.typeformateurs = typeFormateurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Formateur formateur = (Formateur) o;
        if (formateur.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, formateur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Formateur{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", cv='" + cv + "'" +
            '}';
    }
}
