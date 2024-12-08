package org.MIFI.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
public class Link implements Entity {
    Long id;
    String UUID;
    String longLink;
    String shortLink;
    Long dateStart;
    Long dateEnd;
    Integer transitionLimit;

    @Override
    public String toString() {
        return "Длинная ссылка: " + longLink + ", " +
                "короткая ссылка: " + shortLink + ", " +
                "Оставшееся время жизни ссылки: " + getDateString() + ", " +
                "Осталось переходов: " + transitionLimit;

    }

    private String getDateString() {
        Long date = dateEnd - new Date().getTime();

        long days = TimeUnit.MILLISECONDS.toDays(date);
        long hours = TimeUnit.MILLISECONDS.toHours(date) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(date) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(date));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(date) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(date));

        return String.format("%d дней, %d ч %d м %d сек", days, hours, minutes, seconds);
//        return new SimpleDateFormat("dd.MM. HH:mm:ss").format(new Date(date));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Link link = (Link) object;
        return Objects.equals(id, link.id) && Objects.equals(UUID, link.UUID) && Objects.equals(longLink, link.longLink) && Objects.equals(shortLink, link.shortLink) && Objects.equals(dateStart, link.dateStart) && Objects.equals(dateEnd, link.dateEnd) && Objects.equals(transitionLimit, link.transitionLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, UUID, longLink, shortLink, dateStart, dateEnd, transitionLimit);
    }

}
