package itkhamar.com.config;

import itkhamar.com.enums.EmployeeStatus;
import itkhamar.com.enums.EmployeeType;
import itkhamar.com.enums.RoleEnum;
import itkhamar.com.model.Employee;
import itkhamar.com.model.User;
import itkhamar.com.repository.EmployeeRepository;
import itkhamar.com.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData() {
        userRepository.count()
                .flatMap(count -> {
                    if (count == 0) {
                        Employee defaultEmployee = Employee.builder()
                                .name("System Admin")
                                .phone("01700000000")
                                .department("IT")
                                .designation("Administrator")
                                .status(EmployeeStatus.ACTIVE)
                                .type(EmployeeType.PERMANENT)
                                .createdAt(LocalDate.now())
                                .updatedAt(LocalDate.now())
                                .build();
                        return employeeRepository.save(defaultEmployee)
                                .flatMap(savedEmployee -> {
                                    System.out.println("Default Employee is created.");

                                    Set<RoleEnum> roles = new HashSet<>();
                                    roles.add(RoleEnum.USER);
                                    roles.add(RoleEnum.ADMIN);

                                    User defaultUser = User.builder()
                                            .email("admin@itkhamar.com")
                                            .password(passwordEncoder.encode("admin123"))
                                            .roles(roles)
                                            .employeeId(savedEmployee.getId())
                                            .createdAt(LocalDate.now())
                                            .updatedAt(LocalDate.now())
                                            .build();

                                    return userRepository.save(defaultUser)
                                            .doOnSuccess(user -> System.out.println("✅ Default admin user created."));
                                });
                    } else {
                        System.out.println("ℹ️ Users already exist. Skipping default user creation.");
                        return Mono.empty();
                    }
                })
                .subscribe();
    }
}
