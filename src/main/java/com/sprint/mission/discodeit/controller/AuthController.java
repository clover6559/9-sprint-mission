package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;

  @GetMapping("/csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    String tokenValue = csrfToken.getToken();
    log.debug("CSRF 토큰 요청: {}", tokenValue);
    return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getUserDto(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      throw new DiscodeitException(ErrorCode.INVALID_USER_CREDENTIALS);
    }

    UserDto user;

    if (userDetails instanceof DiscodeitUserDetails customUserDetails) {
      user = customUserDetails.getUserDto();
    } else {
      user = new UserDto(
          null,
          userDetails.getUsername(),
          "test@example.com",
          null,
          true
      );
    }
      return ResponseEntity.status(HttpStatus.OK).body(user);
    }

//  @PutMapping("/role")
//  public ResponseEntity<UserDto> updateRole(@RequestBody UserRoleUpdateRequest userRoleUpdateRequest) {
//    UserDto updateRoleUser =  authService.updateRole(userRoleUpdateRequest.userId(), userRoleUpdateRequest.newRole());
//    return ResponseEntity.status(HttpStatus.OK).body(updateRoleUser);
//  }
  }
