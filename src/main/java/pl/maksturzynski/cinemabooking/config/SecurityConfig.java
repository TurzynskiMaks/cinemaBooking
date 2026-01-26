package pl.maksturzynski.cinemabooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui.html", "/swagger-ui/**",
                                "/api-docs/**",
                                "/favicon.ico", "h2-console/**"
                        ).permitAll()

                )
                .csrf(csrf -> csrf.disable()) // albo csrf.disable() w dev
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                .build();
    }
}
