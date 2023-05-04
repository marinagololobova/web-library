package ru.skypro.lessons.springboot.weblibrary.repository;
import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;

import java.util.List;

public interface EmployeeRepository {
    public List<Employee> getAllEmployees();

    public double showSumSalary();
    public List<Employee> showEmployeeSalaryMin();
    public List<Employee> showEmployeeSalaryMax();
    public List<Employee> showEmployeesSalaryAboveAverage();
}
