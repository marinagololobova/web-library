package ru.skypro.lessons.springboot.weblibrary.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "position")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 20)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(id, position.id) && Objects.equals(name, position.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
