package itkhamar.com.service;

import itkhamar.com.dto.BaseResponse;
import itkhamar.com.dto.EmployeeRequest;
import itkhamar.com.dto.EmployeeResponse;
import itkhamar.com.enums.EmployeeStatus;
import itkhamar.com.enums.EmployeeType;
import itkhamar.com.enums.RoleEnum;
import itkhamar.com.exception.BadRequestException;
import itkhamar.com.model.Employee;
import itkhamar.com.model.User;
import itkhamar.com.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    public Mono<ResponseEntity<?>> findLoggedEmployee() {
        Mono<User> user = userService.findLoggedUser();
        return user
                .flatMap(u -> employeeRepository.findById(u.getEmployeeId()))
                .map(employee -> BaseResponse.success(new EmployeeResponse(employee.getName(), employee.getPhone(),
                                    employee.getDepartment(), employee.getDesignation(), employee.getJoiningDate(), employee.getStatus(), employee.getType())));
    }

    public Mono<ResponseEntity<?>> findAllEmployee() {
        Flux<Employee> allEmployee = employeeRepository.findAll();
        return allEmployee
                .map(employee -> new EmployeeResponse(employee.getName(), employee.getPhone(),
                        employee.getDepartment(), employee.getDesignation(), employee.getJoiningDate(), employee.getStatus(), employee.getType()))
                .collectList()
                .map(BaseResponse::success);
    }

    public Mono<ResponseEntity<?>> createEmployee(EmployeeRequest employeeRequest, boolean isHR) {
        return userService.isExistUser(employeeRequest.email())
                .flatMap(exist -> {
                    if (exist) {
                        log.info("ℹ️ User already exists.");
                        return Mono.error(new BadRequestException("User already exists"));
                    }

                    Employee employee = Employee.builder()
                            .name(employeeRequest.name())
                            .phone(employeeRequest.phone())
                            .department(employeeRequest.department())
                            .designation(employeeRequest.designation())
                            .joiningDate(employeeRequest.joiningDate())
                            .status(EmployeeStatus.ACTIVE)
                            .type(EmployeeType.PROBATIONARY)
                            .createdAt(LocalDate.now())
                            .updatedAt(LocalDate.now())
                            .build();

                    return employeeRepository.save(employee)
                            .flatMap(savedEmp -> {
                                log.info("New Employee is created.");

                                return userService.createUser(savedEmp.getId(), employeeRequest.email(), employeeRequest.phone(), isHR)
                                        .thenReturn(BaseResponse.success(savedEmp));
                            });

                });
    }
}
