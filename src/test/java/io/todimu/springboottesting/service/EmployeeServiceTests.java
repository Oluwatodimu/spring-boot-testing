package io.todimu.springboottesting.service;

import io.todimu.springboottesting.exception.ResourceNotFoundException;
import io.todimu.springboottesting.model.Employee;
import io.todimu.springboottesting.repository.EmployeeRepository;
import io.todimu.springboottesting.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock private EmployeeRepository employeeRepository;

    @InjectMocks private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Todimu")
                .lastName("Isewon")
                .email("todimu@gmail")
                .build();
    }

    @Test
    @DisplayName("save employee without any error")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or behaviour
         Employee savedEmployee = employeeService. saveEmployee(employee);

        // then - verify output
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("save employee which throws an exception")
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // when - action or behaviour
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService. saveEmployee(employee);
        });

        // then - verify output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("get all saved employees")
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnsEmployeesList() {

        // given - precondition or setup
        Employee employeeOne = employee = Employee.builder()
                .id(2L)
                .firstName("Lani")
                .lastName("Isewon")
                .email("lani@gmail")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employeeOne));

        // when - action or behaviour
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("get all saved employees empty list")
    public void givenEmptyListOfEmployees_whenGetAllEmployees_thenReturnsEmptyEmployeesList() {

        // given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or behaviour
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify output
        assertThat(employeeList).isEmpty();
    }

    @Test
    @DisplayName("get employee optional by Id")
    public void givenEmployeeId_whenFindById_thenReturnEmployeeOptional() {

        // given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when - action or behaviour
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        // then - verify output
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("update employee data")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);

        employee.setEmail("kaido@yahoo.com");
        employee.setFirstName("Victor");
        employee.setLastName("Krum");

        // when - action or behaviour
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Victor");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Krum");
        assertThat(updatedEmployee.getEmail()).isEqualTo("kaido@yahoo.com");
    }

    @Test
    @DisplayName("delete employee by id")
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {

        long employeeId = 1L;

        // given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or behaviour
        employeeService.deleteEmployee(employeeId);

        // then - verify output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
