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
 * A Carriere.
 */
@Entity
@Table(name = "carriere")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "carriere")
public class Carriere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "integre")
    private Boolean integre;

    @Column(name = "debut_i_nt")
    private ZonedDateTime debutINt;

    @Column(name = "fin_int")
    private ZonedDateTime finINT;

    @Column(name = "date_rec")
    private ZonedDateTime dateRec;

    @Lob
    @Column(name = "document")
    private byte[] document;

    @Column(name = "document_content_type")
    private String documentContentType;

    @Column(name = "actived")
    private Boolean actived;

    @Lob
    @Column(name = "contrat")
    private byte[] contrat;

    @Column(name = "contrat_content_type")
    private String contratContentType;

    @ManyToOne
    private Employe employe;

    @ManyToOne(optional = false)
    @NotNull
    private TypeContrat typeContrat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIntegre() {
        return integre;
    }

    public Carriere integre(Boolean integre) {
        this.integre = integre;
        return this;
    }

    public void setIntegre(Boolean integre) {
        this.integre = integre;
    }

    public ZonedDateTime getDebutINt() {
        return debutINt;
    }

    public Carriere debutINt(ZonedDateTime debutINt) {
        this.debutINt = debutINt;
        return this;
    }

    public void setDebutINt(ZonedDateTime debutINt) {
        this.debutINt = debutINt;
    }

    public ZonedDateTime getFinINT() {
        return finINT;
    }

    public Carriere finINT(ZonedDateTime finINT) {
        this.finINT = finINT;
        return this;
    }

    public void setFinINT(ZonedDateTime finINT) {
        this.finINT = finINT;
    }

    public ZonedDateTime getDateRec() {
        return dateRec;
    }

    public Carriere dateRec(ZonedDateTime dateRec) {
        this.dateRec = dateRec;
        return this;
    }

    public void setDateRec(ZonedDateTime dateRec) {
        this.dateRec = dateRec;
    }

    public byte[] getDocument() {
        return document;
    }

    public Carriere document(byte[] document) {
        this.document = document;
        return this;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public Carriere documentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
        return this;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public Boolean isActived() {
        return actived;
    }

    public Carriere actived(Boolean actived) {
        this.actived = actived;
        return this;
    }

    public void setActived(Boolean actived) {
        this.actived = actived;
    }

    public byte[] getContrat() {
        return contrat;
    }

    public Carriere contrat(byte[] contrat) {
        this.contrat = contrat;
        return this;
    }

    public void setContrat(byte[] contrat) {
        this.contrat = contrat;
    }

    public String getContratContentType() {
        return contratContentType;
    }

    public Carriere contratContentType(String contratContentType) {
        this.contratContentType = contratContentType;
        return this;
    }

    public void setContratContentType(String contratContentType) {
        this.contratContentType = contratContentType;
    }

    public Employe getEmploye() {
        return employe;
    }

    public Carriere employe(Employe employe) {
        this.employe = employe;
        return this;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public TypeContrat getTypeContrat() {
        return typeContrat;
    }

    public Carriere typeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
        return this;
    }

    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Carriere carriere = (Carriere) o;
        if (carriere.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, carriere.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Carriere{" +
            "id=" + id +
            ", integre='" + integre + "'" +
            ", debutINt='" + debutINt + "'" +
            ", finINT='" + finINT + "'" +
            ", dateRec='" + dateRec + "'" +
            ", document='" + document + "'" +
            ", documentContentType='" + documentContentType + "'" +
            ", actived='" + actived + "'" +
            ", contrat='" + contrat + "'" +
            ", contratContentType='" + contratContentType + "'" +
            '}';
    }
}
