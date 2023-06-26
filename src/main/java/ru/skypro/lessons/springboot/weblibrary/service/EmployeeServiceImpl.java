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
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;
    private final ReportRepository reportRepository;
    private final JsonUtil jsonUtil;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        LOG.debug("Was invoked method for getAllEmployees");
        List<EmployeeDTO> result = new ArrayList<>();
        employeeRepository.findAll().stream().map(EmployeeDTO::fromEmployee)
                .forEach(result::add);
        return result;
    }

    @Override
    public void addEmployee(List<EmployeeDTO> employees) {
        LOG.debug("Was invoked method for addEmployee with parameter: {}", jsonUtil.toJson(employees));
        employees.stream().map(EmployeeDTO::toEmployee).forEach(employeeRepository::save);
    }

    @Override
    public void editEmployees(EmployeeDTO employeeDTO) throws IncorrectEmployeeIdException{
        LOG.debug("Was invoked method for editEmployees with parameter: {}", jsonUtil.toJson(employeeDTO));
        Employee employee = employeeDTO.toEmployee();
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

    @Override
    public EmployeeDTO getEmployeeById(Integer id) {
        LOG.debug("Was invoked method for getEmployeeById with parameter: {}", jsonUtil.toJson(id));
        return employeeRepository.findById(id)
                .map(EmployeeDTO::fromEmployee)
                .orElseThrow(() -> new IncorrectEmployeeIdException(id));
    }

    @Override
    public void deleteEmployeeById(Integer id) {
        LOG.debug("Was invoked method for deleteEmployeeById with parameter: {}", jsonUtil.toJson(id));
        employeeRepository.deleteById(id);
    }





    @Override
    public List<Employee> findEmployeesWithHighestSalary() {
        LOG.debug("Was invoked method for findEmployeesWithHighestSalary");
        return employeeRepository.findEmployeesWithHighestSalary();
    }

    @Override
    public List<Employee> findEmployeesByPosition(String position) {
        LOG.debug("Was invoked method for findEmployeesByPosition with parameter: {}", jsonUtil.toJson(position));
        return employeeRepository.findEmployeesByPositionNameLike(position);
    }

    @Override
    public Employee findEmployeeById(Integer id) {
        LOG.debug("Was invoked method for findEmployeesById with parameter: {}", jsonUtil.toJson(id));
        return employeeRepository.findEmployeeById(id);
    }

    @Override
    public List<EmployeeDTO> findAll(PageRequest pageRequest) {
        LOG.debug("Was invoked method for findAll with parameter: {}", jsonUtil.toJson(pageRequest));
        Page<Employee> page = employeeRepository.findAll(pageRequest);
        return page.getContent().stream()
                .map(EmployeeDTO::fromEmployee)
                .collect(Collectors.toList());
    }




    @Override
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
            addEmployee(employeeDTOS);
        }catch (IOException e){
            LOG.error(e.getMessage(), e);
            throw new IllegalJsonFileException();
        }
    }

    @Override
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

    @Override
    public String buildReport() throws JsonProcessingException {
        LOG.debug("Was invoked method for buildReport");
        List<ReportDTO> reportDTOList = employeeRepository.createReports();
        return objectMapper.writeValueAsString(reportDTOList);
    }

    @Override
    public Resource downloadReport(int id) {
        LOG.debug("Was invoked method for downloadReport with parameter: {}", jsonUtil.toJson(id));
        return new ByteArrayResource(reportRepository
                .findById(id)
                .orElseThrow(ReportNotFoundException::new)
                .getReport()
                .getBytes(StandardCharsets.UTF_8));
    }
}
