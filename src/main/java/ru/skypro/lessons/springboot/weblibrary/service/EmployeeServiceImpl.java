package ru.skypro.lessons.springboot.weblibrary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;
import ru.skypro.lessons.springboot.weblibrary.entity.Position;
import ru.skypro.lessons.springboot.weblibrary.exceptions.IncorrectEmployeeIdException;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> result = new ArrayList<>();
        employeeRepository.findAll()
                .forEach(result::add);
        return result.stream()
                .map(EmployeeDTO::fromEmployee)
                .toList();
    }

    @Override
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void editEmployees(Employee employee) {
        employeeRepository.save(employee);
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
    public List<EmployeeDTO> findEmployeesWithHighestSalary() {
        return employeeRepository.findEmployeesWithHighestSalary();
    }

    @Override
    public List<EmployeeDTO> findEmployeesByPosition(Position position) {
        return employeeRepository.findEmployeesByPosition(position);
    }

    @Override
    public EmployeeDTO findEmployeeById(Integer id) {
        return employeeRepository.findEmployeeById(id);
    }

    @Override
    public List<Employee> findAll(PageRequest pageRequest) {
        Page<Employee> page = employeeRepository.findAll(pageRequest);
        return page.getContent();
    }


}
