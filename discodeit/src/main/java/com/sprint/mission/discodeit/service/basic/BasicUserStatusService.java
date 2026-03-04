package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatus.UserStatusCreate;
import com.sprint.mission.discodeit.dto.UserStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;


  @Override
  public UserStatus create(UserStatusCreate request) {
    UUID userId = request.userId();
    userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("해당 유저를을 찾을 수 없습니다. "));
    if (userStatusRepository.existsById(userId)) {
      throw new RuntimeException("해당 유저에 대한 유저 상태가 이미 존재합니다.");
    }
    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(userId, lastActiveAt);
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new RuntimeException("해당 유저 상태를 찾을 수 없습니다."));

  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public UserStatus updateByUserId(UUID userId, UserStatusUpdate update) {
    Instant newLastActiveAt = update.newLastActiveAt();
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("해당 유저의 읽음 상태를 찾을 수 없습니다. "));
    userStatus.update(newLastActiveAt);
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus update(UUID userStatusId, UserStatusUpdate update) {
    Instant newLastActiveAt = update.newLastActiveAt();
    UserStatus userStatus = userStatusRepository.findByUserId(userStatusId)
        .orElseThrow(() -> new RuntimeException("읽음 상태를 찾을 수 없습니다."));
    userStatus.update(newLastActiveAt);
    return userStatusRepository.save(userStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new RuntimeException("해당 유저 상태를 찾을 수 없습니다."));
    userStatusRepository.deleteById(userStatusId);

  }
}

