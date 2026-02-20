package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.UserStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "User API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Operation(summary = "회원 등록")
  @PostMapping(
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity<User> create(
      @RequestPart("userCreateRequest") UserCreate userCreate,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) throws IOException {
    Optional<BinaryContentCreate> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    User createdUser = userService.create(userCreate, profileRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdUser);
  }

  @Operation(summary = "회원 정보 수정")
  @PatchMapping(
      path = "/{userId}",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity<User> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) throws IOException {
    Optional<BinaryContentCreate> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUser);
  }

  @Operation(summary = "회원 삭제")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID userId
  ) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "전체 회원 조회")
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> userList = userService.findAll();
    return ResponseEntity.ok(userList);
  }

  @Operation(summary = "회원의 온라인 상태 업데이트")
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatus> statusUpdate(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdate request
  ) throws IllegalArgumentException {
    UserStatus userStatus = userStatusService.update(userId, request);
    return ResponseEntity.ok(userStatus);
  }

  private Optional<BinaryContentCreate> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreate binaryContentCreate = new BinaryContentCreate(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreate);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
