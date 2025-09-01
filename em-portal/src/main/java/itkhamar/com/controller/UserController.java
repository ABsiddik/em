package itkhamar.com.controller;

import itkhamar.com.dto.BaseResponse;
import itkhamar.com.dto.UserResponse;
import itkhamar.com.service.UserService;
import itkhamar.com.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public Mono<ResponseEntity<?>> getMyInfo() {
        return userService.getUserInfo();
    }
}
