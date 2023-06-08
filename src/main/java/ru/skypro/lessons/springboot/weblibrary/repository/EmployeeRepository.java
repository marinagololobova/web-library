package ru.skypro.lessons.springboot.weblibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;
import ru.skypro.lessons.springboot.weblibrary.entity.Position;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "SELECT *" +
            "FROM employee e WHERE salary = (SELECT MAX(salary) FROM employee)",
            nativeQuery = true)
    List<EmployeeDTO> findEmployeesWithHighestSalary();

    List<EmployeeDTO> findEmployeesByPosition(Position position);

    EmployeeDTO findEmployeeById(Integer id);

}
