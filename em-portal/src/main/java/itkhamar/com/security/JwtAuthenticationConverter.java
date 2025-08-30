package itkhamar.com.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {
    private final String jwtSecret;

    public JwtAuthenticationConverter(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtSecret.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();

                @SuppressWarnings("unchecked")
                List<String> roles = claims.get("role", List.class);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                return Mono.just(new UsernamePasswordAuthenticationToken(username, null, authorities));
            } catch (Exception e) {
                log.error("Error while converting token - " + e.getMessage());
                return Mono.empty();
            }
        }
        else {
            log.debug("Invalid bearer token");
        }

        return Mono.empty();
    }
}
