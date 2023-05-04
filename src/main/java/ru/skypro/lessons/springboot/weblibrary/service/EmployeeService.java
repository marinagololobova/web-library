package ru.skypro.lessons.springboot.weblibrary.service;

import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();

    double showSumSalary();
    List<Employee> showEmployeeSalaryMin();
    List<Employee> showEmployeeSalaryMax();
    List<Employee> showEmployeesSalaryAboveAverage();

}
