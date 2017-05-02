package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Servicepost.
 */
@Entity
@Table(name = "servicepost")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "servicepost")
public class Servicepost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    private Serviice serviice;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "servicepost_poste",
               joinColumns = @JoinColumn(name="serviceposts_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="postes_id", referencedColumnName="id"))
    private Set<Poste> postes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serviice getServiice() {
        return serviice;
    }

    public Servicepost serviice(Serviice serviice) {
        this.serviice = serviice;
        return this;
    }

    public void setServiice(Serviice serviice) {
        this.serviice = serviice;
    }

    public Set<Poste> getPostes() {
        return postes;
    }

    public Servicepost postes(Set<Poste> postes) {
        this.postes = postes;
        return this;
    }

    public Servicepost addPoste(Poste poste) {
        this.postes.add(poste);
        poste.getServiceposts().add(this);
        return this;
    }

    public Servicepost removePoste(Poste poste) {
        this.postes.remove(poste);
        poste.getServiceposts().remove(this);
        return this;
    }

    public void setPostes(Set<Poste> postes) {
        this.postes = postes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Servicepost servicepost = (Servicepost) o;
        if (servicepost.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, servicepost.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Servicepost{" +
            "id=" + id +
            '}';
    }
}
