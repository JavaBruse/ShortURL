package org.MIFI.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Link implements  Entity {
    Long id;
    String UUID;
    String LongLink;
    String shortLink;
    Long dateStart;
    Long dateEnd;
    Integer transitionLimit;

    public Link(String UUID, String longLink, String shortLink, Long dateEnd, Integer transitionLimit) {
        this.UUID = UUID;
        this.LongLink = longLink;
        this.shortLink = shortLink;
        this.dateStart = new Date().getTime();
        this.dateEnd = dateEnd;
        this.transitionLimit = transitionLimit;
    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", idUser=" + UUID +
                ", LongLink='" + LongLink + '\'' +
                ", shortLink='" + shortLink + '\'' +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", limit=" + transitionLimit +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Link link = (Link) object;
        return Objects.equals(id, link.id) && Objects.equals(UUID, link.UUID) && Objects.equals(LongLink, link.LongLink) && Objects.equals(shortLink, link.shortLink) && Objects.equals(dateStart, link.dateStart) && Objects.equals(dateEnd, link.dateEnd) && Objects.equals(transitionLimit, link.transitionLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, UUID, LongLink, shortLink, dateStart, dateEnd, transitionLimit);
    }

}
