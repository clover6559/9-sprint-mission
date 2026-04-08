package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.Message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private BasicMessageService messageService;

    @Test
    @DisplayName("메세지 생성 성공")
    void create_success() {
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        User sender = new User("testUser", "test@test.com", "pw", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "공지", "설명");
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(userRepository.findById(userId)).willReturn(Optional.of(sender));

        MessageCreateRequest request = new MessageCreateRequest("안녕하세요", channelId, userId);
        List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();
        messageService.create(request, binaryContentCreateRequests);
        then(messageRepository).should().save(any(Message.class));
    }

    @Test
    @DisplayName("메세지 성공 실패")
    void create_fail() {
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        Channel channel = new Channel(ChannelType.PUBLIC, "공지", "설명");
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        MessageCreateRequest request = new MessageCreateRequest("안녕하세요", channelId, userId);
        List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();
        assertThrows(UserNotFoundException.class, () -> messageService.create(request, binaryContentCreateRequests));
    }

    @Test
    @DisplayName("메세지 수정 성공")
    void update_success() {
        MessageUpdateRequest request = new MessageUpdateRequest("반가워요");
        UUID messageId = UUID.randomUUID();
        User user = new User("testUser", "test@test.com", "password123", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "공지", "공지합니다.");
        Message message = new Message("안녕하세요", channel, user, null);
        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
        messageService.update(messageId, request);
        assertEquals("반가워요", message.getContent());
    }

    @Test
    @DisplayName("메세지 수정 실패")
    void update_fail() {
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest request = new MessageUpdateRequest("반가워요");
        User user = new User("testUser", "test@test.com", "password123", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "공지", "공지합니다.");
        Message message = new Message("안녕하세요", channel, user, null);
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());
        assertThrows(MessageNotFoundException.class, () -> messageService.update(messageId, request));
        assertEquals("안녕하세요", message.getContent());
    }

    @Test
    @DisplayName("메세지 삭제 성공")
    void delete_success() {
        UUID messageId = UUID.randomUUID();
        given(messageRepository.existsById(messageId)).willReturn(true);
        messageService.delete(messageId);
        then(messageRepository).should().deleteById(messageId);
    }

    @Test
    @DisplayName("메세지 삭제 실패")
    void delete_fail() {
        UUID messageId = UUID.randomUUID();
        given(messageRepository.existsById(messageId)).willReturn(false);
        assertThrows(MessageNotFoundException.class, () -> messageService.delete(messageId));
        then(messageRepository).should(never()).deleteById(messageId);
    }

    @Test
    @DisplayName("채널에 있는 메세지 조회 성공")
    void findByChannel_success() {
        UUID channelId = UUID.randomUUID();
        given(channelRepository.existsById(channelId)).willReturn(true);
        Message msg1 = new Message("내용1", null, null, null);
        Message msg2 = new Message("내용2", null, null, null);
        List<Message> messages = List.of(msg1, msg2);

        given(messageRepository.findAllByChannelId(channelId)).willReturn(messages);
        List<MessageDto> result = messageService.findByChannelId(channelId);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("채널에 있는 메세지 조회 실패")
    void findByChannel_fail() {
        UUID channelId = UUID.randomUUID();
        given(channelRepository.existsById(channelId)).willReturn(false);
        assertThrows(ChannelNotFoundException.class, () -> messageService.findByChannelId(channelId));
        then(messageRepository).should(never()).findAllByChannelId(any());
    }
}
