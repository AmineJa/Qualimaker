package org.qualimaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Serviice.
 */
@Entity
@Table(name = "serviice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "serviice")
public class Serviice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @OneToMany(mappedBy = "serviice")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Poste> postes = new HashSet<>();

    @OneToMany(mappedBy = "serviice")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Servicepost> serviceposts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Serviice nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Poste> getPostes() {
        return postes;
    }

    public Serviice postes(Set<Poste> postes) {
        this.postes = postes;
        return this;
    }

    public Serviice addPoste(Poste poste) {
        this.postes.add(poste);
        poste.setServiice(this);
        return this;
    }

    public Serviice removePoste(Poste poste) {
        this.postes.remove(poste);
        poste.setServiice(null);
        return this;
    }

    public void setPostes(Set<Poste> postes) {
        this.postes = postes;
    }

    public Set<Servicepost> getServiceposts() {
        return serviceposts;
    }

    public Serviice serviceposts(Set<Servicepost> serviceposts) {
        this.serviceposts = serviceposts;
        return this;
    }

    public Serviice addServicepost(Servicepost servicepost) {
        this.serviceposts.add(servicepost);
        servicepost.setServiice(this);
        return this;
    }

    public Serviice removeServicepost(Servicepost servicepost) {
        this.serviceposts.remove(servicepost);
        servicepost.setServiice(null);
        return this;
    }

    public void setServiceposts(Set<Servicepost> serviceposts) {
        this.serviceposts = serviceposts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Serviice serviice = (Serviice) o;
        if (serviice.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Serviice{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            '}';
    }
}
