package ru.skypro.lessons.springboot.weblibrary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Lob
    private String report;

    @CreationTimestamp()
    @Column(updatable = false)
    private Instant createdAt;

}
