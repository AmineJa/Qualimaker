package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LieuxClassement.
 */
@Entity
@Table(name = "lienx_classement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lieuxclassement")
public class LieuxClassement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "lieux")
    private String lieux;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLieux() {
        return lieux;
    }

    public LieuxClassement lieux(String lieux) {
        this.lieux = lieux;
        return this;
    }

    public void setLieux(String lieux) {
        this.lieux = lieux;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LieuxClassement lieuxClassement = (LieuxClassement) o;
        if (lieuxClassement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lieuxClassement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LieuxClassement{" +
            "id=" + id +
            ", lieux='" + lieux + "'" +
            '}';
    }
}
