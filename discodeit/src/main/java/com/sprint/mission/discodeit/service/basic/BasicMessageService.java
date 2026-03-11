package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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


  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequestRequests) {
    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));
    User user = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

    List<BinaryContent> attachments = binaryContentCreateRequestRequests.stream()
        .map(req -> {
          BinaryContent binaryContent = new BinaryContent(
              req.fileName(), (long) req.bytes().length, req.contentType());
          BinaryContent saved = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(saved.getId(), req.bytes());
          return saved;
        })
        .toList();
    Message message = new Message(messageCreateRequest.content(), channel, user, attachments);
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
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
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
  public PageResponse<MessageDto> getMessages(UUID channelId, Instant cursor, Pageable pageable) {
    Slice<Message> messageSlice;
    if (cursor == null) {
      messageSlice = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable);
    } else {
      messageSlice = messageRepository.findByChannelIdAndCreatedAtBeforeOrderByCreatedAtDesc(
          channelId, cursor, pageable);
    }
    List<MessageDto> content = messageSlice.getContent().stream()
        .map(messageMapper::toDto)
        .toList();
    Instant nextCursor = null;
    if (messageSlice.hasNext() && !content.isEmpty()) {
      nextCursor = content.get(content.size() - 1).createdAt();
    }
    return new PageResponse<>(
        content,
        nextCursor,
        messageSlice.getSize(),
        messageSlice.hasNext(),
        null);
  }

}
