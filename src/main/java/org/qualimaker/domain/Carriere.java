package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
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

    @Column(name = "nom")
    private String nom;

    @Column(name = "etat")
    private String etat;

    @Column(name = "integre")
    private String integre;

    @Column(name = "debut_i_nt")
    private ZonedDateTime debutINt;

    @Column(name = "fin_int")
    private ZonedDateTime finINT;

    @Column(name = "date_rec")
    private ZonedDateTime dateRec;

    @Column(name = "fich_cont")
    private String fichCont;

    @ManyToOne
    private TypeContrat typeContrat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Carriere nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEtat() {
        return etat;
    }

    public Carriere etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getIntegre() {
        return integre;
    }

    public Carriere integre(String integre) {
        this.integre = integre;
        return this;
    }

    public void setIntegre(String integre) {
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

    public String getFichCont() {
        return fichCont;
    }

    public Carriere fichCont(String fichCont) {
        this.fichCont = fichCont;
        return this;
    }

    public void setFichCont(String fichCont) {
        this.fichCont = fichCont;
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
            ", nom='" + nom + "'" +
            ", etat='" + etat + "'" +
            ", integre='" + integre + "'" +
            ", debutINt='" + debutINt + "'" +
            ", finINT='" + finINT + "'" +
            ", dateRec='" + dateRec + "'" +
            ", fichCont='" + fichCont + "'" +
            '}';
    }
}
