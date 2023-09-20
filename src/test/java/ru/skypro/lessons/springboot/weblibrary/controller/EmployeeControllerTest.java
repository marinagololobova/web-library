package ru.skypro.lessons.springboot.weblibrary.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.lessons.springboot.weblibrary.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.Employee;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeeRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testUser", roles = {"USER", "ADMIN"})
public class EmployeeControllerTest {

    private static String EMPLOYEES_URL = "/employees";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @AfterEach
    void cleanData() {
        employeeRepository.deleteAll();
    }


    @Test
    public void addEmployeesTest() throws Exception{
        List<Employee> employees = Stream.iterate(1, id -> id + 1)
                .map(id -> generateEmployee(id, id + 100))
                .limit(5)
                .toList();

        List<EmployeeDTO> employeeDTOS = employees.stream()
                .map(this::employeeDTO)
                .collect(Collectors.toList());


        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTOS)))
                .andExpect(result -> {
                            assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        List <EmployeeDTO> actual = getEmployeesTest();
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrderElementsOf(employeeDTOS);
    });

    }

    public List<EmployeeDTO> getEmployeesTest() throws Exception{
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/all")
                .accept(APPLICATION_JSON))
                .andExpect(result -> {
                    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                    employeeDTOS.addAll(
                            objectMapper.readValue(
                                    result.getResponse().getContentAsString(),
                                    new TypeReference<>() {
                                    }
                            )
                    );
                });
        return employeeDTOS;
    }

    @Test
    public void editEmployeesTest() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "test_name");

        String createdUserString = mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("test_name"))
                .andReturn().getResponse().getContentAsString();
        JSONObject createdUser = new JSONObject(Integer.parseInt(createdUserString));

        Long id = (Long) createdUser.get("id");

        createdUser.put("name", "test_user2");
        mockMvc.perform(put("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createdUser.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("test_user2"));

        mockMvc.perform(get(EMPLOYEES_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("test_user2"));
    }

    @Test
    public void deleteEmployeeByIdTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "test_name");

        String createdUserString = mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("test_name"))
                .andReturn().getResponse().getContentAsString();
        JSONObject createdUser = new JSONObject(Integer.parseInt(createdUserString));
        Long id = (Long) createdUser.get("id");

        mockMvc.perform(delete("/employees/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(EMPLOYEES_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }


    private Employee generateEmployee(int id, Integer positionId) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setSalary(faker.random().nextInt(100_000, 300_000));
        employee.setName(faker.name().fullName());
        return employee;
    }

    private EmployeeDTO employeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName().substring(0, Math.min(16, employee.getName().length())));
        employeeDTO.setSalary(employee.getSalary());
        return employeeDTO;
    }
}
