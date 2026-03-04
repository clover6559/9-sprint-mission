package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.message.MessageCreate;
import com.sprint.mission.discodeit.dto.message.MessageUpdate;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
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

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public Message create(MessageCreate messageCreate,
      List<BinaryContentCreate> binaryContentCreateRequests) {
    UUID channelId = messageCreate.channelId();
    UUID authorId = messageCreate.authorId();

    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
    }
    if (!userRepository.existsById(authorId)) {
      throw new NoSuchElementException("Author with id " + authorId + " does not exist");
    }

    List<UUID> attachmentIds = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          return createdBinaryContent.getId();
        })
        .toList();

    String content = messageCreate.content();
    Message message = new Message(
        content,
        channelId,
        authorId,
        attachmentIds
    );
    return messageRepository.save(message);
  }

  @Override
  public Message findById(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("해당 메시지를 찾을 수 없습니다."));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream().toList();
  }

  @Override
  public Message update(UUID messageId, MessageUpdate request) {
    String newContent = request.newContent();
    Message foundMessage = messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("해당 메세지를 찾을 수 없습니다."));
    foundMessage.update(newContent);
    return messageRepository.save(foundMessage);
  }

  @Override
  public void delete(UUID messageId) {
    Message findMessage = messageRepository.findById(messageId)
        .orElseThrow(() -> new RuntimeException("해당 메세지를 찾을 수 없습니다."));
    findMessage.getAttachmentIds()
        .forEach(binaryContentRepository::deleteById);
    messageRepository.deleteById(messageId);

  }

}
