package itkhamar.com.controller;

import itkhamar.com.dto.EmployeeRequest;
import itkhamar.com.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/user/employment/me")
    public Mono<ResponseEntity<?>> getMyEmployment() {
        return employeeService.findLoggedEmployee();
    }

    @GetMapping("/hr/employees")
    public Mono<ResponseEntity<?>> findAllEmployees() {
        return employeeService.findAllEmployee();
    }

    @PostMapping("/admin/employees/hr")
    public Mono<ResponseEntity<?>> createHR(@Valid @RequestBody EmployeeRequest employeeRequest) {
        return employeeService.createEmployee(employeeRequest, true);
    }
}
