package org.MIFI.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class User implements  Entity {
    Long id;
    String name;
    String UUID;


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(UUID, user.UUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, UUID);
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", UUID='" + UUID + '\'' +
                '}';
    }
}
