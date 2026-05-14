package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.auth.CsrfCookieFilter;
import com.sprint.mission.discodeit.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.handler.*;
import com.sprint.mission.discodeit.handler.SpaCsrfTokenRequestHandler;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor

public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
//    private final DiscodeitUserDetails discodeitUserDetails;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
                .addFilterAfter(new CsrfCookieFilter(), CsrfFilter.class)
                .formLogin(login -> login.loginProcessingUrl("/api/auth/login")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/", "/index.html", "/static/**", "/assets/**", "*.js", "*.css", "*.ico").permitAll()
                        .requestMatchers("/api/auth/csrf-token", "/api/auth/login", "/api/auth/logout", "/api/auth").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
//                .rememberMe(remember -> remember
//                        .key("myAppSecretKey")
//                        .tokenValiditySeconds(1209600)
//                        .userDetailsService(discodeitUserDetails)
//                        .rememberMeCookieName("remember-me")
//                        .rememberMeParameter("remember-me")
//                        .useSecureCookie(true)
//                        .alwaysRemember(false)
//                )
                .sessionManagement(management -> management
                        .sessionConcurrency(concurrency -> concurrency.maximumSessions(1)
                                .maxSessionsPreventsLogin(false)
                                .sessionRegistry(sessionRegistry())))
                .logout(logout -> logout.logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(
                                new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initAdminAccount(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            User admin = userRepository.findByUsername("admin").orElseGet(() -> {
                return User.builder()
                        .username("admin")
                        .email("admin@discodeit.com")
                        .password(passwordEncoder.encode("admin1234!"))
                        .build();
            });
            admin.updateRole(Role.ADMIN);
            userRepository.save(admin);
        };
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}