package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.User.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private BinaryContentRepository binaryContentRepository;
    @Mock private UserMapper userMapper;
    @Mock private BinaryContentStorage binaryContentStorage;
    @Mock private UserStatusRepository userStatusRepository;
    @InjectMocks private BasicUserService userService;

    // create
    @Test
    @DisplayName("유저 생성 성공")
    void create_success() {
        UserCreateRequest request =
                new UserCreateRequest("testUser", "test@test.com", "password123");
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(userRepository.existsByUsername(anyString())).willReturn(false);
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        userService.create(request, Optional.empty());
        then(userRepository).should(times(1)).save(any(User.class));
        then(userStatusRepository).should().save(any());
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 유저 생성 시 예외 발생")
    void create_fail_duplicateEmail() {
        UserCreateRequest request =
                new UserCreateRequest("testUser", "duplicate@test.com", "password123");
        given(userRepository.existsByEmail(anyString())).willReturn(true);
        assertThrows(
                UserAlreadyExistException.class,
                () -> userService.create(request, Optional.empty()));
    }

    @Test
    @DisplayName("유저 정보 업데이트 성공")
    void update_success() {
        UUID userId = UUID.randomUUID();
        User user = new User("oldName", "old@test.com", "pw", null);
        UserUpdateRequest updateRequest = new UserUpdateRequest("newName", "new@test.com", "newPw");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(userRepository.existsByUsername(anyString())).willReturn(false);

        userService.update(userId, updateRequest, Optional.empty());
        assertEquals("newName", user.getUsername());
        then(userRepository).should().save(any());
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 유저 수정 시 예외 발생")
    void update_fail_duplicateEmail() {
        UUID userId = UUID.randomUUID();
        User user = new User("oldName", "old@test.com", "pw", null);
        UserUpdateRequest updateRequest = new UserUpdateRequest("newName", "new@test.com", "newPw");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(anyString())).willReturn(true);

        assertThrows(
                UserAlreadyExistException.class,
                () -> userService.update(userId, updateRequest, Optional.empty()));
        assertEquals("old@test.com", user.getEmail());
    }

    @Test
    @DisplayName("유저 삭제 성공")
    void delete_success() {
        UUID userId = UUID.randomUUID();
        given(userRepository.existsById(userId)).willReturn(true);

        userService.delete(userId);
        then(userRepository).should().deleteById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 유저 삭제 시 예외 발생")
    void delete_fail_notFound() {
        UUID userId = UUID.randomUUID();
        given(userRepository.existsById(userId)).willReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
        then(userRepository).should(never()).deleteById(any());
    }
}
