package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Naturediscipline.
 */
@Entity
@Table(name = "naturediscipline")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "naturediscipline")
public class Naturediscipline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nature")
    private String nature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNature() {
        return nature;
    }

    public Naturediscipline nature(String nature) {
        this.nature = nature;
        return this;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Naturediscipline naturediscipline = (Naturediscipline) o;
        if (naturediscipline.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, naturediscipline.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Naturediscipline{" +
            "id=" + id +
            ", nature='" + nature + "'" +
            '}';
    }
}
