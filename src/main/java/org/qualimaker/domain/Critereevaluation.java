package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Critereevaluation.
 */
@Entity
@Table(name = "critereevaluation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "critereevaluation")
public class Critereevaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "critere")
    private String critere;

    @Column(name = "categorie")
    private String categorie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCritere() {
        return critere;
    }

    public Critereevaluation critere(String critere) {
        this.critere = critere;
        return this;
    }

    public void setCritere(String critere) {
        this.critere = critere;
    }

    public String getCategorie() {
        return categorie;
    }

    public Critereevaluation categorie(String categorie) {
        this.categorie = categorie;
        return this;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Critereevaluation critereevaluation = (Critereevaluation) o;
        if (critereevaluation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, critereevaluation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Critereevaluation{" +
            "id=" + id +
            ", critere='" + critere + "'" +
            ", categorie='" + categorie + "'" +
            '}';
    }
}
