package io.todimu.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.todimu.springboottesting.model.Employee;
import io.todimu.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private EmployeeService employeeService;

    private Employee savedEmployee;

    @BeforeEach
    public void setup() {
        savedEmployee = Employee.builder()
                .firstName("Todimu")
                .lastName("Isewon")
                .email("todi@gmail.com")
                .build();
    }

    @Test
    @DisplayName("save employee")
    public void givenEmployeeObject_whenCreateEmployee_thenSavedEmployee() throws Exception {

        // given - precondition or setup

        // stubbed method call
        // will return whatever argument is passed to the employeeService.saveEmployee() method
        // way of functional programming I will do going forward
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

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

        given(employeeService.getAllEmployees()).willReturn(employeeList);

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
        long employeeId = 1L;



        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));

        // when - action or behaviour
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

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

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

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
        long employeeId = 1L;

        Employee updatedEmployee = Employee.builder()
                .firstName("Lani")
                .lastName("Isewon")
                .email("todimu@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
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

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

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
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when - action or behaviour
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        // then - verify output
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
