package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.loginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.IOException;

@Controller
@RequestMapping("/login")
public class AuthController {

    @RequestMapping(
            path = "login",
            method = RequestMethod.POST

    )
    public ResponseEntity<Login> login(
            @RequestPart("loginRequest")loginDto loginDto

    ) throws IOException {

        loginDto.userName()
    }
}