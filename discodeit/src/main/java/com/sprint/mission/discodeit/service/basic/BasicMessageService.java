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
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.Message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final PageResponseMapper pageResponseMapper;
  private final MessageMapper messageMapper;


  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequestRequests) {
    log.info("메세지 생성 요청 - 채널 ID: {}, 유저 ID: {}, 내용: {}", messageCreateRequest.channelId(),
        messageCreateRequest.authorId(), messageCreateRequest.content());
    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> {
          log.warn("없는 채널 ID로 메세지 생성 실페 - 채널 ID: {}", messageCreateRequest.channelId());
          return new ChannelNotFoundException(messageCreateRequest.channelId());
        });
    User user = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> {
          log.warn("없는 유저 ID로 메세지 생성 실페 - 유저 ID: {}", messageCreateRequest.authorId());
          return new UserNotFoundException(messageCreateRequest.authorId());
        });

    List<BinaryContent> attachments = binaryContentCreateRequestRequests.stream()
        .map(req -> {
          BinaryContent binaryContent = new BinaryContent(
              req.fileName(), (long) req.bytes().length, req.contentType());
          log.info("첨부파일 등록 요청 - 파일 이름 : {}. 파일 타입 : {}, 파일 용량: {} bytes", req.fileName(),
              req.contentType(), req.bytes().length);
          BinaryContent saved = binaryContentRepository.save(binaryContent);
          log.info("첨부파일 등록 성공 - 파일 ID: {}, 파일 이름: {}", saved.getId(), binaryContent.getFileName());
          binaryContentStorage.put(saved.getId(), req.bytes());
          return saved;
        })
        .toList();
    Message message = new Message(messageCreateRequest.content(), channel, user, attachments);
    Message savedMessage = messageRepository.save(message);
    log.info("메세지 생성 성공 - 메세지 ID: {}", message.getId());
    return messageMapper.toDto(savedMessage);
  }

  @Override
  public MessageDto findById(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(() -> new MessageNotFoundException(messageId));
  }

  @Override
  public List<MessageDto> findByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new ChannelNotFoundException(channelId);
    }
    return messageRepository.findAllByChannelId(channelId).stream()
        .map(messageMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    log.info("메세지 업데이트 요청 - 메세지 ID: {}, 변경할 내용: {}", messageId, request.newContent());
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> {
          log.warn("없는 메세지 ID로 업데이트 실패 - 메세지 ID: {}", messageId);
          return new MessageNotFoundException(messageId);
        });
    message.update(newContent);
    log.info("메세지 업데이트 성공 - 메세지 ID: {}", messageId);
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    log.info("메세지 삭제 요청 - 메세지 ID: {}", messageId);
    if (!messageRepository.existsById(messageId)) {
      throw new MessageNotFoundException(messageId);
    }
    messageRepository.deleteById(messageId);
    log.info("메세지 삭제 성공 - 메세지 ID: {}", messageId);

  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(createAt).orElse(Instant.now()),
            pageable)
        .map(messageMapper::toDto);

    Instant nextCursor = null;
    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1)
          .createdAt();
    }

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }
}
