package org.qualimaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Jour.
 */
@Entity
@Table(name = "jour")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "jour")
public class Jour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jour")
    private ZonedDateTime jour;

    @OneToOne
    @JoinColumn(unique = true)
    private Programme programme;

    @OneToMany(mappedBy = "jour")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Calendrier> calendriers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getJour() {
        return jour;
    }

    public Jour jour(ZonedDateTime jour) {
        this.jour = jour;
        return this;
    }

    public void setJour(ZonedDateTime jour) {
        this.jour = jour;
    }

    public Programme getProgramme() {
        return programme;
    }

    public Jour programme(Programme programme) {
        this.programme = programme;
        return this;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public Set<Calendrier> getCalendriers() {
        return calendriers;
    }

    public Jour calendriers(Set<Calendrier> calendriers) {
        this.calendriers = calendriers;
        return this;
    }

    public Jour addCalendrier(Calendrier calendrier) {
        this.calendriers.add(calendrier);
        calendrier.setJour(this);
        return this;
    }

    public Jour removeCalendrier(Calendrier calendrier) {
        this.calendriers.remove(calendrier);
        calendrier.setJour(null);
        return this;
    }

    public void setCalendriers(Set<Calendrier> calendriers) {
        this.calendriers = calendriers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Jour jour = (Jour) o;
        if (jour.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jour.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Jour{" +
            "id=" + id +
            ", jour='" + jour + "'" +
            '}';
    }
}
