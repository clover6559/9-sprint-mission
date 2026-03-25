package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    log.info("사용자 등록 시작 - 사용자 이름: {}, 사용자 이메일 : {}", userCreateRequest.username(),
        userCreateRequest.email());
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();
    if (userRepository.existsByEmail(email)) {
      log.warn("중복 이메일로 인해 사용자 등록 실패 - 이메일: {}", email);
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      log.warn("중복 이름으로 인해 사용자 등록 실패 - 이름: {}", username);
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          log.info("프로필 이미지 등록 시작 - 파일 이름 : {}. 파일 타입 : {}, 파일 용량: {}", profileRequest.fileName(),
              profileRequest.contentType(), profileRequest.bytes());
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          log.info("프로필 이미지 등록 성공 - 파일 이름 : {}, 파일 ID : {}", profileRequest.fileName(),
              binaryContent.getId());
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);
    String password = userCreateRequest.password();

    User user = new User(username, email, password, nullableProfile);
    Instant now = Instant.now();
    log.info("사용자 상태 생성 - 사용자 이름 : {}", username);
    UserStatus userStatus = new UserStatus(user, now);
    userRepository.save(user);
    log.info("사용자 등록 성공 - 사용자 이름 : {}, 사용자 이메일 : {}, 사용자 ID : {}", username, email, user.getId());
    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdate,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    log.info("사용자 정보 업데이트 시작 - 사용자 ID: {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("존재하지 않는 ID로 업데이트 실패 - 사용자 ID: {}", userId);
          return new NoSuchElementException("User with id " + userId + " not found");
        });

    String newUsername = userUpdate.newUsername();
    String newEmail = userUpdate.newEmail();
    log.info("변경하고자 하는 정보 - 이메일: {}, 이름: {}", newEmail, newUsername);

    if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
      log.warn("이미 존재하는 이메일으로 업데이트 실패 - 이메일: {}", newEmail);
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }

    if (!user.getUsername().equals(newUsername) && userRepository.existsByUsername(newUsername)) {
      log.warn("이미 존재하는 이름으로 업데이트 실패 - 이름: {}", newUsername);
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          log.info("프로필 업데이트 시작 - 파일 이름: {}, 파일 타입: {}, 파일 용량: {}", profileRequest.fileName(),
              profileRequest.contentType(), profileRequest.bytes());
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          log.info("프로필 업데이트 성공 - 파일 이름: {}, 파일 ID: {}", profileRequest.fileName(),
              binaryContent.getId());
          return binaryContent;
        })
        .orElse(null);

    String newPassword = userUpdate.newPassword();
    log.info("사용자 업데이트할 정보 - 사용자 ID: {}, 사용자 이름: {}, 사용자 이메일: {}, 사용자 비밀번호: {}", userId,
        newUsername, newEmail, hashFirstChars(newPassword));
    user.update(newUsername, newEmail, newPassword, nullableProfile);
    log.info("사용자 정보 업데이트 성공 - 사용자 ID: {}, 사용자 이름: {}, 사용자 이메일: {}, 사용자 비밀번호: {}", userId,
        newUsername, newEmail, hashFirstChars(newPassword));
    return userMapper.toDto(user);
  }

  private String hashFirstChars(String password) {
    return password.substring(0, Math.min(3, password.length())) + "***";
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    log.info("사용자 삭제 요청 - 사용자 ID: {}", userId);
    if (!userRepository.existsById(userId)) {
      log.warn("존재하지 않는 사용자로 삭제 실패 - 사용자 ID: {}", userId);
      throw new NoSuchElementException("User with id " + userId + " not found");
    }
    userRepository.deleteById(userId);
    log.info("사용자 삭제 성공 - 사용자 ID: {}", userId);
  }
}
