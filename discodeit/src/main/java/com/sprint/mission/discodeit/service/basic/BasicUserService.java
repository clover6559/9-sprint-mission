package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.User.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    public UserDto create(
            UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        log.info("유저 등록 요청 - 유저 이름: {}, 유저 이메일 : {}", userCreateRequest.username(), userCreateRequest.email());
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();
        if (userRepository.existsByEmail(email)) {
            log.warn("중복 이메일로 인해 유저 등록 실패 - 이메일: {}", email);
            throw UserAlreadyExistException.ofEmail(email);
        }
        if (userRepository.existsByUsername(username)) {
            log.warn("중복 이름으로 인해 유저 등록 실패 - 이름: {}", username);
            throw UserAlreadyExistException.ofUsername(username);
        }

        BinaryContent nullableProfile = optionalProfileCreateRequest
                .map(profileRequest -> {
                    log.info(
                            "프로필 이미지 등록 요청 - 파일 이름 : {}. 파일 타입 : {}, 파일 용량: {} bytes",
                            profileRequest.fileName(),
                            profileRequest.contentType(),
                            profileRequest.bytes().length);
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
                    binaryContentRepository.save(binaryContent);
                    log.info(
                            "프로필 이미지 등록 성공 - 파일 이름 : {}, 파일 ID : {}", profileRequest.fileName(), binaryContent.getId());
                    binaryContentStorage.put(binaryContent.getId(), bytes);
                    return binaryContent;
                })
                .orElse(null);
        String password = userCreateRequest.password();

        User user = new User(username, email, password, nullableProfile);
        Instant now = Instant.now();
        log.info("유저 상태 생성 - 유저 이름 : {}", username);
        UserStatus userStatus = new UserStatus(user, now);
        userRepository.save(user);
        userStatusRepository.save(userStatus);
        log.info("유저 등록 성공 - 유저 이름 : {}, 유저 이메일 : {}, 유저 ID : {}", username, email, user.getId());
        return userMapper.toDto(user);
    }

    @Override
    public UserDto find(UUID userId) {
        return userRepository
                .findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAllWithProfileAndStatus().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public UserDto update(
            UUID userId,
            UserUpdateRequest userUpdate,
            Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        log.info("유저 정보 업데이트 요청 - 유저 ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("존재하지 않는 ID로 업데이트 실패 - 유저 ID: {}", userId);
            return new UserNotFoundException(userId);
        });

        String newUsername = userUpdate.newUsername();
        String newEmail = userUpdate.newEmail();
        log.info("변경하고자 하는 정보 - 이메일: {}, 이름: {}", newEmail, newUsername);

        if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            log.warn("이미 존재하는 이메일으로 업데이트 실패 - 이메일: {}", newEmail);
            throw UserAlreadyExistException.ofEmail(newEmail);
        }

        if (!user.getUsername().equals(newUsername) && userRepository.existsByUsername(newUsername)) {
            log.warn("이미 존재하는 이름으로 업데이트 실패 - 이름: {}", newUsername);
            throw UserAlreadyExistException.ofUsername(newUsername);
        }

        BinaryContent nullableProfile = optionalProfileCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    log.info(
                            "프로필 업데이트 요청 - 파일 이름: {}, 파일 타입: {}, 파일 용량: {}",
                            profileRequest.fileName(),
                            profileRequest.contentType(),
                            profileRequest.bytes());
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
                    binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(binaryContent.getId(), bytes);
                    log.info("프로필 업데이트 성공 - 파일 이름: {}, 파일 ID: {}", profileRequest.fileName(), binaryContent.getId());
                    return binaryContent;
                })
                .orElse(null);

        String newPassword = userUpdate.newPassword();
        log.info(
                "업데이트할 유저 정보 - 유저 ID: {}, 유저 이름: {}, 유저 이메일: {}, 유저 비밀번호: {}",
                userId,
                newUsername,
                newEmail,
                hashFirstChars(newPassword));
        user.update(newUsername, newEmail, newPassword, nullableProfile);
        log.info(
                "유저 정보 업데이트 성공 - 유저 ID: {}, 유저 이름: {}, 유저 이메일: {}, 유저 비밀번호: {}",
                userId,
                newUsername,
                newEmail,
                hashFirstChars(newPassword));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    private String hashFirstChars(String password) {
        return password.substring(0, Math.min(3, password.length())) + "***";
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        log.info("유저 삭제 요청 - 유저 ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("존재하지 않는 유저 ID로 삭제 실패 - 유저 ID: {}", userId);
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
        log.info("유저 삭제 성공 - 유저 ID: {}", userId);
    }
}
