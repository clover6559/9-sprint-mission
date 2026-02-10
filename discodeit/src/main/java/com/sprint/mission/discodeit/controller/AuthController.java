package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.LoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @RequestMapping(
            path = "login",
            method = RequestMethod.POST
    )
    public ResponseEntity<User> login(
            @RequestBody LoginDto loginDto

    ) {
        User login = authService.login(loginDto);
        return ResponseEntity.ok(login);
    }
}