package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Enregistrement.
 */
@Entity
@Table(name = "enregistrement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "enregistrement")
public class Enregistrement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @NotNull
    @Lob
    @Column(name = "fichier", nullable = false)
    private byte[] fichier;

    @Column(name = "fichier_content_type", nullable = false)
    private String fichierContentType;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "motclef")
    private String motclef;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "emp")
    private String emp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Enregistrement libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTitre() {
        return titre;
    }

    public Enregistrement titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public byte[] getFichier() {
        return fichier;
    }

    public Enregistrement fichier(byte[] fichier) {
        this.fichier = fichier;
        return this;
    }

    public void setFichier(byte[] fichier) {
        this.fichier = fichier;
    }

    public String getFichierContentType() {
        return fichierContentType;
    }

    public Enregistrement fichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
        return this;
    }

    public void setFichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Enregistrement commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getMotclef() {
        return motclef;
    }

    public Enregistrement motclef(String motclef) {
        this.motclef = motclef;
        return this;
    }

    public void setMotclef(String motclef) {
        this.motclef = motclef;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Enregistrement date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getEmp() {
        return emp;
    }

    public Enregistrement emp(String emp) {
        this.emp = emp;
        return this;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enregistrement enregistrement = (Enregistrement) o;
        if (enregistrement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, enregistrement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Enregistrement{" +
            "id=" + id +
            ", libelle='" + libelle + "'" +
            ", titre='" + titre + "'" +
            ", fichier='" + fichier + "'" +
            ", fichierContentType='" + fichierContentType + "'" +
            ", commentaire='" + commentaire + "'" +
            ", motclef='" + motclef + "'" +
            ", date='" + date + "'" +
            ", emp='" + emp + "'" +
            '}';
    }
}
