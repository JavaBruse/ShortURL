package org.MIFI.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Data
@NoArgsConstructor
public class User implements  Entity {
    String UUID;
    String name;


    @Override
    public String toString() {
        return "User{" +
                "UUID='" + UUID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(UUID, user.UUID) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UUID, name);
    }
}
