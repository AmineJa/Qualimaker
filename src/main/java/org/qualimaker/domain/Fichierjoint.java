package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Fichierjoint.
 */
@Entity
@Table(name = "fichierjoint")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fichierjoint")
public class Fichierjoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "commentaire")
    private String commentaire;

    @Lob
    @Column(name = "fichejoint")
    private byte[] fichejoint;

    @Column(name = "fichejoint_content_type")
    private String fichejointContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Fichierjoint nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Fichierjoint commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public byte[] getFichejoint() {
        return fichejoint;
    }

    public Fichierjoint fichejoint(byte[] fichejoint) {
        this.fichejoint = fichejoint;
        return this;
    }

    public void setFichejoint(byte[] fichejoint) {
        this.fichejoint = fichejoint;
    }

    public String getFichejointContentType() {
        return fichejointContentType;
    }

    public Fichierjoint fichejointContentType(String fichejointContentType) {
        this.fichejointContentType = fichejointContentType;
        return this;
    }

    public void setFichejointContentType(String fichejointContentType) {
        this.fichejointContentType = fichejointContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fichierjoint fichierjoint = (Fichierjoint) o;
        if (fichierjoint.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fichierjoint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Fichierjoint{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", commentaire='" + commentaire + "'" +
            ", fichejoint='" + fichejoint + "'" +
            ", fichejointContentType='" + fichejointContentType + "'" +
            '}';
    }
}
