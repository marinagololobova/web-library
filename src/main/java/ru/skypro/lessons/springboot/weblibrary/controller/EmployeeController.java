package ru.skypro.lessons.springboot.weblibrary.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;
import ru.skypro.lessons.springboot.weblibrary.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/salary/sum")
    public double showSumSalary() {
        return employeeService.showSumSalary();
    }

    @GetMapping("/salary/min")
    public List<Employee> showEmployeeSalaryMin() {
        return employeeService.showEmployeeSalaryMin();
    }

    @GetMapping("/salary/max")
    public List<Employee> showEmployeeSalaryMax() {
        return employeeService.showEmployeeSalaryMax();
    }

    @GetMapping("/salary/high-salary")
    public List<Employee> showEmployeesSalaryAboveAverage() {
        return employeeService.showEmployeesSalaryAboveAverage();
    }

    @PostMapping
    public void addEmployees(@RequestBody Employee employee) {
        employeeService.addEmployees(employee);
    }

    @PutMapping("/{id}")
    public void editEmployees(@RequestBody Employee employee){
        employeeService.editEmployees(employee);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable Integer id) {
        employeeService.deleteEmployeeById(id);
    }

    @GetMapping("/salaryHigherThan")
    public List<Employee> getEmployeesWithSalaryHigherThan(@RequestParam("compareSalary") double compareSalary ) {
        return employeeService.getEmployeesWithSalaryHigherThan(compareSalary);
    }
}
