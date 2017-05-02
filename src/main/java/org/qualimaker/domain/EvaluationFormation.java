package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EvaluationFormation.
 */
@Entity
@Table(name = "evaluation_formation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "evaluationformation")
public class EvaluationFormation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "matricuel")
    private Double matricuel;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "effectue")
    private Float effectue;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "afroid")
    private Float afroid;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "achaud")
    private Float achaud;

    @Column(name = "evaluerpar")
    private String evaluerpar;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "refaire")
    private Float refaire;

    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "dateprevu")
    private String dateprevu;

    @Column(name = "efficace")
    private Boolean efficace;

    @Column(name = "commentaire")
    private String commentaire;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "methodologie")
    private Float methodologie;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "competece")
    private Float competece;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "supportcours")
    private Float supportcours;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "animation")
    private Float animation;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "lieu")
    private Float lieu;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "respecthoraire")
    private Float respecthoraire;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "contnucours")
    private Float contnucours;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "traveauxp")
    private Float traveauxp;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "objectif")
    private Float objectif;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "duree")
    private Float duree;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "ambiace")
    private Float ambiace;

    @ManyToOne
    private Formation formation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMatricuel() {
        return matricuel;
    }

    public EvaluationFormation matricuel(Double matricuel) {
        this.matricuel = matricuel;
        return this;
    }

    public void setMatricuel(Double matricuel) {
        this.matricuel = matricuel;
    }

    public Float getEffectue() {
        return effectue;
    }

    public EvaluationFormation effectue(Float effectue) {
        this.effectue = effectue;
        return this;
    }

    public void setEffectue(Float effectue) {
        this.effectue = effectue;
    }

    public Float getAfroid() {
        return afroid;
    }

    public EvaluationFormation afroid(Float afroid) {
        this.afroid = afroid;
        return this;
    }

    public void setAfroid(Float afroid) {
        this.afroid = afroid;
    }

    public Float getAchaud() {
        return achaud;
    }

    public EvaluationFormation achaud(Float achaud) {
        this.achaud = achaud;
        return this;
    }

    public void setAchaud(Float achaud) {
        this.achaud = achaud;
    }

    public String getEvaluerpar() {
        return evaluerpar;
    }

    public EvaluationFormation evaluerpar(String evaluerpar) {
        this.evaluerpar = evaluerpar;
        return this;
    }

    public void setEvaluerpar(String evaluerpar) {
        this.evaluerpar = evaluerpar;
    }

    public Float getRefaire() {
        return refaire;
    }

    public EvaluationFormation refaire(Float refaire) {
        this.refaire = refaire;
        return this;
    }

    public void setRefaire(Float refaire) {
        this.refaire = refaire;
    }

    public String getDateprevu() {
        return dateprevu;
    }

    public EvaluationFormation dateprevu(String dateprevu) {
        this.dateprevu = dateprevu;
        return this;
    }

    public void setDateprevu(String dateprevu) {
        this.dateprevu = dateprevu;
    }

    public Boolean isEfficace() {
        return efficace;
    }

    public EvaluationFormation efficace(Boolean efficace) {
        this.efficace = efficace;
        return this;
    }

    public void setEfficace(Boolean efficace) {
        this.efficace = efficace;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public EvaluationFormation commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Float getMethodologie() {
        return methodologie;
    }

    public EvaluationFormation methodologie(Float methodologie) {
        this.methodologie = methodologie;
        return this;
    }

    public void setMethodologie(Float methodologie) {
        this.methodologie = methodologie;
    }

    public Float getCompetece() {
        return competece;
    }

    public EvaluationFormation competece(Float competece) {
        this.competece = competece;
        return this;
    }

    public void setCompetece(Float competece) {
        this.competece = competece;
    }

    public Float getSupportcours() {
        return supportcours;
    }

    public EvaluationFormation supportcours(Float supportcours) {
        this.supportcours = supportcours;
        return this;
    }

    public void setSupportcours(Float supportcours) {
        this.supportcours = supportcours;
    }

    public Float getAnimation() {
        return animation;
    }

    public EvaluationFormation animation(Float animation) {
        this.animation = animation;
        return this;
    }

    public void setAnimation(Float animation) {
        this.animation = animation;
    }

    public Float getLieu() {
        return lieu;
    }

    public EvaluationFormation lieu(Float lieu) {
        this.lieu = lieu;
        return this;
    }

    public void setLieu(Float lieu) {
        this.lieu = lieu;
    }

    public Float getRespecthoraire() {
        return respecthoraire;
    }

    public EvaluationFormation respecthoraire(Float respecthoraire) {
        this.respecthoraire = respecthoraire;
        return this;
    }

    public void setRespecthoraire(Float respecthoraire) {
        this.respecthoraire = respecthoraire;
    }

    public Float getContnucours() {
        return contnucours;
    }

    public EvaluationFormation contnucours(Float contnucours) {
        this.contnucours = contnucours;
        return this;
    }

    public void setContnucours(Float contnucours) {
        this.contnucours = contnucours;
    }

    public Float getTraveauxp() {
        return traveauxp;
    }

    public EvaluationFormation traveauxp(Float traveauxp) {
        this.traveauxp = traveauxp;
        return this;
    }

    public void setTraveauxp(Float traveauxp) {
        this.traveauxp = traveauxp;
    }

    public Float getObjectif() {
        return objectif;
    }

    public EvaluationFormation objectif(Float objectif) {
        this.objectif = objectif;
        return this;
    }

    public void setObjectif(Float objectif) {
        this.objectif = objectif;
    }

    public Float getDuree() {
        return duree;
    }

    public EvaluationFormation duree(Float duree) {
        this.duree = duree;
        return this;
    }

    public void setDuree(Float duree) {
        this.duree = duree;
    }

    public Float getAmbiace() {
        return ambiace;
    }

    public EvaluationFormation ambiace(Float ambiace) {
        this.ambiace = ambiace;
        return this;
    }

    public void setAmbiace(Float ambiace) {
        this.ambiace = ambiace;
    }

    public Formation getFormation() {
        return formation;
    }

    public EvaluationFormation formation(Formation formation) {
        this.formation = formation;
        return this;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EvaluationFormation evaluationFormation = (EvaluationFormation) o;
        if (evaluationFormation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, evaluationFormation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EvaluationFormation{" +
            "id=" + id +
            ", matricuel='" + matricuel + "'" +
            ", effectue='" + effectue + "'" +
            ", afroid='" + afroid + "'" +
            ", achaud='" + achaud + "'" +
            ", evaluerpar='" + evaluerpar + "'" +
            ", refaire='" + refaire + "'" +
            ", dateprevu='" + dateprevu + "'" +
            ", efficace='" + efficace + "'" +
            ", commentaire='" + commentaire + "'" +
            ", methodologie='" + methodologie + "'" +
            ", competece='" + competece + "'" +
            ", supportcours='" + supportcours + "'" +
            ", animation='" + animation + "'" +
            ", lieu='" + lieu + "'" +
            ", respecthoraire='" + respecthoraire + "'" +
            ", contnucours='" + contnucours + "'" +
            ", traveauxp='" + traveauxp + "'" +
            ", objectif='" + objectif + "'" +
            ", duree='" + duree + "'" +
            ", ambiace='" + ambiace + "'" +
            '}';
    }
}
