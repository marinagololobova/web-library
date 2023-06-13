package ru.skypro.lessons.springboot.weblibrary.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;
import ru.skypro.lessons.springboot.weblibrary.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/all")
    public List<EmployeeDTO> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public void addEmployees(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.addEmployee(employeeDTO);
    }

    @PutMapping("/{id}")
    public void editEmployees(@RequestBody EmployeeDTO employeeDTO){
        employeeService.editEmployees(employeeDTO);
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable Integer id) {
        employeeService.deleteEmployeeById(id);
    }




    @GetMapping("/withHighestSalary")
    public List<Employee> showEmployeesSalaryMax() {
        return employeeService.findEmployeesWithHighestSalary();
    }

    @GetMapping()
    public List<Employee> findEmployeesByPosition(@RequestParam(required = false, defaultValue = "разработчик") String position) {
        return employeeService.findEmployeesByPosition(position);
    }

    @GetMapping("/{id}/fullInfo")
    public Employee findEmployeeById(@PathVariable Integer id){
        return employeeService.findEmployeeById(id);
    }

    @GetMapping("/page")
    public List <EmployeeDTO> getEmployeeWithPaging(@RequestParam(required = false, defaultValue = "0") int page,
                                                    @RequestParam(required = false, defaultValue = "10")int size) {
        return employeeService.findAll(PageRequest.of(page, size));
    }

}
