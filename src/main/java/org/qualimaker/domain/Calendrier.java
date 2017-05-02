package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Calendrier.
 */
@Entity
@Table(name = "calendrier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "calendrier")
public class Calendrier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    private Jour jour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jour getJour() {
        return jour;
    }

    public Calendrier jour(Jour jour) {
        this.jour = jour;
        return this;
    }

    public void setJour(Jour jour) {
        this.jour = jour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Calendrier calendrier = (Calendrier) o;
        if (calendrier.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, calendrier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Calendrier{" +
            "id=" + id +
            '}';
    }
}
