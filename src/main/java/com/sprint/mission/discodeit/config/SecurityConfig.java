package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.auth.CsrfCookieFilter;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.handler.*;
import com.sprint.mission.discodeit.handler.SpaCsrfTokenRequestHandler;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
  private final ObjectMapper objectMapper;

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
            .requestMatchers("/api/auth/csrf-token", "/api/auth/login", "/api/auth/logout", "/api/auth/").permitAll()
            .requestMatchers("/swagger-ui/**","/v3/api-docs/**", "/actuator/**").permitAll()
            .anyRequest().authenticated())
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) -> {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
              response.setContentType("application/json;charset=UTF-8");

              ErrorResponse errorResponse = new ErrorResponse(
                  Instant.now(),
                  "UNAUTHORIZED",
                  "로그인이 필요한 서비스입니다.",
                  null,
                  authException.getClass().getSimpleName(),
                  HttpServletResponse.SC_UNAUTHORIZED
              );

              String result = objectMapper.writeValueAsString(errorResponse);
              response.getWriter().write(result);
            })

            // 2. 403 Forbidden: 로그인은 했지만 해당 API를 쓸 권한이 없는 경우
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
              response.setContentType("application/json;charset=UTF-8");

              ErrorResponse errorResponse = new ErrorResponse(
                  Instant.now(),
                  "FORBIDDEN",
                  "해당 리소스에 접근할 권한이 없습니다.",
                  null,
                  accessDeniedException.getClass().getSimpleName(),
                  HttpServletResponse.SC_FORBIDDEN
              );

              String result = objectMapper.writeValueAsString(errorResponse);
              response.getWriter().write(result);
            })
        )
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
  public UserDetailsService userDetailsService(
      PasswordEncoder passwordEncoder
  ) {

    return new InMemoryUserDetailsManager(
        org.springframework.security.core.userdetails.User.builder()
            .username("user")
            .password(passwordEncoder.encode("1234"))
            .roles("USER")
            .build()
    );
  }

  @Bean
  public CommandLineRunner initAdminAccount(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder
  ) {
    return args -> {
      if (!userRepository.existsByRole(Role.ADMIN)) {
        User admin = User.builder()
            .username("admin")
            .email("admin@discodeit.com")
            .password(passwordEncoder.encode("admin1234"))
            .build();
        admin.updateRole(Role.ADMIN);
        userRepository.save(admin);
      }
    };
  }
}
