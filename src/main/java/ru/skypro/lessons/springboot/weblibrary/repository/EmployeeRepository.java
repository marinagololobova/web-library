package ru.skypro.lessons.springboot.weblibrary.repository;
import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;

import java.util.List;

public interface EmployeeRepository {
    public List<Employee> getAllEmployees();

    public double showSumSalary();
    public List<Employee> showEmployeeSalaryMin();
    public List<Employee> showEmployeeSalaryMax();
    public List<Employee> showEmployeesSalaryAboveAverage();

    public void addEmployees(Employee employee);
    public void editEmployees(Employee employee);
    public Employee getEmployeeById(Integer id);
    public void deleteEmployeeById(Integer id);
    public List<Employee> getEmployeesWithSalaryHigherThan(double compareSalary);
}
