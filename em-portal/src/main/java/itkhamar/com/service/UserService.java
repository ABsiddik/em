package itkhamar.com.service;

import itkhamar.com.dto.BaseResponse;
import itkhamar.com.dto.UserResponse;
import itkhamar.com.enums.RoleEnum;
import itkhamar.com.exception.BadRequestException;
import itkhamar.com.exception.NotFoundException;
import itkhamar.com.model.User;
import itkhamar.com.repository.UserRepository;
import itkhamar.com.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> findLoggedUser() {
        Mono<String> username = SecurityUtils.getCurrentUsername();
        return username.flatMap(userRepository::findByEmail)
                .switchIfEmpty(Mono.error(new NotFoundException("Invalid username to retrieve user")));
    }

    public Mono<ResponseEntity<?>> getUserInfo() {
        return findLoggedUser().flatMap(user ->
                Mono.just(BaseResponse.success(new UserResponse(user.getId(), user.getEmail(), user.getRoles().stream().map(Enum::name).toList()))));
    }

    public Mono<Boolean> isExistUser(String email) {
        return userRepository.existsByEmail(email);
    }

    public Mono<User> createUser(String employeeID, String email, String plainPassword, boolean isHR) {
        Set<RoleEnum> roles = new HashSet<>();
        roles.add(RoleEnum.USER);
        if (isHR) {
            roles.add(RoleEnum.HR);
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(plainPassword))
                .roles(roles)
                .employeeId(employeeID)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        return userRepository.save(user)
                .doOnSuccess(u -> log.info("New user is created successfully: {}", u.getId()))
                .doOnError(e -> log.error("Failed to create user for employeeId={} email={}", employeeID, email, e));
    }
}
