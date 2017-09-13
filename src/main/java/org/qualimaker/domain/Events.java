package org.qualimaker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Events.
 */
@Entity
@Table(name = "events")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "events")
public class Events implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "start")
    private ZonedDateTime start;

    @Column(name = "description")
    private String description;

    @Column(name = "all_day")
    private Boolean allDay;

    @Column(name = "endtime")
    private ZonedDateTime end;

    @Lob
    @Column(name = "icon")
    private byte[] icon;

    @Column(name = "icon_content_type")
    private String iconContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Events title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public Events start(ZonedDateTime start) {
        this.start = start;
        return this;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public String getDescription() {
        return description;
    }

    public Events description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isAllDay() {
        return allDay;
    }

    public Events allDay(Boolean allDay) {
        this.allDay = allDay;
        return this;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public Events end(ZonedDateTime end) {
        this.end = end;
        return this;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public byte[] getIcon() {
        return icon;
    }

    public Events icon(byte[] icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getIconContentType() {
        return iconContentType;
    }

    public Events iconContentType(String iconContentType) {
        this.iconContentType = iconContentType;
        return this;
    }

    public void setIconContentType(String iconContentType) {
        this.iconContentType = iconContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Events events = (Events) o;
        if (events.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, events.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Events{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", start='" + start + "'" +
            ", description='" + description + "'" +
            ", allDay='" + allDay + "'" +
            ", end='" + end + "'" +
            ", icon='" + icon + "'" +
            ", iconContentType='" + iconContentType + "'" +
            '}';
    }
}
