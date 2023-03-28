package io.todimu.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.todimu.springboottesting.model.Employee;
import io.todimu.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerITests {

    @Autowired private MockMvc mockMvc;

    @Autowired private EmployeeRepository employeeRepository;

    @Autowired private ObjectMapper objectMapper;

    private Employee savedEmployee;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();

        savedEmployee = Employee.builder()
                .firstName("Todimu")
                .lastName("Isewon")
                .email("toddy@gmail.com")
                .build();
    }

    @AfterEach
    public void tearDown(){
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("save employee")
    public void givenEmployeeObject_whenCreateEmployee_thenSavedEmployee() throws Exception {

        // given - precondition or setup

        // when - action or behaviour
        ResultActions response =  mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedEmployee)));

        // then - verify output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(savedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(savedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(savedEmployee.getEmail())));
    }

    @Test
    @DisplayName("get all employees")
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {

        // given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("Todimu").lastName("Isewon").email("todi@gmail.com").build());
        employeeList.add(Employee.builder().firstName("Lani").lastName("Isewon").email("lani@gmail.com").build());

        employeeRepository.saveAll(employeeList);

        // when - action or behaviour
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));
    }

    @Test
    @DisplayName("get employee by id positive scenario with valid id")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        // given - precondition or setup
        employeeRepository.save(savedEmployee);

        // when - action or behaviour
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", savedEmployee.getId()));

        // then - verify output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(savedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(savedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(savedEmployee.getEmail())));
    }

    @Test
    @DisplayName("get employee by id negative scenario with invalid id")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;
        employeeRepository.save(savedEmployee);

        // when - action or behaviour
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify output
        response.andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("update employee positive scenario")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {

        // given - precondition or setup

        Employee updatedEmployee = Employee.builder()
                .firstName("Lani")
                .lastName("Isewon")
                .email("todimu@gmail.com")
                .build();

        employeeRepository.save(savedEmployee);


        // when - action or behaviour
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @Test
    @DisplayName("update employee negative scenario")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnException() throws Exception {

        // given - precondition or setup
        long employeeId = 1L;

        Employee updatedEmployee = Employee.builder()
                .firstName("Lani")
                .lastName("Isewon")
                .email("todimu@gmail.com")
                .build();

        employeeRepository.save(savedEmployee);


        // when - action or behaviour
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("delete employee")
    public void givenEmployeeId_whenDeleteUser_thenReturn200() throws Exception {

        // given - precondition or setup
        employeeRepository.save(savedEmployee);

        // when - action or behaviour
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId() ));

        // then - verify output
        response.andDo(print())
                .andExpect(status().isOk());
    }

}
