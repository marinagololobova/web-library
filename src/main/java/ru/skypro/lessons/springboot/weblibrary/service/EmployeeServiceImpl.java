package ru.skypro.lessons.springboot.weblibrary.service;

import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeeRepository;

import java.util.List;
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    @Override
    public double showSumSalary() {
        return employeeRepository.showSumSalary();
    }

    @Override
    public List<Employee> showEmployeeSalaryMin() {
        return employeeRepository.showEmployeeSalaryMin();
    }

    @Override
    public List<Employee> showEmployeeSalaryMax() {
        return employeeRepository.showEmployeeSalaryMax();
    }

    @Override
    public List<Employee> showEmployeesSalaryAboveAverage() {
        return employeeRepository.showEmployeesSalaryAboveAverage();
    }
}
