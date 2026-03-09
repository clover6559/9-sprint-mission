package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.request.MessageCreate;
import com.sprint.mission.discodeit.dto.request.MessageUpdate;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;
  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;


  @Transactional
  @Override
  public MessageDto create(MessageCreate messageCreate,
      List<BinaryContentCreate> binaryContentCreateRequests) {
    Channel channel = channelRepository.findById(messageCreate.channelId())
        .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));
    User user = userRepository.findById(messageCreate.authorId())
        .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(req -> {
          BinaryContent binaryContent = new BinaryContent(
              req.fileName(), (long) req.bytes().length, req.contentType());
          BinaryContent saved = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(saved.getId(), req.bytes());
          return saved;
        })
        .toList();
    Message message = new Message(
        messageCreate.content(), channel, user, attachments);
    Message savedMessage = messageRepository.save(message);
    return messageMapper.toDto(savedMessage);
  }

  @Override
  public MessageDto findById(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(() -> new RuntimeException("해당 메시지를 찾을 수 없습니다."));
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream()
        .map(messageMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdate request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("해당 메세지를 찾을 수 없습니다."));
    message.update(newContent);
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    Message findMessage = messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("해당 메세지를 찾을 수 없습니다."));
    messageRepository.delete(findMessage);
  }

  @Override
  public PageResponse<MessageDto> getMessages(Instant lastCreatedAt, int size) {
    Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
    Slice<Message> slice;
    if (lastCreatedAt == null) {
      slice = messageRepository.findSliceBy(pageable);
    } else {
      slice = messageRepository.findSliceByCreatedAtLessThan(lastCreatedAt, pageable);
    }
    long totalElements = messageRepository.count();
    return pageResponseMapper.fromSlice(
        slice.map(messageMapper::toDto), MessageDto::createdAt, totalElements);
  }
}
