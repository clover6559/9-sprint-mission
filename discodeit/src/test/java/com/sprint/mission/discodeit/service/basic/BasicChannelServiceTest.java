package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Channel.ChannelAlreadyExistException;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.Channel.EmptyParticipantListException;
import com.sprint.mission.discodeit.exception.Channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelMapper channelMapper;

  @InjectMocks
  private BasicChannelService channelService;

  @Test
  @DisplayName("공개 채널 생성 성공")
  void create_public_success() {
    CreatePublicChannelRequest request = new CreatePublicChannelRequest("공지", "공지입니다.");
    given(channelRepository.existsByName("공지")).willReturn(false);
    channelService.create(request);
    then(channelRepository).should().save(any(Channel.class));
  }

  @Test
  @DisplayName("이름 중복으로 공개 채널 생성 실패")
  void create_public_fail() {
    CreatePublicChannelRequest request = new CreatePublicChannelRequest("공지", "공지입니다.");
    given(channelRepository.existsByName("공지")).willReturn(true);
    assertThrows(ChannelAlreadyExistException.class, () -> channelService.create(request));
  }

  @Test
  @DisplayName("비공개 채널 생성 성공")
  void create_private_success() {
    UUID userId = UUID.randomUUID();
    List<UUID> users = List.of(userId);
    CreatePrivateChannelRequest request = new CreatePrivateChannelRequest(users);
    given(userRepository.findById(userId))
        .willReturn(Optional.of(new User("testUser", "test@test.com", "password123", null)));
    given(channelRepository.save(any(Channel.class))).willAnswer(
        invocation -> invocation.getArgument(0));
    channelService.create(request);
    then(channelRepository).should().save(any(Channel.class));
  }

  // TODO 비공개 채널 생성 시 유저 없으면 생성 불가 예외 만들어서 수정
  @Test
  @DisplayName("유저가 없어서 비공개 채널 생성 실패")
  void create_private_fail() {
    CreatePrivateChannelRequest request = new CreatePrivateChannelRequest(new ArrayList<>());
    assertThrows(EmptyParticipantListException.class, () -> {
      channelService.create(request);
    });
  }

  @Test
  @DisplayName("채널 업데이트 성공")
  void update_success() {
    UUID channelId = UUID.randomUUID();
    ChannelUpdateRequest request = new ChannelUpdateRequest("새 공지", "새 공지방입니다.");
    Channel existingChannel = new Channel(ChannelType.PUBLIC, "옛날 공지", "옛날 설명");
    given(channelRepository.findById(channelId)).willReturn(Optional.of(existingChannel));

    channelService.update(channelId, request);
    then(channelRepository).should().save(any(Channel.class));
    assertEquals("새 공지", existingChannel.getName());
  }

  @Test
  @DisplayName("존재하지 않는 채널로 업데이트 실패")
  void update_fail() {
    UUID channelId = UUID.randomUUID();
    ChannelUpdateRequest request = new ChannelUpdateRequest("새 공지", "새 공지방입니다.");
    Channel existingChannel = new Channel(ChannelType.PUBLIC, "옛날 공지", "옛날 설명");
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());
    assertThrows(ChannelNotFoundException.class, () -> channelService.update(channelId, request));
    assertEquals("옛날 공지", existingChannel.getName());
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void delete_success() {
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(true);
    channelService.delete(channelId);
    then(messageRepository).should().deleteAllByChannelId(channelId);
    then(readStatusRepository).should().deleteAllByChannelId(channelId);
    then(channelRepository).should().deleteById(channelId);
  }

  @Test
  @DisplayName("존재하지 않는 채널로 삭제 실패")
  void delete_fail() {
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(false);
    assertThrows(ChannelNotFoundException.class, () -> channelService.delete(channelId));
    then(channelRepository).should(never()).deleteById(any());
  }

  @Test
  @DisplayName("유저로 채널 조회 성공")
  void findByUser_success() {
    UUID userId = UUID.randomUUID();
    Channel publicChannel = new Channel(ChannelType.PUBLIC, "공개", "설명");
    Channel joinedPrivate = new Channel(ChannelType.PRIVATE, "참여비공개", "설명");

    ReflectionTestUtils.setField(publicChannel, "id", UUID.randomUUID());
    ReflectionTestUtils.setField(joinedPrivate, "id", UUID.randomUUID());

    ReadStatus status = new ReadStatus(new User("test", "a@a.com", "123", null), joinedPrivate,
        Instant.now());

    given(readStatusRepository.findAllByUserId(any(UUID.class))).willReturn(List.of(status));

    given(channelRepository.findAllByTypeOrIdIn(any(), any())).willReturn(
        List.of(publicChannel, joinedPrivate));

    given(channelMapper.toDto(any(Channel.class))).willReturn(
        new ChannelDto(null, null, "명칭", null, null, null));

    List<ChannelDto> result = channelService.findAllByUserId(userId);
    assertEquals(2, result.size());
  }

  @Test
  @DisplayName("유저로 채널 조회 실패")
  void findByUser_fail() {
    UUID userId = UUID.randomUUID();
    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of());

    given(channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList()))
        .willReturn(List.of());
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    assertTrue(result.isEmpty());
  }
}
