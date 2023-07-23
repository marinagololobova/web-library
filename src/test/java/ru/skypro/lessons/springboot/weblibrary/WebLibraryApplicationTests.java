package ru.skypro.lessons.springboot.weblibrary;

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
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;
import ru.skypro.lessons.springboot.weblibrary.entity.Position;
import ru.skypro.lessons.springboot.weblibrary.exceptions.IncorrectEmployeeIdException;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.weblibrary.repository.ReportRepository;
import ru.skypro.lessons.springboot.weblibrary.service.EmployeeMapper;
import ru.skypro.lessons.springboot.weblibrary.service.EmployeeService;
import ru.skypro.lessons.springboot.weblibrary.service.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@Data
@SpringBootTest
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
        lenient().when(jsonUtil.toJson(any())).thenReturn("From Mock");
    }

    @Test
    public void getAllEmployees_Test() {
        List<Employee> employees = Stream.iterate(1, id -> id + 1)
                .map(id -> generateEmployee(id, id + 100))
                .limit(5)
                .toList();

        List<EmployeeDTO> expected = new ArrayList<>();
        employees.stream()
                .map(this::employeeDTO)
                .forEach(expected::add);


        when(employeeServiceMock.getAllEmployees()).thenReturn(expected);

        verify(employeeRepository, never()).findAll();

    }

    @Test
    void addEmployee_Test() {
        EmployeeDTO result = employeeDTO(generateEmployee(1, null));
        when(EmployeeMapper.toEmployee(any())).thenAnswer(invocationOnMock -> {
            EmployeeDTO argument = invocationOnMock.getArgument(0, EmployeeDTO.class);
            Employee employee = new Employee();
            employee.setName(argument.getName());
            employee.setSalary(argument.getSalary());
            return employee;
        });

        employeeServiceMock.addEmployee(List.of(result));

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, only()).save(captor.capture());
        Employee actual = captor.getValue();

        org.assertj.core.api.Assertions.assertThat(actual).isNotNull();
        org.assertj.core.api.Assertions.assertThat(actual.getName()).isEqualTo(result.getName());
        org.assertj.core.api.Assertions.assertThat(actual.getSalary()).isEqualTo(result.getSalary());
    }

    @Test
    public void editEmployees_NegativeTest() {
        when(employeeRepository.findEmployeeById(any())).thenReturn(isNull());

        Assertions.assertThrows(IncorrectEmployeeIdException.class,
                () -> employeeServiceMock.findEmployeesWithHighestSalary());
    }


    @Test
    public void getEmployeeById_Test(){
        when(employeeRepository.findEmployeeById(employeeDTO.getId())).thenReturn(generateEmployee(1, null));

        EmployeeDTO actual = employeeServiceMock.getEmployeeById(employeeDTO.getId());

        org.assertj.core.api.Assertions.assertThat(actual).isEqualTo(generateEmployee(1, null));
        verify(employeeRepository, only()).findEmployeeById(employeeDTO.getId());
    }

    /*@Test
    public void deleteEmployeeById() {
        when(employeeRepository.deleteById()).thenReturn(generateEmployee(1, null));

        EmployeeDTO actual = employeeServiceMock.getEmployeeById(employeeDTO.getId());

        org.assertj.core.api.Assertions.assertThat(actual).isEqualTo(generateEmployee(1, null));
        verify(employeeRepository, only()).findEmployeeById(employeeDTO.getId());
    }*/

    @Test
    public void findEmployeesWithHighestSalary_Test() {
        Employee result = generateEmployee(1, null);
        when(employeeRepository.findEmployeesWithHighestSalary(any())).thenReturn(List.of(result));

        List<Employee> actual = employeeServiceMock.findEmployeesWithHighestSalary();
        org.assertj.core.api.Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(result);
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
