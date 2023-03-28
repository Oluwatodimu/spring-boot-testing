package io.todimu.springboottesting.repository;

import io.todimu.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Todimu")
                .lastName("Isewon")
                .email("todimu@gmail.com")
                .build();
    }

    @Test
    @DisplayName("ensure an employee is saved")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployeeObject() {
        //given - precondition or setup

        //when - action or behaviour
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("get all employees")
    public void givenEmployeeList_whenFindAll_thenEmployeesList() {
        // given precondition or setup
        Employee employeeOne = Employee.builder()
                .firstName("Todimu")
                .lastName("Isewon")
                .email("todimu@gmail.com")
                .build();

        Employee employeeTwo = Employee.builder()
                .firstName("Lani")
                .lastName("Isewon")
                .email("lani@gmail.com")
                .build();

        employeeRepository.save(employeeOne);
        employeeRepository.save(employeeTwo);

        // when action or behaviour
        List<Employee> employeeList = employeeRepository.findAll();

        // then verify output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("get employee by Id")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given - precondition or setup

        employeeRepository.save(employee);

        // when - action or behaviour
        Employee employeeDao = employeeRepository.findById(employee.getId()).get();

        // then - verify output
        assertThat(employeeDao).isNotNull();
    }

    @Test
    @DisplayName("get employee by email")
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {
        // given - precondition or setup

        employeeRepository.save(employee);

        // when - action or behaviour
        Employee employeeDao = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify output
        assertThat(employeeDao).isNotNull();
    }

    @Test
    @DisplayName("test name")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup

        employeeRepository.save(employee);

        // when - action or behaviour
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("Oluwatodimu");
        savedEmployee.setEmail("todimu23@gmail.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify output
        assertThat(updatedEmployee.getEmail()).isEqualTo("todimu23@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Oluwatodimu");
    }

    @Test
    @DisplayName("delete employee")
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        // given - precondition or setup
         employeeRepository.save(employee);

        // when - action or behaviour
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then - verify output
        assertThat(employeeOptional).isEmpty();
    }

    @Test
    @DisplayName("get employee using custom JPQL with index params")
    public void givenFirstAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {

        // given - precondition or setup

        employeeRepository.save(employee);
        String firstName = "Todimu";
        String lastName = "Isewon";

        // when - action or behaviour
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

        // then - verify output
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("get employee using custom JPQL with named params")
    public void givenFirstAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {

        // given - precondition or setup

        employeeRepository.save(employee);
        String firstName = "Todimu";
        String lastName = "Isewon";

        // when - action or behaviour
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then - verify output
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("get employee using custom Native SQL with index params")
    public void givenFirstAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {

        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or behaviour
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // then - verify output
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("get employee using custom Native SQL with named params")
    public void givenFirstAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {

        // given - precondition or setup

        employeeRepository.save(employee);

        // when - action or behaviour
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());

        // then - verify output
        assertThat(savedEmployee).isNotNull();
    }
}
