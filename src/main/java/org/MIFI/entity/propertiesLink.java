package org.MIFI.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class propertiesLink {
    Long id;
    Long idLink;
    Date dateStart;
    Date dateStop;
    Integer limit;


    @Override
    public String toString() {
        return "propertiesLink{" +
                "id=" + id +
                ", idLink=" + idLink +
                ", dateStart=" + dateStart +
                ", dateStop=" + dateStop +
                ", limit=" + limit +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        propertiesLink that = (propertiesLink) object;
        return Objects.equals(id, that.id) && Objects.equals(idLink, that.idLink) && Objects.equals(dateStart, that.dateStart) && Objects.equals(dateStop, that.dateStop) && Objects.equals(limit, that.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idLink, dateStart, dateStop, limit);
    }
}
