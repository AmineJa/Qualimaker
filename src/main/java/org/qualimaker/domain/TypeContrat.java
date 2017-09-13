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
 * A TypeContrat.
 */
@Entity
@Table(name = "type_contrat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "typecontrat")
public class TypeContrat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "typeContrat")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Carriere> carrieres = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public TypeContrat type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Carriere> getCarrieres() {
        return carrieres;
    }

    public TypeContrat carrieres(Set<Carriere> carrieres) {
        this.carrieres = carrieres;
        return this;
    }

    public TypeContrat addCarriere(Carriere carriere) {
        this.carrieres.add(carriere);
        carriere.setTypeContrat(this);
        return this;
    }

    public TypeContrat removeCarriere(Carriere carriere) {
        this.carrieres.remove(carriere);
        carriere.setTypeContrat(null);
        return this;
    }

    public void setCarrieres(Set<Carriere> carrieres) {
        this.carrieres = carrieres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeContrat typeContrat = (TypeContrat) o;
        if (typeContrat.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, typeContrat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TypeContrat{" +
            "id=" + id +
            ", type='" + type + "'" +
            '}';
    }
}
