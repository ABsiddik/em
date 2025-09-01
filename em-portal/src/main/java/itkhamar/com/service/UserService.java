package itkhamar.com.service;

import itkhamar.com.dto.BaseResponse;
import itkhamar.com.dto.UserResponse;
import itkhamar.com.exception.BadRequestException;
import itkhamar.com.exception.NotFoundException;
import itkhamar.com.model.User;
import itkhamar.com.repository.UserRepository;
import itkhamar.com.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<User> findLoggedUser() {
        Mono<String> username = SecurityUtils.getCurrentUsername();
        return username.flatMap(userRepository::findByEmail)
                .switchIfEmpty(Mono.error(new NotFoundException("Invalid username to retrieve user")));
    }

    public Mono<ResponseEntity<?>> getUserInfo() {
        return findLoggedUser().flatMap(user ->
                Mono.just(BaseResponse.success(new UserResponse(user.getId(), user.getEmail(), user.getRoles().stream().map(Enum::name).toList()))));
    }
}
