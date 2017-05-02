package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Abscence.
 */
@Entity
@Table(name = "abscence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "abscence")
public class Abscence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "justifier")
    private String justifier;

    @Column(name = "date_d")
    private ZonedDateTime dateD;

    @Column(name = "date_f")
    private ZonedDateTime dateF;

    @Column(name = "etat")
    private String etat;

    @Column(name = "integre")
    private String integre;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "oui")
    private Boolean oui;

    @Column(name = "non")
    private Boolean non;

    @Lob
    @Column(name = "doc")
    private byte[] doc;

    @Column(name = "doc_content_type")
    private String docContentType;

    @Column(name = "com")
    private String com;

    @ManyToOne
    private Employe employe;

    @ManyToOne
    private NatureAbs natureAbs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJustifier() {
        return justifier;
    }

    public Abscence justifier(String justifier) {
        this.justifier = justifier;
        return this;
    }

    public void setJustifier(String justifier) {
        this.justifier = justifier;
    }

    public ZonedDateTime getDateD() {
        return dateD;
    }

    public Abscence dateD(ZonedDateTime dateD) {
        this.dateD = dateD;
        return this;
    }

    public void setDateD(ZonedDateTime dateD) {
        this.dateD = dateD;
    }

    public ZonedDateTime getDateF() {
        return dateF;
    }

    public Abscence dateF(ZonedDateTime dateF) {
        this.dateF = dateF;
        return this;
    }

    public void setDateF(ZonedDateTime dateF) {
        this.dateF = dateF;
    }

    public String getEtat() {
        return etat;
    }

    public Abscence etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getIntegre() {
        return integre;
    }

    public Abscence integre(String integre) {
        this.integre = integre;
        return this;
    }

    public void setIntegre(String integre) {
        this.integre = integre;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Abscence commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean isOui() {
        return oui;
    }

    public Abscence oui(Boolean oui) {
        this.oui = oui;
        return this;
    }

    public void setOui(Boolean oui) {
        this.oui = oui;
    }

    public Boolean isNon() {
        return non;
    }

    public Abscence non(Boolean non) {
        this.non = non;
        return this;
    }

    public void setNon(Boolean non) {
        this.non = non;
    }

    public byte[] getDoc() {
        return doc;
    }

    public Abscence doc(byte[] doc) {
        this.doc = doc;
        return this;
    }

    public void setDoc(byte[] doc) {
        this.doc = doc;
    }

    public String getDocContentType() {
        return docContentType;
    }

    public Abscence docContentType(String docContentType) {
        this.docContentType = docContentType;
        return this;
    }

    public void setDocContentType(String docContentType) {
        this.docContentType = docContentType;
    }

    public String getCom() {
        return com;
    }

    public Abscence com(String com) {
        this.com = com;
        return this;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public Employe getEmploye() {
        return employe;
    }

    public Abscence employe(Employe employe) {
        this.employe = employe;
        return this;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public NatureAbs getNatureAbs() {
        return natureAbs;
    }

    public Abscence natureAbs(NatureAbs natureAbs) {
        this.natureAbs = natureAbs;
        return this;
    }

    public void setNatureAbs(NatureAbs natureAbs) {
        this.natureAbs = natureAbs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Abscence abscence = (Abscence) o;
        if (abscence.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, abscence.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Abscence{" +
            "id=" + id +
            ", justifier='" + justifier + "'" +
            ", dateD='" + dateD + "'" +
            ", dateF='" + dateF + "'" +
            ", etat='" + etat + "'" +
            ", integre='" + integre + "'" +
            ", commentaire='" + commentaire + "'" +
            ", oui='" + oui + "'" +
            ", non='" + non + "'" +
            ", doc='" + doc + "'" +
            ", docContentType='" + docContentType + "'" +
            ", com='" + com + "'" +
            '}';
    }
}
