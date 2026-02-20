package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserFind;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.search.UserSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;


  @Override
  public User create(UserCreate userCreate) {
    // 1. 유효성 검사 (데이터 구조 변경에 맞춰 .basicUserInfo()로 접근)
    if (userRepository.findByName(userCreate.basicUserInfo().username()) != null) {
      throw new RuntimeException("이미 존재하는 이름입니다.");
    }
    if (userRepository.findByEmail(userCreate.basicUserInfo().email()) != null) {
      throw new RuntimeException("이미 존재하는 이메일입니다.");
    }
    User savedUser = new User(userCreate);
    userRepository.save(savedUser);

    if (userCreate.profileImageInfo() != null) {
      BinaryContent profileImage = new BinaryContent(
          userCreate.profileImageInfo().fileName(), userCreate.profileImageInfo().size(),
          userCreate.profileImageInfo().contentType(),
          userCreate.profileImageInfo().data()
      );
      // 이미지 저장
      BinaryContent savedImage = binaryContentRepository.save(profileImage);

      // [중요] 생성된 이미지 ID를 유저에게 업데이트 (연결)
      savedUser.updateProfileId(savedImage.getId());
      userRepository.save(savedUser); // 업데이트 반영
    }

    // 4. 유저 상태 초기화
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(savedUser.getId(), now);
    userStatusRepository.save(userStatus);

    return savedUser;
  }

  @Override
  public UserFind find(UUID userId) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
    UserStatus userStatus = userStatusRepository.findByUserId(findUser.getId())
        .orElse(null);
    boolean isOnline = (userStatus != null) && userStatus.isOnline();
    return new UserFind(isOnline, findUser.getId(), findUser.getUsername(), findUser.getEmail());

  }

  @Override
  public List<UserDto> findAll() {
    List<User> userList = userRepository.findAll();
    return userList.stream()
        .map(user -> {
          UserStatus status = userStatusRepository.findByUserId(user.getId())
              .orElse(null);
          boolean isOnline = (status != null) && status.isOnline();
          return new UserDto(
              user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(),
              user.getEmail(), user.getProfileId(), isOnline
          );
        })
        .toList();
  }

  @Override
  public List<UserFind> search(UserSearch userSearch) {
    return userRepository.findAll().stream()
        .filter(u -> userSearch.getUserName() == null || userSearch.getUserName()
            .equals(u.getUsername()))
        .map(user -> {
          UserStatus status = userStatusRepository.findByUserId(user.getId())
              .orElse(null);
          boolean isOnline = (status != null) && status.isOnline();
          return new UserFind(isOnline, user.getId(), user.getUsername(), user.getEmail());
        })
        .toList();
  }

  @Override
  public void update(@org.jetbrains.annotations.UnknownNullability UserUpdateRequest userUpdate) {
    User findUser = userRepository.findById(userUpdate.targetId())
        .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
    findUser.updateInfo(userUpdate.userUpdateInfo());
    if (userUpdate.userUpdateInfo().profileImageInfo() != null) {
      BinaryContent newContent = new BinaryContent(
          userUpdate.userUpdateInfo().profileImageInfo().fileName(),
          userUpdate.userUpdateInfo().profileImageInfo().size(),
          userUpdate.userUpdateInfo().profileImageInfo().contentType(),
          userUpdate.userUpdateInfo().profileImageInfo().data()
      );
      BinaryContent savedContent = binaryContentRepository.save(newContent);

      UUID oldProfileId = findUser.getProfileId();

      // 새 프로필 ID로 업데이트
      findUser.updateProfileId(savedContent.getId());

      if (oldProfileId != null) {
        binaryContentRepository.deleteById(oldProfileId);
      }
      System.out.println("[Service]\n프로필 이미지 교체 완료");
    }
    userRepository.save(findUser);
  }


  @Override
  public void delete(UUID userId) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
    userStatusRepository.deleteByUserId(userId);
    if (findUser.getProfileId() != null) {
      binaryContentRepository.deleteById(findUser.getProfileId());
    }
    userRepository.deleteById(userId);
    return true;
  }

  public void printRemainUsers() {
    List<User> users = userRepository.findAll();
    System.out.println("현재 남은 유저 수: " + users.size());
    users.forEach(u -> System.out.println("- " + u.getUsername()));
  }

}
