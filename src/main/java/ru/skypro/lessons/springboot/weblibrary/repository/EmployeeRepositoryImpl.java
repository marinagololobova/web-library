package ru.skypro.lessons.springboot.weblibrary.repository;

import org.springframework.stereotype.Repository;
import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final List<Employee> employeeList = List.of(
            new Employee("Катя", 90_000),
            new Employee("Дима", 102_000),
            new Employee("Олег", 80_000),
            new Employee("Вика", 165_000));
    @Override
    public List<Employee> getAllEmployees() {
        return employeeList;
    }

    @Override
    public double showSumSalary() {
        double sum = 0;
        for (Employee employee : employeeList) {
            sum += employee.getSalary();
        }
        return sum;
    }

    @Override
    public List<Employee> showEmployeeSalaryMin() {
        Double min = getAllEmployees().stream()
                .min(Comparator.comparing(Employee::getSalary))
                .map(Employee::getSalary).get();
        List<Employee> employeeWithMinSalary = getAllEmployees().stream()
                .filter(e -> e.getSalary() == min)
                .collect(Collectors.toList());
        return employeeWithMinSalary;
    }

    @Override
    public List<Employee> showEmployeeSalaryMax() {
        Double max = getAllEmployees().stream()
                .max(Comparator.comparing(Employee::getSalary))
                .map(Employee::getSalary)
                .get();
        List<Employee> employeeWithMaxSalary = getAllEmployees().stream()
                .filter(e -> e.getSalary() == max)
                .collect(Collectors.toList());
        return employeeWithMaxSalary;
    }

    @Override
    public List<Employee> showEmployeesSalaryAboveAverage() {
        double averageValue = getAllEmployees().stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .getAsDouble();
        List<Employee>employeesWithSalaryAboveAverage = getAllEmployees().stream()
                .filter(e -> averageValue - e.getSalary() <= 0)
                .collect(Collectors.toList());
        return employeesWithSalaryAboveAverage;
    }

}
