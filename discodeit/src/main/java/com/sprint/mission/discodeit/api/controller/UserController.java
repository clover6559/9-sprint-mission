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
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
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
  public ResponseEntity<UserDto> create(
      @Valid @RequestPart(value = "userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.info("유저 생성 요청 수신 - 유저 이름: {}", userCreateRequest.username());
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    UserDto createdUser = userService.create(userCreateRequest, profileRequest);
    log.info("유저 생성 요청 처리 완료 - 유저 ID: {}", createdUser.id());
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdUser);
  }

  @PatchMapping(
      path = "/{userId}",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  @Override
  public ResponseEntity<UserDto> update(
      @PathVariable UUID userId,
      @Valid @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.info("유저 정보 업데이트 요청 수신 - 유저 ID: {}, 변경할 이름: {}, 변경할 이메일: {}, 비밀번호 변경 여부: {}",
        userId, userUpdateRequest.newUsername(), userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword().isEmpty());
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    UserDto updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
    log.info("유저 정보 업데이트 처리 완료 - 유저 ID: {}, 변경한 이름: {}, 변경한 이메일: {}",
        updatedUser.id(), updatedUser.username(), updatedUser.email());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUser);
  }

  @DeleteMapping("/{userId}")
  @Override
  public ResponseEntity<Void> delete(
      @PathVariable UUID userId
  ) {
    log.info("유저 삭제 요청 수신 - 유저 ID: {}", userId);
    userService.delete(userId);
    log.info("유저 삭제 요청 처리 완료 - 유저 ID: {}", userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  @Override
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(users);
  }

  @PatchMapping("/{userId}/userStatus")
  @Override
  public ResponseEntity<UserStatusDto> statusUpdate(
      @PathVariable UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest request
  ) {
    log.info("유저 상태 업데이트 요청 수신 - 유저 ID: {}", userId);
    UserStatusDto updatedUserStatus = userStatusService.updateByUserId(userId, request);
    log.info("유저 상태 업데이트 처리 완료 - 유저 ID: {}", userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUserStatus);
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      log.debug("프로필 파일이 비어있어 변환을 건너뜁니다.");
      return Optional.empty();
    } else {
      try {
        log.info("프로필 파일 변환 시도 - 파일 이름: {}, 타입: {}, 크기: {} bytes",
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getSize());

        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        log.debug("프로필 파일 변환 완료");
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        log.error("프로필 파일 데이터 읽기 중 오류 발생 - 파일 이름: {}", profileFile.getOriginalFilename(), e);
        throw new RuntimeException(e);
      }
    }
  }
}
