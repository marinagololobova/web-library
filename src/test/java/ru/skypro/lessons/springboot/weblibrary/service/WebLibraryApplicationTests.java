package ru.skypro.lessons.springboot.weblibrary.service;

import com.github.javafaker.Faker;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;
import ru.skypro.lessons.springboot.weblibrary.entity.Position;
import ru.skypro.lessons.springboot.weblibrary.exceptions.IncorrectEmployeeIdException;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.weblibrary.repository.ReportRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@Data
@ExtendWith(MockitoExtension.class)
class WebLibraryApplicationTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private JsonUtil jsonUtil;
    @Mock
    private EmployeeDTO employeeDTO;


    @InjectMocks
    private EmployeeService employeeServiceMock;


    private final Faker faker = new Faker();


    @BeforeEach
    public void beforeEach() {
        EmployeeService employeeServiceMock;
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void getAllEmployees_Test() {
        List<Employee> employees = Stream.iterate(1, id -> id + 1)
                .map(id -> generateEmployee(id, id + 100))
                .limit(5)
                .toList();

        when(employeeRepository.findAll()).thenReturn(employees);
        employeeServiceMock.getAllEmployees();

    }

    @Test
    void addEmployee_Test() {
        EmployeeDTO result = employeeDTO(generateEmployee(1, null));

        employeeServiceMock.addEmployees(List.of(result));

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, only()).save(captor.capture());
        Employee actual = captor.getValue();

        org.assertj.core.api.Assertions.assertThat(actual).isNotNull();
        org.assertj.core.api.Assertions.assertThat(actual.getName()).isEqualTo(result.getName());
        org.assertj.core.api.Assertions.assertThat(actual.getSalary()).isEqualTo(result.getSalary());
    }

    @Test
    public void editEmployees_Test() {
        EmployeeDTO result = employeeDTO(generateEmployee(1, null));
        Employee expected = EmployeeMapper.toEmployee(result);

        when(employeeRepository.findEmployeeById(any())).thenReturn(null);

        Assertions.assertThrows(IncorrectEmployeeIdException.class,
                () -> employeeServiceMock.editEmployees(result));

        when(employeeRepository.findEmployeeById(any())).thenReturn(expected);

        employeeServiceMock.editEmployees(result);
    }


    @Test
    public void getEmployeeById_Test(){
        employeeServiceMock.findEmployeeById(any());
        verify(employeeRepository, only()).findEmployeeById(any());
    }

    @Test
    public void deleteEmployeeById_Test() {
        employeeServiceMock.deleteEmployeeById(any());
        verify(employeeRepository, only()).deleteById(any());
    }

    @Test
    public void findEmployeesWithHighestSalary_Test() {
        employeeServiceMock.findEmployeesWithHighestSalary();
        verify(employeeRepository, only()).findEmployeesWithHighestSalary();
    }

    @Test
    public void findEmployeesByPosition_Test(){
        employeeServiceMock.findEmployeesByPosition(any());
        verify(employeeRepository, only()).findEmployeesByPositionNameLike(any());
    }

    @Test
    public void findAll_Test() {
        List<Employee> employees = Stream.iterate(1, id -> id + 1)
                .map(id -> generateEmployee(id, id + 100))
                .limit(5)
                .toList();

        List<EmployeeDTO> expected = employees.stream()
                .map(this::employeeDTO)
                .collect(Collectors.toList());

        PageRequest page = PageRequest.of(1, 5);

        when(employeeRepository.findAll(page)).thenReturn(new PageImpl<>(employees));

        List<EmployeeDTO> allEmployees = employeeServiceMock.findAll(page);
        org.assertj.core.api.Assertions.assertThat(allEmployees).isEqualTo(expected);
    }



    private Employee generateEmployee(int id, Integer positionId) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setSalary(faker.random().nextInt(100_000, 300_000));
        employee.setName(faker.name().fullName());
        employee.setPosition(positionId != null ? genenatePosition(positionId) : null);
        return employee;
    }

    private Position genenatePosition(int id) {
        Position position = new Position();
        position.setId(id);
        position.setName(faker.company().profession());
        return position;
    }

    private EmployeeDTO employeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(employee.getName());
        employeeDTO.setSalary(employee.getSalary());
        employeeDTO.setPosition(
                Optional.ofNullable(employee.getPosition())
                        .map(Position::getName)
                        .orElse(null)
        );
        return employeeDTO;
    }
}
