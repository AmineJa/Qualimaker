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
 * A Sites.
 */
@Entity
@Table(name = "sites")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sites")
public class Sites implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @OneToMany(mappedBy = "sites")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DocumentInterne> documentInternes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Sites nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<DocumentInterne> getDocumentInternes() {
        return documentInternes;
    }

    public Sites documentInternes(Set<DocumentInterne> documentInternes) {
        this.documentInternes = documentInternes;
        return this;
    }

    public Sites addDocumentInterne(DocumentInterne documentInterne) {
        this.documentInternes.add(documentInterne);
        documentInterne.setSites(this);
        return this;
    }

    public Sites removeDocumentInterne(DocumentInterne documentInterne) {
        this.documentInternes.remove(documentInterne);
        documentInterne.setSites(null);
        return this;
    }

    public void setDocumentInternes(Set<DocumentInterne> documentInternes) {
        this.documentInternes = documentInternes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sites sites = (Sites) o;
        if (sites.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sites.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sites{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            '}';
    }
}
