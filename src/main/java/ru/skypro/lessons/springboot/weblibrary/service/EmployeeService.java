package ru.skypro.lessons.springboot.weblibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    void addEmployee(List<EmployeeDTO> employees);
    void editEmployees(EmployeeDTO employeeDTO);
    EmployeeDTO getEmployeeById(Integer id);
    void deleteEmployeeById(Integer id);

    List<Employee> findEmployeesWithHighestSalary();
    List<Employee> findEmployeesByPosition(String position);
    Employee findEmployeeById(Integer id);
    List<EmployeeDTO> findAll(PageRequest pageRequest);

    void upload(MultipartFile employees);
    int createReport();
    String buildReport() throws JsonProcessingException;
    Resource downloadReport(int id);
}
