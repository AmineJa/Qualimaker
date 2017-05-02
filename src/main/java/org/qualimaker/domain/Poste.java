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
 * A Poste.
 */
@Entity
@Table(name = "poste")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "poste")
public class Poste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "post")
    private String post;

    @Column(name = "parent")
    private String parent;

    @Column(name = "supp")
    private String supp;

    @ManyToOne
    private Serviice serviice;

    @ManyToMany
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Servicepost> serviceposts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public Poste post(String post) {
        this.post = post;
        return this;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getParent() {
        return parent;
    }

    public Poste parent(String parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSupp() {
        return supp;
    }

    public Poste supp(String supp) {
        this.supp = supp;
        return this;
    }

    public void setSupp(String supp) {
        this.supp = supp;
    }

    public Serviice getServiice() {
        return serviice;
    }

    public Poste serviice(Serviice serviice) {
        this.serviice = serviice;
        return this;
    }

    public void setServiice(Serviice serviice) {
        this.serviice = serviice;
    }

    public Set<Servicepost> getServiceposts() {
        return serviceposts;
    }

    public Poste serviceposts(Set<Servicepost> serviceposts) {
        this.serviceposts = serviceposts;
        return this;
    }

    public Poste addServicepost(Servicepost servicepost) {
        this.serviceposts.add(servicepost);
        servicepost.getPostes().add(this);
        return this;
    }

    public Poste removeServicepost(Servicepost servicepost) {
        this.serviceposts.remove(servicepost);
        servicepost.getPostes().remove(this);
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
        Poste poste = (Poste) o;
        if (poste.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, poste.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Poste{" +
            "id=" + id +
            ", post='" + post + "'" +
            ", parent='" + parent + "'" +
            ", supp='" + supp + "'" +
            '}';
    }
}
