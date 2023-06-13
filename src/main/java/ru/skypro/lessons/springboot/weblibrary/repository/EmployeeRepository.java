package ru.skypro.lessons.springboot.weblibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "SELECT * " +
            "FROM employee e WHERE salary = (SELECT MAX(salary) FROM employee)",
            nativeQuery = true)
    List<Employee> findEmployeesWithHighestSalary();

    @Query(value = "SELECT * " +
            "FROM employee e WHERE position_id = position_id",
            nativeQuery = true)
    List<Employee> findEmployeesByPosition(String position);

    Employee findEmployeeById(Integer id);

}
