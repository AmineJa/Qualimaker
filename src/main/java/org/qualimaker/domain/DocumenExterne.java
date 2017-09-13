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
 * A DocumenExterne.
 */
@Entity
@Table(name = "documen_externe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "documenexterne")
public class DocumenExterne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "libele", nullable = false)
    private String libele;

    @Column(name = "daterevision")
    private ZonedDateTime daterevision;

    @Column(name = "indice_evolution")
    private String indiceEvolution;

    @Lob
    @Column(name = "fichier")
    private byte[] fichier;

    @Column(name = "fichier_content_type")
    private String fichierContentType;

    @Column(name = "notif")
    private Integer notif;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "documen_externe_employe",
               joinColumns = @JoinColumn(name="documen_externes_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="employes_id", referencedColumnName="id"))
    private Set<Employe> employes = new HashSet<>();

    @ManyToOne
    private Origine origine;

    @ManyToOne
    private LieuxClassement lieuxClassement;

    @ManyToOne(optional = false)
    @NotNull
    private TypeDocumentation typeDocumentation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public DocumenExterne code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibele() {
        return libele;
    }

    public DocumenExterne libele(String libele) {
        this.libele = libele;
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public ZonedDateTime getDaterevision() {
        return daterevision;
    }

    public DocumenExterne daterevision(ZonedDateTime daterevision) {
        this.daterevision = daterevision;
        return this;
    }

    public void setDaterevision(ZonedDateTime daterevision) {
        this.daterevision = daterevision;
    }

    public String getIndiceEvolution() {
        return indiceEvolution;
    }

    public DocumenExterne indiceEvolution(String indiceEvolution) {
        this.indiceEvolution = indiceEvolution;
        return this;
    }

    public void setIndiceEvolution(String indiceEvolution) {
        this.indiceEvolution = indiceEvolution;
    }

    public byte[] getFichier() {
        return fichier;
    }

    public DocumenExterne fichier(byte[] fichier) {
        this.fichier = fichier;
        return this;
    }

    public void setFichier(byte[] fichier) {
        this.fichier = fichier;
    }

    public String getFichierContentType() {
        return fichierContentType;
    }

    public DocumenExterne fichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
        return this;
    }

    public void setFichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
    }

    public Integer getNotif() {
        return notif;
    }

    public DocumenExterne notif(Integer notif) {
        this.notif = notif;
        return this;
    }

    public void setNotif(Integer notif) {
        this.notif = notif;
    }

    public Set<Employe> getEmployes() {
        return employes;
    }

    public DocumenExterne employes(Set<Employe> employes) {
        this.employes = employes;
        return this;
    }

    public DocumenExterne addEmploye(Employe employe) {
        this.employes.add(employe);
        employe.getDocumenExternes().add(this);
        return this;
    }

    public DocumenExterne removeEmploye(Employe employe) {
        this.employes.remove(employe);
        employe.getDocumenExternes().remove(this);
        return this;
    }

    public void setEmployes(Set<Employe> employes) {
        this.employes = employes;
    }

    public Origine getOrigine() {
        return origine;
    }

    public DocumenExterne origine(Origine origine) {
        this.origine = origine;
        return this;
    }

    public void setOrigine(Origine origine) {
        this.origine = origine;
    }

    public LieuxClassement getLieuxClassement() {
        return lieuxClassement;
    }

    public DocumenExterne lieuxClassement(LieuxClassement lieuxClassement) {
        this.lieuxClassement = lieuxClassement;
        return this;
    }

    public void setLieuxClassement(LieuxClassement lieuxClassement) {
        this.lieuxClassement = lieuxClassement;
    }

    public TypeDocumentation getTypeDocumentation() {
        return typeDocumentation;
    }

    public DocumenExterne typeDocumentation(TypeDocumentation typeDocumentation) {
        this.typeDocumentation = typeDocumentation;
        return this;
    }

    public void setTypeDocumentation(TypeDocumentation typeDocumentation) {
        this.typeDocumentation = typeDocumentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumenExterne documenExterne = (DocumenExterne) o;
        if (documenExterne.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, documenExterne.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DocumenExterne{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", libele='" + libele + "'" +
            ", daterevision='" + daterevision + "'" +
            ", indiceEvolution='" + indiceEvolution + "'" +
            ", fichier='" + fichier + "'" +
            ", fichierContentType='" + fichierContentType + "'" +
            ", notif='" + notif + "'" +
            '}';
    }
}
