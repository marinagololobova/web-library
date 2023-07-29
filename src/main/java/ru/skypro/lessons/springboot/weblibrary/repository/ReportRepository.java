package ru.skypro.lessons.springboot.weblibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.lessons.springboot.weblibrary.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
}
