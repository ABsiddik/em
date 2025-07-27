package itkhamar.com.service;

import itkhamar.com.dto.LoginResponse;
import itkhamar.com.repository.UserRepository;
import itkhamar.com.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Mono<ResponseEntity<?>> login(String username, String password) {
        return userRepository.findByEmail(username)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        List<String> roles = user.getRoles().stream().map(Enum::name).toList();
                        String token = jwtUtil.generateToken(user.getEmail(), roles);
                        return Mono.just(ResponseEntity.ok(new LoginResponse(token, roles)));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found")));
    }

//    public Mono<ResponseEntity<String>> register(AppUser user) {
//        return userRepository.findByEmail(user.getUsername())
//                .flatMap(existing -> Mono.just(ResponseEntity.badRequest().body("User already exists")))
//                .switchIfEmpty(
//                        userService.save(AppUser.builder()
//                                        .username(user.getUsername())
//                                        .password(passwordEncoder.encode(user.getPassword()))
//                                        .roles(user.getRoles())
//                                        .createdAt(LocalDate.now())
//                                        .build())
//                                .thenReturn(ResponseEntity.ok("User registered"))
//                );
//    }
}
