package ru.skypro.lessons.springboot.weblibrary.repository;

import org.springframework.stereotype.Repository;
import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final List<Employee> employeeList;

    {
        employeeList = new ArrayList<>();
        employeeList.add(new Employee(1, "Катя", 90000));
        employeeList.add(new Employee(2, "Дима", 102000));
        employeeList.add(new Employee(3, "Олег", 80000));
        employeeList.add(new Employee(4, "Вика", 165000));
    }
    @Override
    public List<Employee> getAllEmployees() {
        return employeeList;
    }

    @Override
    public double showSumSalary() {
        return employeeList.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
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

    @Override
    public void addEmployees(Employee employee) {
        employeeList.add(employee);

    }

    @Override
    public void editEmployees(Employee employee) {
        Employee employeeToBeUpdated = getEmployeeById(employee.getId());
        employeeToBeUpdated.setName(employee.getName());
        employeeToBeUpdated.setSalary(employee.getSalary());
    }

    @Override
    public Employee getEmployeeById(Integer id) {
        return employeeList.stream()
                .filter(employee -> employee.getId() == id)
                .findAny()
                .orElse(null);
    }

    @Override
    public void deleteEmployeeById(Integer id) {
        employeeList.removeIf(employee -> employee.getId() == id);
    }

    @Override
    public List<Employee> getEmployeesWithSalaryHigherThan(double compareSalary) {
        List<Employee> employeesWithSalaryHigherThanPassedParameter = getAllEmployees().stream()
                .filter(e -> compareSalary - e.getSalary() <= 0)
                .collect(Collectors.toList());
        return employeesWithSalaryHigherThanPassedParameter;
    }
}
