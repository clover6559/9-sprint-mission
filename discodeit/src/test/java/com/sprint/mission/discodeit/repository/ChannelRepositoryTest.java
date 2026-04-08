package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("채널 타입 또는 id들로 채널 조회 성공")
    void findAllByTypeOrIdIn_success() {
        User user = User.builder()
                .username("user1")
                .email("u1@test.com")
                .password("password123")
                .profile(null)
                .build();
        userRepository.save(user);
        Channel channel = Channel.builder().type(ChannelType.PUBLIC).user(user).build();
        ChannelType channelType = ChannelType.PUBLIC;
        channelRepository.save(channel);
        List<Channel> channels = channelRepository.findAllByTypeOrIdIn(channelType, null);
        assertThat(channels).isNotEmpty();
        assertThat(channels.get(0).getType().equals(ChannelType.PUBLIC));
    }

    @Test
    @DisplayName("채널 타입 또는 id들로 채널 조회 실패")
    void findAllByTypeOrIdIn_fail() {
        User user = User.builder()
                .username("user1")
                .email("u1@test.com")
                .password("password123")
                .profile(null)
                .build();
        Channel channel = Channel.builder().type(ChannelType.PRIVATE).user(user).build();
        ChannelType channelType = ChannelType.PUBLIC;
        List<UUID> ids = new ArrayList<>();
        channelRepository.findAllByTypeOrIdIn(channelType, ids);
        assertThat(ids).isEmpty();
    }
}
