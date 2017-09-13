package org.qualimaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.qualimaker.domain.enumeration.NatureDoc;

/**
 * A TypeDocumentation.
 */
@Entity
@Table(name = "type_documentation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "typedocumentation")
public class TypeDocumentation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "p_df")
    private Boolean pDf;

    @Column(name = "securise")
    private Boolean securise;

    @Column(name = "abreviation")
    private String abreviation;

    @Enumerated(EnumType.STRING)
    @Column(name = "nature")
    private NatureDoc nature;

    @Column(name = "notif")
    private Integer notif;

    @ManyToOne(optional = false)
    @NotNull
    private Employe employe;

    @OneToMany(mappedBy = "typeDocumentation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DocumenExterne> documenExternes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public TypeDocumentation type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean ispDf() {
        return pDf;
    }

    public TypeDocumentation pDf(Boolean pDf) {
        this.pDf = pDf;
        return this;
    }

    public void setpDf(Boolean pDf) {
        this.pDf = pDf;
    }

    public Boolean isSecurise() {
        return securise;
    }

    public TypeDocumentation securise(Boolean securise) {
        this.securise = securise;
        return this;
    }

    public void setSecurise(Boolean securise) {
        this.securise = securise;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public TypeDocumentation abreviation(String abreviation) {
        this.abreviation = abreviation;
        return this;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public NatureDoc getNature() {
        return nature;
    }

    public TypeDocumentation nature(NatureDoc nature) {
        this.nature = nature;
        return this;
    }

    public void setNature(NatureDoc nature) {
        this.nature = nature;
    }

    public Integer getNotif() {
        return notif;
    }

    public TypeDocumentation notif(Integer notif) {
        this.notif = notif;
        return this;
    }

    public void setNotif(Integer notif) {
        this.notif = notif;
    }

    public Employe getEmploye() {
        return employe;
    }

    public TypeDocumentation employe(Employe employe) {
        this.employe = employe;
        return this;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Set<DocumenExterne> getDocumenExternes() {
        return documenExternes;
    }

    public TypeDocumentation documenExternes(Set<DocumenExterne> documenExternes) {
        this.documenExternes = documenExternes;
        return this;
    }

    public TypeDocumentation addDocumenExterne(DocumenExterne documenExterne) {
        this.documenExternes.add(documenExterne);
        documenExterne.setTypeDocumentation(this);
        return this;
    }

    public TypeDocumentation removeDocumenExterne(DocumenExterne documenExterne) {
        this.documenExternes.remove(documenExterne);
        documenExterne.setTypeDocumentation(null);
        return this;
    }

    public void setDocumenExternes(Set<DocumenExterne> documenExternes) {
        this.documenExternes = documenExternes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeDocumentation typeDocumentation = (TypeDocumentation) o;
        if (typeDocumentation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, typeDocumentation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TypeDocumentation{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", pDf='" + pDf + "'" +
            ", securise='" + securise + "'" +
            ", abreviation='" + abreviation + "'" +
            ", nature='" + nature + "'" +
            ", notif='" + notif + "'" +
            '}';
    }
}
