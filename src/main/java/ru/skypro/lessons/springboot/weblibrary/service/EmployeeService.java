package ru.skypro.lessons.springboot.weblibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@AllArgsConstructor
@Service
public class EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;
    private final EmployeeMapper employeeMapper;
    private final ReportRepository reportRepository;
    private final JsonUtil jsonUtil;


    public List<EmployeeDTO> getAllEmployees() {
        LOG.debug("Was invoked method for getAllEmployees");
        List<EmployeeDTO> result = new ArrayList<>();
        employeeRepository.findAll().stream()
                .map(EmployeeMapper::fromEmployee)
                .forEach(result::add);
        return result;
    }


    public void addEmployees(List<EmployeeDTO> employees) {
        LOG.debug("Was invoked method for addEmployee with parameter: {}", jsonUtil.toJson(employees));
        employees.stream()
                .map(EmployeeMapper::toEmployee)
                .forEach(employeeRepository::save);
    }

    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        return EmployeeMapper.fromEmployee(employeeRepository.save(employee));
    }


    public void editEmployees(EmployeeDTO employeeDTO) throws IncorrectEmployeeIdException {
        LOG.debug("Was invoked method for editEmployees with parameter: {}", jsonUtil.toJson(employeeDTO));
        Employee employee = EmployeeMapper.toEmployee(employeeDTO);
        if (findEmployeeById(employee.getId()) == null) {
            throw new IncorrectEmployeeIdException(employee.getId());
        }
        try {
            employeeRepository.save(employee);
        } catch (IncorrectEmployeeIdException idException) {
            LOG.error(idException.getMessage(), idException);
            throw new IncorrectEmployeeIdException(employee.getId());
        }
    }


    public EmployeeDTO getEmployeeById(Integer id) {
        LOG.debug("Was invoked method for getEmployeeById with parameter: {}", jsonUtil.toJson(id));
        return employeeRepository.findById(id)
                .map(EmployeeMapper::fromEmployee)
                .orElseThrow(() -> new IncorrectEmployeeIdException(id));
    }


    public void deleteEmployeeById(Integer id) {
        LOG.debug("Was invoked method for deleteEmployeeById with parameter: {}", jsonUtil.toJson(id));
        employeeRepository.deleteById(id);
    }



    public List<Employee> findEmployeesWithHighestSalary() {
        LOG.debug("Was invoked method for findEmployeesWithHighestSalary");
        return employeeRepository.findEmployeesWithHighestSalary();
    }


    public List<Employee> findEmployeesByPosition(String position) {
        LOG.debug("Was invoked method for findEmployeesByPosition with parameter: {}", jsonUtil.toJson(position));
        return employeeRepository.findEmployeesByPositionNameLike(position);
    }


    public Employee findEmployeeById(Integer id) {
        LOG.debug("Was invoked method for findEmployeesById with parameter: {}", jsonUtil.toJson(id));
        return employeeRepository.findEmployeeById(id);
    }


    public List<EmployeeDTO> findAll(PageRequest pageRequest) {
        LOG.debug("Was invoked method for findAll with parameter: {}", jsonUtil.toJson(pageRequest));
        Page<Employee> page = employeeRepository.findAll(pageRequest);
        return page.getContent().stream()
                .map(EmployeeMapper::fromEmployee)
                .collect(Collectors.toList());
    }



    public void upload(MultipartFile employees) {
        LOG.debug("Was invoked method for upload with filName: {}", employees.getOriginalFilename());
        try {
            String extension = StringUtils.getFilenameExtension(employees.getOriginalFilename());
            if (!"json".equals(extension)) {
                throw new IllegalJsonFileException();
            }
            List<EmployeeDTO> employeeDTOS = objectMapper.readValue(employees.getBytes(), new TypeReference<>() {
                    }
            );
            addEmployees(employeeDTOS);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new IllegalJsonFileException();
        }
    }


    public int createReport() {
        LOG.debug("Was invoked method for createReport");
        try {
            Report report = new Report();
            report.setReport(buildReport());
            return reportRepository.save(report).getId();
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
            throw new IllegalJsonFileException();
        }
    }


    public String buildReport() throws JsonProcessingException {
        LOG.debug("Was invoked method for buildReport");
        List<ReportDTO> reportDTOList = employeeRepository.createReports();
        return objectMapper.writeValueAsString(reportDTOList);
    }


    public Resource downloadReport(int id) {
        LOG.debug("Was invoked method for downloadReport with parameter: {}", jsonUtil.toJson(id));
        return new ByteArrayResource(reportRepository
                .findById(id)
                .orElseThrow(ReportNotFoundException::new)
                .getReport()
                .getBytes(StandardCharsets.UTF_8));
    }
}
