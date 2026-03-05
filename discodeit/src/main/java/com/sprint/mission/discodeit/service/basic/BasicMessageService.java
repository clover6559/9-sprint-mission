package com.sprint.mission.discodeit.service.basic;

import static java.util.stream.Collectors.toList;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.request.MessageCreate;
import com.sprint.mission.discodeit.dto.request.MessageUpdate;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
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

  @Transactional
  @Override
  public Message create(MessageCreate messageCreate,
      List<BinaryContentCreate> binaryContentCreateRequests) {
    Channel channel = channelRepository.findById(messageCreate.channelId())
        .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));
    User user = userRepository.findById(messageCreate.authorId())
        .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

    List<BinaryContent> attachmentIds = binaryContentCreateRequests.stream()
        .map(req -> new BinaryContent(req.fileName(), (long) req.bytes().length, req.contentType()))
        .toList();

    String content = messageCreate.content();
    Message message = new Message(content, channel, user, attachmentIds);
    return messageRepository.save(message);
  }

  @Override
  public Message findById(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("해당 메시지를 찾을 수 없습니다."));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId);
  }

  @Transactional
  @Override
  public Message update(UUID messageId, MessageUpdate request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("해당 메세지를 찾을 수 없습니다."));
    message.update(newContent);
    return message;
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    Message findMessage = messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("해당 메세지를 찾을 수 없습니다."));
    messageRepository.delete(findMessage);

  }

}
