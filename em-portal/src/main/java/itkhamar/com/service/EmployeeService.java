package itkhamar.com.service;

import itkhamar.com.dto.BaseResponse;
import itkhamar.com.dto.EmployeeResponse;
import itkhamar.com.model.User;
import itkhamar.com.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
}
