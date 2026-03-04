package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.Api.AuthApi;
import com.sprint.mission.discodeit.dto.LoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthService authService;

  @PostMapping("/login")
  @Override
  public ResponseEntity<User> login(
      @RequestBody LoginDto loginDto

  ) {
    User login = authService.login(loginDto);
    return ResponseEntity.ok(login);
  }
}