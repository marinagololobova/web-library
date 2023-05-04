package ru.skypro.lessons.springboot.weblibrary.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;
import ru.skypro.lessons.springboot.weblibrary.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employee/salary")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/sum")
    public double showSumSalary() {
        return employeeService.showSumSalary();
    }

    @GetMapping("/min")
    public List<Employee> showEmployeeSalaryMin() {
        return employeeService.showEmployeeSalaryMin();
    }

    @GetMapping("/max")
    public List<Employee> showEmployeeSalaryMax() {
        return employeeService.showEmployeeSalaryMax();
    }

    @GetMapping("/high-salary")
    public List<Employee> showEmployeesSalaryAboveAverage() {
        return employeeService.showEmployeesSalaryAboveAverage();
    }
}
