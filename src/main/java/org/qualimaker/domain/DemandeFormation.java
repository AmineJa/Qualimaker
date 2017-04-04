package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DemandeFormation.
 */
@Entity
@Table(name = "demande_formation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "demandeformation")
public class DemandeFormation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "theme")
    private String theme;

    @Column(name = "date_demande")
    private ZonedDateTime dateDemande;

    @Column(name = "datesouhaite")
    private ZonedDateTime datesouhaite;

    @Column(name = "nombresjours")
    private String nombresjours;

    @Column(name = "description")
    private String description;

    @Column(name = "justification")
    private String justification;

    @ManyToOne
    private Employe empploye;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public DemandeFormation theme(String theme) {
        this.theme = theme;
        return this;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public ZonedDateTime getDateDemande() {
        return dateDemande;
    }

    public DemandeFormation dateDemande(ZonedDateTime dateDemande) {
        this.dateDemande = dateDemande;
        return this;
    }

    public void setDateDemande(ZonedDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public ZonedDateTime getDatesouhaite() {
        return datesouhaite;
    }

    public DemandeFormation datesouhaite(ZonedDateTime datesouhaite) {
        this.datesouhaite = datesouhaite;
        return this;
    }

    public void setDatesouhaite(ZonedDateTime datesouhaite) {
        this.datesouhaite = datesouhaite;
    }

    public String getNombresjours() {
        return nombresjours;
    }

    public DemandeFormation nombresjours(String nombresjours) {
        this.nombresjours = nombresjours;
        return this;
    }

    public void setNombresjours(String nombresjours) {
        this.nombresjours = nombresjours;
    }

    public String getDescription() {
        return description;
    }

    public DemandeFormation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJustification() {
        return justification;
    }

    public DemandeFormation justification(String justification) {
        this.justification = justification;
        return this;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Employe getEmpploye() {
        return empploye;
    }

    public DemandeFormation empploye(Employe employe) {
        this.empploye = employe;
        return this;
    }

    public void setEmpploye(Employe employe) {
        this.empploye = employe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DemandeFormation demandeFormation = (DemandeFormation) o;
        if (demandeFormation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, demandeFormation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DemandeFormation{" +
            "id=" + id +
            ", theme='" + theme + "'" +
            ", dateDemande='" + dateDemande + "'" +
            ", datesouhaite='" + datesouhaite + "'" +
            ", nombresjours='" + nombresjours + "'" +
            ", description='" + description + "'" +
            ", justification='" + justification + "'" +
            '}';
    }
}
