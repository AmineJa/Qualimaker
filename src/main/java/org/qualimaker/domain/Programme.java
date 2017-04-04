package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Programme.
 */
@Entity
@Table(name = "programme")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "programme")
public class Programme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "matin")
    private String matin;

    @Column(name = "apresmidi")
    private String apresmidi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatin() {
        return matin;
    }

    public Programme matin(String matin) {
        this.matin = matin;
        return this;
    }

    public void setMatin(String matin) {
        this.matin = matin;
    }

    public String getApresmidi() {
        return apresmidi;
    }

    public Programme apresmidi(String apresmidi) {
        this.apresmidi = apresmidi;
        return this;
    }

    public void setApresmidi(String apresmidi) {
        this.apresmidi = apresmidi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Programme programme = (Programme) o;
        if (programme.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, programme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Programme{" +
            "id=" + id +
            ", matin='" + matin + "'" +
            ", apresmidi='" + apresmidi + "'" +
            '}';
    }
}
