package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("전체 사용자 Fetch Join 조회 성공")
    void findAllWithProfileAndStatus_Success() {
        User user = User.builder()
                .username("user1")
                .email("u1@test.com")
                .password("password123")
                .profile(null)
                .build();
        UserStatus status =
                UserStatus.builder().user(user).lastActiveAt(Instant.now()).build();
        userRepository.save(user);

        List<User> users = userRepository.findAllWithProfileAndStatus();

        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("저장된 사용자가 없어서 전체 사용자 조회 실패")
    void findAllWithProfileAndStatus_Fail_Empty() {

        userRepository.deleteAll();

        List<User> users = userRepository.findAllWithProfileAndStatus();

        assertThat(users).isEmpty();
        assertThat(users.size()).isEqualTo(0);
    }
}
