package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Test
  @DisplayName("채널과 유저로 메세지 조회 성공")
  void findAllByChannelIdWithAuthor_success() {
    User user = User.builder()
        .username("user1")
        .email("u1@test.com")
        .password("password123")
        .profile(null)
        .build();
    userRepository.save(user);

    UserStatus status =
        UserStatus.builder().user(user).lastActiveAt(Instant.now()).build();
    userStatusRepository.save(status);
    Channel channel = Channel.builder()
        .name("java-study")
        .type(ChannelType.PUBLIC)
        .user(user)
        .build();
    channelRepository.save(channel);
    Message message =
        Message.builder().content("hiii").author(user).channel(channel).build();
    messageRepository.save(message);

    Instant cursor = Instant.now().plusSeconds(1);
    PageRequest pageRequest = PageRequest.of(0, 10);
    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(channel.getId(), cursor,
        pageRequest);

    assertThat(result.getContent()).isNotEmpty();
    assertThat(result.getContent().get(0).getContent()).isEqualTo("hiii");
    assertThat(result.getContent().get(0).getAuthor().getUsername()).isEqualTo("user1");
  }

  @Test
  @DisplayName("채널과 유저로 메세지 조회 실패")
  void findAllByChannelIdWithAuthor_fail() {
    User user = userRepository.save(User.builder()
        .username("fail_user")
        .email("f@test.com")
        .password("123")
        .build());
    userStatusRepository.save(
        UserStatus.builder().user(user).lastActiveAt(Instant.now()).build());
    Channel channel = channelRepository.save(Channel.builder()
        .name("fail-ch")
        .type(ChannelType.PUBLIC)
        .user(user)
        .build());

    messageRepository.save(Message.builder()
        .content("너무 최신 메시지")
        .author(user)
        .channel(channel)
        .build());

    Instant pastCursor = Instant.now().minus(Duration.ofHours(1));
    PageRequest pageRequest = PageRequest.of(0, 10);
    Slice<Message> result =
        messageRepository.findAllByChannelIdWithAuthor(channel.getId(), pastCursor, pageRequest);

    assertThat(result.getContent()).isEmpty();
  }

  @Test
  @DisplayName("채널과 마지막 메세지 시간으로 조회 성공")
  void findLastMessageAtByChannelId_success() {
    User user = userRepository.save(User.builder()
        .username("time_user")
        .email("t@test.com")
        .password("123")
        .build());
    Channel channel = channelRepository.save(Channel.builder()
        .name("time-channel")
        .type(ChannelType.PUBLIC)
        .user(user)
        .build());

    messageRepository.save(
        Message.builder().content("첫 번째").author(user).channel(channel).build());
    Message lastMessage = messageRepository.save(
        Message.builder().content("두 번째").author(user).channel(channel).build());

    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        channel.getId());
    assertThat(lastMessageAt).isPresent();
    assertThat(lastMessageAt.get()).isAfter(Instant.MIN);
  }

  @Test
  @DisplayName("채널과 마지막 메세지 시간으로 조회 실패")
  void findLastMessageAtByChannelId_fail() {
    User user = userRepository.save(User.builder()
        .username("empty_user")
        .email("e@test.com")
        .password("123")
        .build());
    Channel emptyChannel = channelRepository.save(Channel.builder()
        .name("empty-channel")
        .type(ChannelType.PUBLIC)
        .user(user)
        .build());

    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        emptyChannel.getId());

    assertThat(lastMessageAt).isEmpty();
  }
}
