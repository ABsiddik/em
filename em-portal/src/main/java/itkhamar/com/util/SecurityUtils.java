package itkhamar.com.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

public class SecurityUtils {
    public static Mono<Authentication> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication);
    }

    public static Mono<String> getCurrentUsername() {
        return getCurrentUser().map(Authentication::getName);
    }
}
