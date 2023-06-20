package ru.skypro.lessons.springboot.weblibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReportDTO {
    private String position;
    private long count;
    private int maxSalary;
    private int minSalary;
    private double averageSalary;
}
