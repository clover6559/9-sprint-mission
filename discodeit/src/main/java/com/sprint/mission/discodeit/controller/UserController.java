package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(
            path = "/create",
            method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<User> create(
            @RequestPart("userCreateRequest") UserCreate.BasicUserInfo basicUserInfo,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        UserCreate.ProfileImageInfo profileImageInfo = null;
        if (profile != null && !profile.isEmpty()) {
            profileImageInfo = new UserCreate.ProfileImageInfo(
                    UUID.randomUUID(),profile.getOriginalFilename(), profile.getBytes()
            );
        }
        UserCreate requestDto = new UserCreate(basicUserInfo, profileImageInfo);
        User createdUser = userService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @RequestMapping(
            path = "/{userId}",
            method = RequestMethod.PATCH,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Void> update(
            @PathVariable UUID userId,
            @RequestPart("userUpdateInfo") UserUpdate.UserUpdateInfo userUpdateInfo,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        UserCreate.ProfileImageInfo profileImageInfo = null;
        if (profile != null && !profile.isEmpty()) {
            profileImageInfo = new UserCreate.ProfileImageInfo(
                    null,profile.getOriginalFilename(), profile.getBytes()
            );
        }
        UserUpdate.UserUpdateInfo finalUpdateInfo = new UserUpdate.UserUpdateInfo(
                userUpdateInfo.userName(),
                userUpdateInfo.email(),
                userUpdateInfo.password(),
                profileImageInfo
        );
        UserUpdate userUpdate = new UserUpdate(userId, finalUpdateInfo);
        userService.update(userUpdate);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            path = "/{userId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<Void> delete(
            @PathVariable UUID userId
    )  {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userList = userService.findAll();
        return ResponseEntity.ok(userList);
    }

    @RequestMapping(
            path = "/{userId}/status",
            method = RequestMethod.PATCH
    )
    public ResponseEntity<Void> statusUpdate(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdate request
    ) throws IllegalArgumentException {
        userStatusService.update(userId,request);
        return ResponseEntity.ok().build();
    }
}
