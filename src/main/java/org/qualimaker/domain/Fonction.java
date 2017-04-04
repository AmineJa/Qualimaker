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

    @Column(name = "nom")
    private String nom;

    @ManyToMany(mappedBy = "fonctions")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Employe> employees = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Fonction nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Employe> getEmployees() {
        return employees;
    }

    public Fonction employees(Set<Employe> employes) {
        this.employees = employes;
        return this;
    }

    public Fonction addEmployee(Employe employe) {
        this.employees.add(employe);
        employe.getFonctions().add(this);
        return this;
    }

    public Fonction removeEmployee(Employe employe) {
        this.employees.remove(employe);
        employe.getFonctions().remove(this);
        return this;
    }

    public void setEmployees(Set<Employe> employes) {
        this.employees = employes;
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
            ", nom='" + nom + "'" +
            '}';
    }
}
