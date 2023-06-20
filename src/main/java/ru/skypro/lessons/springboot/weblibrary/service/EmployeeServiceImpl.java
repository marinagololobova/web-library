package ru.skypro.lessons.springboot.weblibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.dto.ReportDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;
import ru.skypro.lessons.springboot.weblibrary.entity.Report;
import ru.skypro.lessons.springboot.weblibrary.exceptions.IllegalJsonFileException;
import ru.skypro.lessons.springboot.weblibrary.exceptions.IncorrectEmployeeIdException;
import ru.skypro.lessons.springboot.weblibrary.exceptions.ReportNotFoundException;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.weblibrary.repository.ReportRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;
    private final ReportRepository reportRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ObjectMapper objectMapper,
                               ReportRepository reportRepository) {
        this.employeeRepository = employeeRepository;
        this.objectMapper = objectMapper;
        this.reportRepository = reportRepository;
    }


    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> result = new ArrayList<>();
        employeeRepository.findAll().stream().map(EmployeeDTO::fromEmployee)
                .forEach(result::add);
        return result;
    }

    @Override
    public void addEmployee(List<EmployeeDTO> employees) {
        employees.stream().map(EmployeeDTO::toEmployee).forEach(employeeRepository::save);
    }

    @Override
    public void editEmployees(EmployeeDTO employeeDTO) throws IncorrectEmployeeIdException{
        Employee employee = employeeDTO.toEmployee();
        if (findEmployeeById(employee.getId()) == null) {
            throw new IncorrectEmployeeIdException(employee.getId());
        }
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
        return employeeRepository.findEmployeesByPositionNameLike(position);
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




    @Override
    public void upload(MultipartFile employees) {
        try {
            String extension = StringUtils.getFilenameExtension(employees.getOriginalFilename());
            if (!"json".equals(extension)) {
                throw new IllegalJsonFileException();
            }
            List<EmployeeDTO> employeeDTOS = objectMapper.readValue(employees.getBytes(), new TypeReference<>() {
            }
            );
            addEmployee(employeeDTOS);
        }catch (IOException e){
            throw new IllegalJsonFileException();
        }
    }

    @Override
    public int createReport() {
        try {
            Report report = new Report();
            report.setReport(buildReport());
            return reportRepository.save(report).getId();
        } catch (JsonProcessingException e) {
            throw new IllegalJsonFileException();
        }
    }

    @Override
    public String buildReport() throws JsonProcessingException {
        List<ReportDTO> reportDTOList = employeeRepository.createReports();
        return objectMapper.writeValueAsString(reportDTOList);
    }

    @Override
    public Resource downloadReport(int id) {
        return new ByteArrayResource(reportRepository
                .findById(id)
                .orElseThrow(ReportNotFoundException::new)
                .getReport()
                .getBytes(StandardCharsets.UTF_8));
    }
}
