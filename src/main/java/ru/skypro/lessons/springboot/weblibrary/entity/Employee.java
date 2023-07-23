package ru.skypro.lessons.springboot.weblibrary.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Objects;


@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 20)
    private String name;
    private Integer salary;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id")
    private Position position;

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(salary, employee.salary) && Objects.equals(position, employee.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, position);
    }
}
