package ru.skypro.lessons.springboot.weblibrary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;
import ru.skypro.lessons.springboot.weblibrary.exceptions.IncorrectEmployeeIdException;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> result = new ArrayList<>();
        employeeRepository.findAll().stream().map(EmployeeDTO::fromEmployee)
                .forEach(result::add);
        return result;
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeDTO.toEmployee();
        employeeRepository.save(employee);
    }

    @Override
    public void editEmployees(EmployeeDTO employeeDTO) {
        Employee employee = employeeDTO.toEmployee();
        try {
            employeeRepository.save(employee);
        } catch (IncorrectEmployeeIdException idException) {
            throw new IncorrectEmployeeIdException(employee.getId());
        }
    }

    @Override
    public EmployeeDTO getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .map(EmployeeDTO::fromEmployee)
                .orElseThrow(() -> new IncorrectEmployeeIdException(id));
    }

    @Override
    public void deleteEmployeeById(Integer id) {
        employeeRepository.deleteById(id);
    }





    @Override
    public List<Employee> findEmployeesWithHighestSalary() {
        return employeeRepository.findEmployeesWithHighestSalary();
    }

    @Override
    public List<Employee> findEmployeesByPosition(String position) {
        return employeeRepository.findEmployeesByPosition(position);
    }

    @Override
    public Employee findEmployeeById(Integer id) {
        return employeeRepository.findEmployeeById(id);
    }

    @Override
    public List<EmployeeDTO> findAll(PageRequest pageRequest) {
        Page<Employee> page = employeeRepository.findAll(pageRequest);
        return page.getContent().stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }
}
