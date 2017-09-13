package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DocumentInterne.
 */
@Entity
@Table(name = "document_interne")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "documentinterne")
public class DocumentInterne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Lob
    @Column(name = "fichier", nullable = false)
    private byte[] fichier;

    @Column(name = "fichier_content_type", nullable = false)
    private String fichierContentType;

    @NotNull
    @Column(name = "motif", nullable = false)
    private String motif;

    @Column(name = "message")
    private String message;

    @Column(name = "etat")
    private String etat;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "v")
    private Boolean v;

    @Column(name = "r")
    private Boolean r;

    @Column(name = "a")
    private Boolean a;

    @Column(name = "typeuser")
    private String typeuser;

    @Column(name = "precedent")
    private String precedent;

    @Column(name = "suivant")
    private String suivant;

    @Column(name = "version")
    private Float version;

    @ManyToOne
    private Sites sites;

    @ManyToOne
    private Processus processus;

    @ManyToOne(optional = false)
    @NotNull
    private TypeDocumentation typeDocumentation;

    @ManyToOne
    private DroitaccesDocument superviseur;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "document_interne_verificateur",
               joinColumns = @JoinColumn(name="document_internes_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="verificateurs_id", referencedColumnName="id"))
    private Set<DroitaccesDocument> verificateurs = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "document_interne_redacteur",
               joinColumns = @JoinColumn(name="document_internes_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="redacteurs_id", referencedColumnName="id"))
    private Set<DroitaccesDocument> redacteurs = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "document_interne_approubateur",
               joinColumns = @JoinColumn(name="document_internes_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="approubateurs_id", referencedColumnName="id"))
    private Set<DroitaccesDocument> approubateurs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public DocumentInterne code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public DocumentInterne libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public byte[] getFichier() {
        return fichier;
    }

    public DocumentInterne fichier(byte[] fichier) {
        this.fichier = fichier;
        return this;
    }

    public void setFichier(byte[] fichier) {
        this.fichier = fichier;
    }

    public String getFichierContentType() {
        return fichierContentType;
    }

    public DocumentInterne fichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
        return this;
    }

    public void setFichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
    }

    public String getMotif() {
        return motif;
    }

    public DocumentInterne motif(String motif) {
        this.motif = motif;
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getMessage() {
        return message;
    }

    public DocumentInterne message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEtat() {
        return etat;
    }

    public DocumentInterne etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public DocumentInterne date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Boolean isV() {
        return v;
    }

    public DocumentInterne v(Boolean v) {
        this.v = v;
        return this;
    }

    public void setV(Boolean v) {
        this.v = v;
    }

    public Boolean isR() {
        return r;
    }

    public DocumentInterne r(Boolean r) {
        this.r = r;
        return this;
    }

    public void setR(Boolean r) {
        this.r = r;
    }

    public Boolean isA() {
        return a;
    }

    public DocumentInterne a(Boolean a) {
        this.a = a;
        return this;
    }

    public void setA(Boolean a) {
        this.a = a;
    }

    public String getTypeuser() {
        return typeuser;
    }

    public DocumentInterne typeuser(String typeuser) {
        this.typeuser = typeuser;
        return this;
    }

    public void setTypeuser(String typeuser) {
        this.typeuser = typeuser;
    }

    public String getPrecedent() {
        return precedent;
    }

    public DocumentInterne precedent(String precedent) {
        this.precedent = precedent;
        return this;
    }

    public void setPrecedent(String precedent) {
        this.precedent = precedent;
    }

    public String getSuivant() {
        return suivant;
    }

    public DocumentInterne suivant(String suivant) {
        this.suivant = suivant;
        return this;
    }

    public void setSuivant(String suivant) {
        this.suivant = suivant;
    }

    public Float getVersion() {
        return version;
    }

    public DocumentInterne version(Float version) {
        this.version = version;
        return this;
    }

    public void setVersion(Float version) {
        this.version = version;
    }

    public Sites getSites() {
        return sites;
    }

    public DocumentInterne sites(Sites sites) {
        this.sites = sites;
        return this;
    }

    public void setSites(Sites sites) {
        this.sites = sites;
    }

    public Processus getProcessus() {
        return processus;
    }

    public DocumentInterne processus(Processus processus) {
        this.processus = processus;
        return this;
    }

    public void setProcessus(Processus processus) {
        this.processus = processus;
    }

    public TypeDocumentation getTypeDocumentation() {
        return typeDocumentation;
    }

    public DocumentInterne typeDocumentation(TypeDocumentation typeDocumentation) {
        this.typeDocumentation = typeDocumentation;
        return this;
    }

    public void setTypeDocumentation(TypeDocumentation typeDocumentation) {
        this.typeDocumentation = typeDocumentation;
    }

    public DroitaccesDocument getSuperviseur() {
        return superviseur;
    }

    public DocumentInterne superviseur(DroitaccesDocument droitaccesDocument) {
        this.superviseur = droitaccesDocument;
        return this;
    }

    public void setSuperviseur(DroitaccesDocument droitaccesDocument) {
        this.superviseur = droitaccesDocument;
    }

    public Set<DroitaccesDocument> getVerificateurs() {
        return verificateurs;
    }

    public DocumentInterne verificateurs(Set<DroitaccesDocument> droitaccesDocuments) {
        this.verificateurs = droitaccesDocuments;
        return this;
    }

    public DocumentInterne addVerificateur(DroitaccesDocument droitaccesDocument) {
        this.verificateurs.add(droitaccesDocument);
        droitaccesDocument.getVerificateurs().add(this);
        return this;
    }

    public DocumentInterne removeVerificateur(DroitaccesDocument droitaccesDocument) {
        this.verificateurs.remove(droitaccesDocument);
        droitaccesDocument.getVerificateurs().remove(this);
        return this;
    }

    public void setVerificateurs(Set<DroitaccesDocument> droitaccesDocuments) {
        this.verificateurs = droitaccesDocuments;
    }

    public Set<DroitaccesDocument> getRedacteurs() {
        return redacteurs;
    }

    public DocumentInterne redacteurs(Set<DroitaccesDocument> droitaccesDocuments) {
        this.redacteurs = droitaccesDocuments;
        return this;
    }

    public DocumentInterne addRedacteur(DroitaccesDocument droitaccesDocument) {
        this.redacteurs.add(droitaccesDocument);
        droitaccesDocument.getRedacteurs().add(this);
        return this;
    }

    public DocumentInterne removeRedacteur(DroitaccesDocument droitaccesDocument) {
        this.redacteurs.remove(droitaccesDocument);
        droitaccesDocument.getRedacteurs().remove(this);
        return this;
    }

    public void setRedacteurs(Set<DroitaccesDocument> droitaccesDocuments) {
        this.redacteurs = droitaccesDocuments;
    }

    public Set<DroitaccesDocument> getApproubateurs() {
        return approubateurs;
    }

    public DocumentInterne approubateurs(Set<DroitaccesDocument> droitaccesDocuments) {
        this.approubateurs = droitaccesDocuments;
        return this;
    }

    public DocumentInterne addApproubateur(DroitaccesDocument droitaccesDocument) {
        this.approubateurs.add(droitaccesDocument);
        droitaccesDocument.getApproubateurs().add(this);
        return this;
    }

    public DocumentInterne removeApproubateur(DroitaccesDocument droitaccesDocument) {
        this.approubateurs.remove(droitaccesDocument);
        droitaccesDocument.getApproubateurs().remove(this);
        return this;
    }

    public void setApproubateurs(Set<DroitaccesDocument> droitaccesDocuments) {
        this.approubateurs = droitaccesDocuments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentInterne documentInterne = (DocumentInterne) o;
        if (documentInterne.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, documentInterne.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DocumentInterne{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", libelle='" + libelle + "'" +
            ", fichier='" + fichier + "'" +
            ", fichierContentType='" + fichierContentType + "'" +
            ", motif='" + motif + "'" +
            ", message='" + message + "'" +
            ", etat='" + etat + "'" +
            ", date='" + date + "'" +
            ", v='" + v + "'" +
            ", r='" + r + "'" +
            ", a='" + a + "'" +
            ", typeuser='" + typeuser + "'" +
            ", precedent='" + precedent + "'" +
            ", suivant='" + suivant + "'" +
            ", version='" + version + "'" +
            '}';
    }
}
