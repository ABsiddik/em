package itkhamar.com.controller;

import itkhamar.com.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/user/employment", "/hr/employees"})
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/me")
    public Mono<ResponseEntity<?>> getMyEmployment() {
        return employeeService.findLoggedEmployee();
    }
    
}
