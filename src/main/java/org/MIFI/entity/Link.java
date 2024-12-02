package org.MIFI.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Link implements  Entity {
    Long id;
    Long idUser;
    String LongLink;
    String shortLink;

    @Override
    public String toString() {
        return "link{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", LongLink='" + LongLink + '\'' +
                ", shortLink='" + shortLink + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Link link = (Link) object;
        return Objects.equals(id, link.id) && Objects.equals(idUser, link.idUser) && Objects.equals(LongLink, link.LongLink) && Objects.equals(shortLink, link.shortLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUser, LongLink, shortLink);
    }
}
