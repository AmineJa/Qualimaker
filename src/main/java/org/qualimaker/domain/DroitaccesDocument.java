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

import org.qualimaker.domain.enumeration.Roles;

/**
 * A DroitaccesDocument.
 */
@Entity
@Table(name = "droitacces_document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "droitaccesdocument")
public class DroitaccesDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "roles", nullable = false)
    private Roles roles;

    @ManyToOne(optional = false)
    @NotNull
    private Employe employe;

    @ManyToMany(mappedBy = "verificateurs")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DocumentInterne> verificateurs = new HashSet<>();

    @ManyToMany(mappedBy = "redacteurs")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DocumentInterne> redacteurs = new HashSet<>();

    @OneToMany(mappedBy = "superviseur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DocumentInterne> superviseurs = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "droitacces_document_approubateur",
               joinColumns = @JoinColumn(name="droitacces_documents_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="approubateurs_id", referencedColumnName="id"))
    private Set<DocumentInterne> approubateurs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Roles getRoles() {
        return roles;
    }

    public DroitaccesDocument roles(Roles roles) {
        this.roles = roles;
        return this;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public Employe getEmploye() {
        return employe;
    }

    public DroitaccesDocument employe(Employe employe) {
        this.employe = employe;
        return this;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Set<DocumentInterne> getVerificateurs() {
        return verificateurs;
    }

    public DroitaccesDocument verificateurs(Set<DocumentInterne> documentInternes) {
        this.verificateurs = documentInternes;
        return this;
    }

    public DroitaccesDocument addVerificateur(DocumentInterne documentInterne) {
        this.verificateurs.add(documentInterne);
        documentInterne.getVerificateurs().add(this);
        return this;
    }

    public DroitaccesDocument removeVerificateur(DocumentInterne documentInterne) {
        this.verificateurs.remove(documentInterne);
        documentInterne.getVerificateurs().remove(this);
        return this;
    }

    public void setVerificateurs(Set<DocumentInterne> documentInternes) {
        this.verificateurs = documentInternes;
    }

    public Set<DocumentInterne> getRedacteurs() {
        return redacteurs;
    }

    public DroitaccesDocument redacteurs(Set<DocumentInterne> documentInternes) {
        this.redacteurs = documentInternes;
        return this;
    }

    public DroitaccesDocument addRedacteur(DocumentInterne documentInterne) {
        this.redacteurs.add(documentInterne);
        documentInterne.getRedacteurs().add(this);
        return this;
    }

    public DroitaccesDocument removeRedacteur(DocumentInterne documentInterne) {
        this.redacteurs.remove(documentInterne);
        documentInterne.getRedacteurs().remove(this);
        return this;
    }

    public void setRedacteurs(Set<DocumentInterne> documentInternes) {
        this.redacteurs = documentInternes;
    }

    public Set<DocumentInterne> getSuperviseurs() {
        return superviseurs;
    }

    public DroitaccesDocument superviseurs(Set<DocumentInterne> documentInternes) {
        this.superviseurs = documentInternes;
        return this;
    }

    public DroitaccesDocument addSuperviseur(DocumentInterne documentInterne) {
        this.superviseurs.add(documentInterne);
        documentInterne.setSuperviseur(this);
        return this;
    }

    public DroitaccesDocument removeSuperviseur(DocumentInterne documentInterne) {
        this.superviseurs.remove(documentInterne);
        documentInterne.setSuperviseur(null);
        return this;
    }

    public void setSuperviseurs(Set<DocumentInterne> documentInternes) {
        this.superviseurs = documentInternes;
    }

    public Set<DocumentInterne> getApproubateurs() {
        return approubateurs;
    }

    public DroitaccesDocument approubateurs(Set<DocumentInterne> documentInternes) {
        this.approubateurs = documentInternes;
        return this;
    }

    public DroitaccesDocument addApproubateur(DocumentInterne documentInterne) {
        this.approubateurs.add(documentInterne);
        documentInterne.getApproubateurs().add(this);
        return this;
    }

    public DroitaccesDocument removeApproubateur(DocumentInterne documentInterne) {
        this.approubateurs.remove(documentInterne);
        documentInterne.getApproubateurs().remove(this);
        return this;
    }

    public void setApproubateurs(Set<DocumentInterne> documentInternes) {
        this.approubateurs = documentInternes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DroitaccesDocument droitaccesDocument = (DroitaccesDocument) o;
        if (droitaccesDocument.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, droitaccesDocument.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DroitaccesDocument{" +
            "id=" + id +
            ", roles='" + roles + "'" +
            '}';
    }
}
