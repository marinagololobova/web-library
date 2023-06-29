package ru.skypro.lessons.springboot.weblibrary.entity;

import jakarta.persistence.*;
import lombok.*;

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

}
