package org.qualimaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import org.qualimaker.domain.enumeration.Sexe;

/**
 * A Employe.
 */
@Entity
@Table(name = "employe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employe")
public class Employe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "date_n", nullable = false)
    private ZonedDateTime dateN;

    @NotNull
    @Column(name = "cin", nullable = false)
    private Double cin;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private Sexe sexe;

    @Column(name = "adress")
    private String adress;

    @NotNull
    @Column(name = "gsm", nullable = false)
    private Double gsm;

    @Column(name = "lieu_nais")
    private String lieuNais;

    @Column(name = "tel_mais")
    private Double telMais;

    @Column(name = "teleph")
    private Double teleph;

    @Column(name = "delivrele")
    private ZonedDateTime delivrele;

    @Column(name = "delivrea")
    private String delivrea;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "matricule")
    private Double matricule;

    @Column(name = "email_2")
    private String email2;

    @Column(name = "grade")
    private String grade;

    @Column(name = "rib")
    private Double rib;

    @Column(name = "nsc")
    private Double nsc;

    @Column(name = "competence")
    private String competence;

    @Column(name = "diplome")
    private String diplome;

    @Column(name = "experience")
    private String experience;

    @Column(name = "aptphy")
    private String aptphy;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Lob
    @Column(name = "cv")
    private byte[] cv;

    @Column(name = "cv_content_type")
    private String cvContentType;

    @Column(name = "etat_doc")
    private String etatDoc;

    @Lob
    @Column(name = "signature")
    private byte[] signature;

    @Column(name = "signature_content_type")
    private String signatureContentType;

    @OneToMany(mappedBy = "employe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Conge> conges = new HashSet<>();

    @ManyToOne
    private Sites site;

    @ManyToOne
    private Groupe groupe;

    @ManyToOne
    private Serviice service;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "employe_formation",
               joinColumns = @JoinColumn(name="employes_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="formations_id", referencedColumnName="id"))
    private Set<Formation> formations = new HashSet<>();

    @ManyToMany(mappedBy = "employes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DocumenExterne> documenExternes = new HashSet<>();

    @OneToMany(mappedBy = "employe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Remplacer> remplacers = new HashSet<>();

    @OneToMany(mappedBy = "nouveau")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Remplacer> remplacernvs = new HashSet<>();

    @OneToMany(mappedBy = "employe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DroitaccesDocument> droitaccesDocuments = new HashSet<>();

    @OneToMany(mappedBy = "employe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Carriere> carrieres = new HashSet<>();

    @ManyToOne
    private Profilsfonction profilsfonction;

    @OneToMany(mappedBy = "employe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Competences> competences = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Employe nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Employe prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public ZonedDateTime getDateN() {
        return dateN;
    }

    public Employe dateN(ZonedDateTime dateN) {
        this.dateN = dateN;
        return this;
    }

    public void setDateN(ZonedDateTime dateN) {
        this.dateN = dateN;
    }

    public Double getCin() {
        return cin;
    }

    public Employe cin(Double cin) {
        this.cin = cin;
        return this;
    }

    public void setCin(Double cin) {
        this.cin = cin;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public Employe sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getAdress() {
        return adress;
    }

    public Employe adress(String adress) {
        this.adress = adress;
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Double getGsm() {
        return gsm;
    }

    public Employe gsm(Double gsm) {
        this.gsm = gsm;
        return this;
    }

    public void setGsm(Double gsm) {
        this.gsm = gsm;
    }

    public String getLieuNais() {
        return lieuNais;
    }

    public Employe lieuNais(String lieuNais) {
        this.lieuNais = lieuNais;
        return this;
    }

    public void setLieuNais(String lieuNais) {
        this.lieuNais = lieuNais;
    }

    public Double getTelMais() {
        return telMais;
    }

    public Employe telMais(Double telMais) {
        this.telMais = telMais;
        return this;
    }

    public void setTelMais(Double telMais) {
        this.telMais = telMais;
    }

    public Double getTeleph() {
        return teleph;
    }

    public Employe teleph(Double teleph) {
        this.teleph = teleph;
        return this;
    }

    public void setTeleph(Double teleph) {
        this.teleph = teleph;
    }

    public ZonedDateTime getDelivrele() {
        return delivrele;
    }

    public Employe delivrele(ZonedDateTime delivrele) {
        this.delivrele = delivrele;
        return this;
    }

    public void setDelivrele(ZonedDateTime delivrele) {
        this.delivrele = delivrele;
    }

    public String getDelivrea() {
        return delivrea;
    }

    public Employe delivrea(String delivrea) {
        this.delivrea = delivrea;
        return this;
    }

    public void setDelivrea(String delivrea) {
        this.delivrea = delivrea;
    }

    public String getEmail() {
        return email;
    }

    public Employe email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getMatricule() {
        return matricule;
    }

    public Employe matricule(Double matricule) {
        this.matricule = matricule;
        return this;
    }

    public void setMatricule(Double matricule) {
        this.matricule = matricule;
    }

    public String getEmail2() {
        return email2;
    }

    public Employe email2(String email2) {
        this.email2 = email2;
        return this;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getGrade() {
        return grade;
    }

    public Employe grade(String grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Double getRib() {
        return rib;
    }

    public Employe rib(Double rib) {
        this.rib = rib;
        return this;
    }

    public void setRib(Double rib) {
        this.rib = rib;
    }

    public Double getNsc() {
        return nsc;
    }

    public Employe nsc(Double nsc) {
        this.nsc = nsc;
        return this;
    }

    public void setNsc(Double nsc) {
        this.nsc = nsc;
    }

    public String getCompetence() {
        return competence;
    }

    public Employe competence(String competence) {
        this.competence = competence;
        return this;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    public String getDiplome() {
        return diplome;
    }

    public Employe diplome(String diplome) {
        this.diplome = diplome;
        return this;
    }

    public void setDiplome(String diplome) {
        this.diplome = diplome;
    }

    public String getExperience() {
        return experience;
    }

    public Employe experience(String experience) {
        this.experience = experience;
        return this;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAptphy() {
        return aptphy;
    }

    public Employe aptphy(String aptphy) {
        this.aptphy = aptphy;
        return this;
    }

    public void setAptphy(String aptphy) {
        this.aptphy = aptphy;
    }

    public byte[] getImage() {
        return image;
    }

    public Employe image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Employe imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public byte[] getCv() {
        return cv;
    }

    public Employe cv(byte[] cv) {
        this.cv = cv;
        return this;
    }

    public void setCv(byte[] cv) {
        this.cv = cv;
    }

    public String getCvContentType() {
        return cvContentType;
    }

    public Employe cvContentType(String cvContentType) {
        this.cvContentType = cvContentType;
        return this;
    }

    public void setCvContentType(String cvContentType) {
        this.cvContentType = cvContentType;
    }

    public String getEtatDoc() {
        return etatDoc;
    }

    public Employe etatDoc(String etatDoc) {
        this.etatDoc = etatDoc;
        return this;
    }

    public void setEtatDoc(String etatDoc) {
        this.etatDoc = etatDoc;
    }

    public byte[] getSignature() {
        return signature;
    }

    public Employe signature(byte[] signature) {
        this.signature = signature;
        return this;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getSignatureContentType() {
        return signatureContentType;
    }

    public Employe signatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
        return this;
    }

    public void setSignatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
    }

    public Set<Conge> getConges() {
        return conges;
    }

    public Employe conges(Set<Conge> conges) {
        this.conges = conges;
        return this;
    }

    public Employe addConge(Conge conge) {
        this.conges.add(conge);
        conge.setEmploye(this);
        return this;
    }

    public Employe removeConge(Conge conge) {
        this.conges.remove(conge);
        conge.setEmploye(null);
        return this;
    }

    public void setConges(Set<Conge> conges) {
        this.conges = conges;
    }

    public Sites getSite() {
        return site;
    }

    public Employe site(Sites sites) {
        this.site = sites;
        return this;
    }

    public void setSite(Sites sites) {
        this.site = sites;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public Employe groupe(Groupe groupe) {
        this.groupe = groupe;
        return this;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Serviice getService() {
        return service;
    }

    public Employe service(Serviice serviice) {
        this.service = serviice;
        return this;
    }

    public void setService(Serviice serviice) {
        this.service = serviice;
    }

    public Set<Formation> getFormations() {
        return formations;
    }

    public Employe formations(Set<Formation> formations) {
        this.formations = formations;
        return this;
    }

    public Employe addFormation(Formation formation) {
        this.formations.add(formation);
        formation.getEmployes().add(this);
        return this;
    }

    public Employe removeFormation(Formation formation) {
        this.formations.remove(formation);
        formation.getEmployes().remove(this);
        return this;
    }

    public void setFormations(Set<Formation> formations) {
        this.formations = formations;
    }

    public Set<DocumenExterne> getDocumenExternes() {
        return documenExternes;
    }

    public Employe documenExternes(Set<DocumenExterne> documenExternes) {
        this.documenExternes = documenExternes;
        return this;
    }

    public Employe addDocumenExternes(DocumenExterne documenExterne) {
        this.documenExternes.add(documenExterne);
        documenExterne.getEmployes().add(this);
        return this;
    }

    public Employe removeDocumenExternes(DocumenExterne documenExterne) {
        this.documenExternes.remove(documenExterne);
        documenExterne.getEmployes().remove(this);
        return this;
    }

    public void setDocumenExternes(Set<DocumenExterne> documenExternes) {
        this.documenExternes = documenExternes;
    }

    public Set<Remplacer> getRemplacers() {
        return remplacers;
    }

    public Employe remplacers(Set<Remplacer> remplacers) {
        this.remplacers = remplacers;
        return this;
    }

    public Employe addRemplacer(Remplacer remplacer) {
        this.remplacers.add(remplacer);
        remplacer.setEmploye(this);
        return this;
    }

    public Employe removeRemplacer(Remplacer remplacer) {
        this.remplacers.remove(remplacer);
        remplacer.setEmploye(null);
        return this;
    }

    public void setRemplacers(Set<Remplacer> remplacers) {
        this.remplacers = remplacers;
    }

    public Set<Remplacer> getRemplacernvs() {
        return remplacernvs;
    }

    public Employe remplacernvs(Set<Remplacer> remplacers) {
        this.remplacernvs = remplacers;
        return this;
    }

    public Employe addRemplacernv(Remplacer remplacer) {
        this.remplacernvs.add(remplacer);
        remplacer.setNouveau(this);
        return this;
    }

    public Employe removeRemplacernv(Remplacer remplacer) {
        this.remplacernvs.remove(remplacer);
        remplacer.setNouveau(null);
        return this;
    }

    public void setRemplacernvs(Set<Remplacer> remplacers) {
        this.remplacernvs = remplacers;
    }

    public Set<DroitaccesDocument> getDroitaccesDocuments() {
        return droitaccesDocuments;
    }

    public Employe droitaccesDocuments(Set<DroitaccesDocument> droitaccesDocuments) {
        this.droitaccesDocuments = droitaccesDocuments;
        return this;
    }

    public Employe addDroitaccesDocument(DroitaccesDocument droitaccesDocument) {
        this.droitaccesDocuments.add(droitaccesDocument);
        droitaccesDocument.setEmploye(this);
        return this;
    }

    public Employe removeDroitaccesDocument(DroitaccesDocument droitaccesDocument) {
        this.droitaccesDocuments.remove(droitaccesDocument);
        droitaccesDocument.setEmploye(null);
        return this;
    }

    public void setDroitaccesDocuments(Set<DroitaccesDocument> droitaccesDocuments) {
        this.droitaccesDocuments = droitaccesDocuments;
    }

    public Set<Carriere> getCarrieres() {
        return carrieres;
    }

    public Employe carrieres(Set<Carriere> carrieres) {
        this.carrieres = carrieres;
        return this;
    }

    public Employe addCarriere(Carriere carriere) {
        this.carrieres.add(carriere);
        carriere.setEmploye(this);
        return this;
    }

    public Employe removeCarriere(Carriere carriere) {
        this.carrieres.remove(carriere);
        carriere.setEmploye(null);
        return this;
    }

    public void setCarrieres(Set<Carriere> carrieres) {
        this.carrieres = carrieres;
    }

    public Profilsfonction getProfilsfonction() {
        return profilsfonction;
    }

    public Employe profilsfonction(Profilsfonction profilsfonction) {
        this.profilsfonction = profilsfonction;
        return this;
    }

    public void setProfilsfonction(Profilsfonction profilsfonction) {
        this.profilsfonction = profilsfonction;
    }

    public Set<Competences> getCompetences() {
        return competences;
    }

    public Employe competences(Set<Competences> competences) {
        this.competences = competences;
        return this;
    }

    public Employe addCompetences(Competences competences) {
        this.competences.add(competences);
        competences.setEmploye(this);
        return this;
    }

    public Employe removeCompetences(Competences competences) {
        this.competences.remove(competences);
        competences.setEmploye(null);
        return this;
    }

    public void setCompetences(Set<Competences> competences) {
        this.competences = competences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employe employe = (Employe) o;
        if (employe.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employe{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", dateN='" + dateN + "'" +
            ", cin='" + cin + "'" +
            ", sexe='" + sexe + "'" +
            ", adress='" + adress + "'" +
            ", gsm='" + gsm + "'" +
            ", lieuNais='" + lieuNais + "'" +
            ", telMais='" + telMais + "'" +
            ", teleph='" + teleph + "'" +
            ", delivrele='" + delivrele + "'" +
            ", delivrea='" + delivrea + "'" +
            ", email='" + email + "'" +
            ", matricule='" + matricule + "'" +
            ", email2='" + email2 + "'" +
            ", grade='" + grade + "'" +
            ", rib='" + rib + "'" +
            ", nsc='" + nsc + "'" +
            ", competence='" + competence + "'" +
            ", diplome='" + diplome + "'" +
            ", experience='" + experience + "'" +
            ", aptphy='" + aptphy + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", cv='" + cv + "'" +
            ", cvContentType='" + cvContentType + "'" +
            ", etatDoc='" + etatDoc + "'" +
            ", signature='" + signature + "'" +
            ", signatureContentType='" + signatureContentType + "'" +
            '}';
    }
}
