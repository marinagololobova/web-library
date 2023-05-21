package ru.skypro.lessons.springboot.weblibrary.service;

import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();

    double showSumSalary();
    List<Employee> showEmployeeSalaryMin();
    List<Employee> showEmployeeSalaryMax();
    List<Employee> showEmployeesSalaryAboveAverage();
    void addEmployees(Employee employee);
    void editEmployees(Employee employee);
    Employee getEmployeeById(Integer id);
    void deleteEmployeeById(Integer id);
    List<Employee> getEmployeesWithSalaryHigherThan(double compareSalary);

}
