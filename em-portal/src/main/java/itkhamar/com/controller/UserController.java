package itkhamar.com.controller;

import itkhamar.com.util.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile")
    public Mono<String> getMyProfile() {
        return SecurityUtils.getCurrentUsername();
    }
}
