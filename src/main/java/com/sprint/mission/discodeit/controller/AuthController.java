package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.JwtDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import com.sprint.mission.discodeit.security.JwtTokenProvider;
import com.sprint.mission.discodeit.security.RefreshTokenStore;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final RefreshTokenStore refreshTokenStore;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;

  @GetMapping("/csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    String tokenValue = csrfToken.getToken();
    log.debug("CSRF 토큰 요청: {}", tokenValue);
    return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateRole(@RequestBody UserRoleUpdateRequest userRoleUpdateRequest) {
    UserDto updateRoleUser =  authService.updateRole(userRoleUpdateRequest.userId(), userRoleUpdateRequest.newRole());
    return ResponseEntity.status(HttpStatus.OK).body(updateRoleUser);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = getRefreshTokenFromCookie(request);

    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(new DiscodeitException(ErrorCode.INVALID_REQUEST), 401));
    }

    String username = refreshTokenStore.findUsername(refreshToken).orElse(null);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(new DiscodeitException(ErrorCode.INVALID_REQUEST), 401));
    }
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
    refreshTokenStore.remove(refreshToken);
    String newRefreshToken = jwtTokenProvider.generateRefreshToken();
    refreshTokenStore.save(newRefreshToken, username);

    Cookie refreshCookie = new Cookie("REFRESH_TOKEN", newRefreshToken);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(false);
    refreshCookie.setPath("/api/auth");
    refreshCookie.setMaxAge(7*24*60*60);
    response.addCookie(refreshCookie);

    DiscodeitUserDetails discodeitUserDetails = (DiscodeitUserDetails) userDetails;
    UserDto user = discodeitUserDetails.getUserDto();

    JwtDto responseBody = JwtDto.builder()
            .accessToken(newAccessToken)
            .userDto(user)
            .build();

    return ResponseEntity.ok().body(responseBody);
  }

  private String getRefreshTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return null;
    }
    return Arrays.stream(request.getCookies())
            .filter(cookie -> "REFRESH_TOKEN".equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);
  }
  }
