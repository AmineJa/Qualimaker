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
 * A Processus.
 */
@Entity
@Table(name = "processus")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "processus")
public class Processus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @OneToMany(mappedBy = "processus")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DocumentInterne> documentInternes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Processus libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<DocumentInterne> getDocumentInternes() {
        return documentInternes;
    }

    public Processus documentInternes(Set<DocumentInterne> documentInternes) {
        this.documentInternes = documentInternes;
        return this;
    }

    public Processus addDocumentInterne(DocumentInterne documentInterne) {
        this.documentInternes.add(documentInterne);
        documentInterne.setProcessus(this);
        return this;
    }

    public Processus removeDocumentInterne(DocumentInterne documentInterne) {
        this.documentInternes.remove(documentInterne);
        documentInterne.setProcessus(null);
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
        Processus processus = (Processus) o;
        if (processus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, processus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Processus{" +
            "id=" + id +
            ", libelle='" + libelle + "'" +
            '}';
    }
}
