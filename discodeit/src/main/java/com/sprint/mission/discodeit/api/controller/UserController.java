package com.sprint.mission.discodeit.api.controller;

import com.sprint.mission.discodeit.api.UserApi;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  @Override
  public UserDto create(
      @RequestPart(value = "userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    return userService.create(userCreateRequest, profileRequest);
  }

  @PatchMapping(
      path = "/{userId}",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  @Override
  public UserDto update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    return userService.update(userId, userUpdateRequest, profileRequest);
  }

  @DeleteMapping("/{userId}")
  @Override
  public void delete(
      @PathVariable UUID userId
  ) {
    userService.delete(userId);
  }

  @GetMapping
  @Override
  public List<UserDto> findAll() {
    return userService.findAll();
  }

  @PatchMapping("/{userId}/userStatus")
  @Override
  public UserStatusDto statusUpdate(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) throws IllegalArgumentException {
    return userStatusService.updateByUserId(userId, request);
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
