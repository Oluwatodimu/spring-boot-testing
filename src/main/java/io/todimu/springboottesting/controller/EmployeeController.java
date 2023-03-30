package io.todimu.springboottesting.controller;

import io.todimu.springboottesting.model.Employee;
import io.todimu.springboottesting.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        return  employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(name = "id") long employeeId) {
        return employeeService.getEmployeeById(employeeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") long employeeId,
                                                   @RequestBody Employee employee) {

        return employeeService.getEmployeeById(employeeId)
                .map(savedEmployee -> {
                   savedEmployee.setFirstName(employee.getFirstName());
                   savedEmployee.setLastName(employee.getLastName());
                   savedEmployee.setEmail(employee.getEmail());

                   Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
                   return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(value = "id") long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
    }

//    @RunWith(MockitoJUnitRunner.class)
//    public class MyControllerTest {
//
//        @Mock
//        private MyService myService;
//
//        @Test
//        public void testGetMessage() {
//            // Create a mock supplier and stub the get() method to return "Hello, World!"
//            Supplier<String> mockSupplier = Mockito.mock(Supplier.class);
//            Mockito.when(mockSupplier.get()).thenReturn("Hello, World!");
//
//            // Create an instance of the controller and inject the mock supplier and service
//            MyController controller = new MyController(mockSupplier, myService);
//
//            // Call the getMessage() method on the controller and verify the result
//            String result = controller.getMessage();
//            Assert.assertEquals("Hello, World!", result);
//        }
//    }

}
