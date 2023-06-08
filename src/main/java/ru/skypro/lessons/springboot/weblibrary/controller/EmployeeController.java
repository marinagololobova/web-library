package ru.skypro.lessons.springboot.weblibrary.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;
import ru.skypro.lessons.springboot.weblibrary.entity.Position;
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
    public void addEmployees(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
    }

    @PutMapping("/{id}")
    public void editEmployees(@RequestBody Employee employee){
        employeeService.editEmployees(employee);
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
    public List<EmployeeDTO> showEmployeesSalaryMax() {
        return employeeService.findEmployeesWithHighestSalary();
    }

    @GetMapping()
    public List<EmployeeDTO> findEmployeesByPosition(@RequestParam(required = false, defaultValue = "0") Position position) {
        return employeeService.findEmployeesByPosition(position);
    }

    @GetMapping("/{id}/fullInfo")
    public EmployeeDTO findEmployeeById(@PathVariable Integer id){
        return employeeService.findEmployeeById(id);
    }

    @GetMapping("/page")
    public List <Employee> getEmployeeWithPaging(@RequestParam(required = false, defaultValue = "0") int page,
                                                    @RequestParam(required = false, defaultValue = "10")int size) {
        return employeeService.findAll(PageRequest.of(page, size));
    }

}
