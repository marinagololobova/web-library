package ru.skypro.lessons.springboot.weblibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.lessons.springboot.weblibrary.dto.ReportDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "SELECT * " +
            "FROM employee e WHERE salary = (SELECT MAX(salary) FROM employee)",
            nativeQuery = true)
    List<Employee> findEmployeesWithHighestSalary();

    List<Employee> findEmployeesByPositionNameLike(String position);

    Employee findEmployeeById(Integer id);

    @Query("SELECT new ru.skypro.lessons.springboot.weblibrary.dto.ReportDTO(e.position.name, count (e.id), max (e.salary), min (e.salary), avg (e.salary)) " +
            "FROM Employee e GROUP BY e.position.name")
    List<ReportDTO> createReports();

}
