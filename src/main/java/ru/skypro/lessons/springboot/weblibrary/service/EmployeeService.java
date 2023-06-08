package ru.skypro.lessons.springboot.weblibrary.service;

import org.springframework.data.domain.PageRequest;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;
import ru.skypro.lessons.springboot.weblibrary.entity.Position;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    void addEmployee(Employee employee);
    void editEmployees(Employee employee);
    EmployeeDTO getEmployeeById(Integer id);
    void deleteEmployeeById(Integer id);

    List<EmployeeDTO> findEmployeesWithHighestSalary();
    List<EmployeeDTO> findEmployeesByPosition(Position position);
    EmployeeDTO findEmployeeById(Integer id);
    List<Employee> findAll(PageRequest pageRequest);



}
