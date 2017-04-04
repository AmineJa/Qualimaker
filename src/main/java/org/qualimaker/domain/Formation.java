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

/**
 * A Formation.
 */
@Entity
@Table(name = "formation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "formation")
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "theme", nullable = false)
    private String theme;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @NotNull
    @Column(name = "date_d", nullable = false)
    private ZonedDateTime dateD;

    @NotNull
    @Column(name = "date_f", nullable = false)
    private ZonedDateTime dateF;

    @Column(name = "lieu")
    private String lieu;

    @Column(name = "objectif")
    private String objectif;

    @Column(name = "contenu")
    private String contenu;

    @Column(name = "cout_mat")
    private String coutMat;

    @Column(name = "cout_imat")
    private String coutImat;

    @Column(name = "reccurence")
    private Boolean reccurence;

    @Column(name = "periode")
    private String periode;

    @Column(name = "finaprs")
    private Boolean finaprs;

    @Column(name = "termina")
    private Boolean termina;

    @OneToOne
    @JoinColumn(unique = true)
    private DemandeFormation demandeformation;

    @OneToOne
    @JoinColumn(unique = true)
    private FormationComp formationcomp;

    @OneToOne
    @JoinColumn(unique = true)
    private Formateur formateur;

    @ManyToOne
    private Natureformation natureFormation;

    @ManyToOne
    private Fichierjoint fichierjoint;

    @ManyToOne
    private Critereevaluation critereevaluation;

    @ManyToOne
    private Jour jour;

    @ManyToMany(mappedBy = "formations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Employe> employes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public Formation theme(String theme) {
        this.theme = theme;
        return this;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTitre() {
        return titre;
    }

    public Formation titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public ZonedDateTime getDateD() {
        return dateD;
    }

    public Formation dateD(ZonedDateTime dateD) {
        this.dateD = dateD;
        return this;
    }

    public void setDateD(ZonedDateTime dateD) {
        this.dateD = dateD;
    }

    public ZonedDateTime getDateF() {
        return dateF;
    }

    public Formation dateF(ZonedDateTime dateF) {
        this.dateF = dateF;
        return this;
    }

    public void setDateF(ZonedDateTime dateF) {
        this.dateF = dateF;
    }

    public String getLieu() {
        return lieu;
    }

    public Formation lieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getObjectif() {
        return objectif;
    }

    public Formation objectif(String objectif) {
        this.objectif = objectif;
        return this;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getContenu() {
        return contenu;
    }

    public Formation contenu(String contenu) {
        this.contenu = contenu;
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getCoutMat() {
        return coutMat;
    }

    public Formation coutMat(String coutMat) {
        this.coutMat = coutMat;
        return this;
    }

    public void setCoutMat(String coutMat) {
        this.coutMat = coutMat;
    }

    public String getCoutImat() {
        return coutImat;
    }

    public Formation coutImat(String coutImat) {
        this.coutImat = coutImat;
        return this;
    }

    public void setCoutImat(String coutImat) {
        this.coutImat = coutImat;
    }

    public Boolean isReccurence() {
        return reccurence;
    }

    public Formation reccurence(Boolean reccurence) {
        this.reccurence = reccurence;
        return this;
    }

    public void setReccurence(Boolean reccurence) {
        this.reccurence = reccurence;
    }

    public String getPeriode() {
        return periode;
    }

    public Formation periode(String periode) {
        this.periode = periode;
        return this;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Boolean isFinaprs() {
        return finaprs;
    }

    public Formation finaprs(Boolean finaprs) {
        this.finaprs = finaprs;
        return this;
    }

    public void setFinaprs(Boolean finaprs) {
        this.finaprs = finaprs;
    }

    public Boolean isTermina() {
        return termina;
    }

    public Formation termina(Boolean termina) {
        this.termina = termina;
        return this;
    }

    public void setTermina(Boolean termina) {
        this.termina = termina;
    }

    public DemandeFormation getDemandeformation() {
        return demandeformation;
    }

    public Formation demandeformation(DemandeFormation demandeFormation) {
        this.demandeformation = demandeFormation;
        return this;
    }

    public void setDemandeformation(DemandeFormation demandeFormation) {
        this.demandeformation = demandeFormation;
    }

    public FormationComp getFormationcomp() {
        return formationcomp;
    }

    public Formation formationcomp(FormationComp formationComp) {
        this.formationcomp = formationComp;
        return this;
    }

    public void setFormationcomp(FormationComp formationComp) {
        this.formationcomp = formationComp;
    }

    public Formateur getFormateur() {
        return formateur;
    }

    public Formation formateur(Formateur formateur) {
        this.formateur = formateur;
        return this;
    }

    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }

    public Natureformation getNatureFormation() {
        return natureFormation;
    }

    public Formation natureFormation(Natureformation natureformation) {
        this.natureFormation = natureformation;
        return this;
    }

    public void setNatureFormation(Natureformation natureformation) {
        this.natureFormation = natureformation;
    }

    public Fichierjoint getFichierjoint() {
        return fichierjoint;
    }

    public Formation fichierjoint(Fichierjoint fichierjoint) {
        this.fichierjoint = fichierjoint;
        return this;
    }

    public void setFichierjoint(Fichierjoint fichierjoint) {
        this.fichierjoint = fichierjoint;
    }

    public Critereevaluation getCritereevaluation() {
        return critereevaluation;
    }

    public Formation critereevaluation(Critereevaluation critereevaluation) {
        this.critereevaluation = critereevaluation;
        return this;
    }

    public void setCritereevaluation(Critereevaluation critereevaluation) {
        this.critereevaluation = critereevaluation;
    }

    public Jour getJour() {
        return jour;
    }

    public Formation jour(Jour jour) {
        this.jour = jour;
        return this;
    }

    public void setJour(Jour jour) {
        this.jour = jour;
    }

    public Set<Employe> getEmployes() {
        return employes;
    }

    public Formation employes(Set<Employe> employes) {
        this.employes = employes;
        return this;
    }

    public Formation addEmploye(Employe employe) {
        this.employes.add(employe);
        employe.getFormations().add(this);
        return this;
    }

    public Formation removeEmploye(Employe employe) {
        this.employes.remove(employe);
        employe.getFormations().remove(this);
        return this;
    }

    public void setEmployes(Set<Employe> employes) {
        this.employes = employes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Formation formation = (Formation) o;
        if (formation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, formation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Formation{" +
            "id=" + id +
            ", theme='" + theme + "'" +
            ", titre='" + titre + "'" +
            ", dateD='" + dateD + "'" +
            ", dateF='" + dateF + "'" +
            ", lieu='" + lieu + "'" +
            ", objectif='" + objectif + "'" +
            ", contenu='" + contenu + "'" +
            ", coutMat='" + coutMat + "'" +
            ", coutImat='" + coutImat + "'" +
            ", reccurence='" + reccurence + "'" +
            ", periode='" + periode + "'" +
            ", finaprs='" + finaprs + "'" +
            ", termina='" + termina + "'" +
            '}';
    }
}
