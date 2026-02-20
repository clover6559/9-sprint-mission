package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.message.MessageCreate;
import com.sprint.mission.discodeit.dto.message.MessageUpdate;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.search.MessageSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.entity.DateUtil.formatTime;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreate messageCreate, BinaryContentCreate binaryCreate) {
        List<UUID> attachmentIds = new ArrayList<>();
        if (binaryCreate != null && binaryCreate.bytes() != null) {
            long fileSize = (long) binaryCreate.bytes().length;

            BinaryContent binaryContent = new BinaryContent(
                    binaryCreate.fileName(),
                    fileSize,
                    binaryCreate.contentType(),
                    binaryCreate.bytes()
            );
            BinaryContent savedContent = binaryContentRepository.save(binaryContent);
            attachmentIds.add(savedContent.getId());
        }

        Message message = new Message(
                messageCreate.content(),
                messageCreate.channelId(),
                messageCreate.authorId(),
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
    public List<Message> search(MessageSearch messageSearch) {
        return messageRepository.findAll().stream()
                .filter(m -> {
                    if (messageSearch.getUserName() == null) return true;
                    return userRepository.findById(m.getAuthordId())
                            .map(u -> u.getUserName().equals(messageSearch.getUserName()))
                            .orElse(false);
                })
                .filter(m -> {
                    if (messageSearch.getChannelName() == null) return true;
                    return channelRepository.findById(m.getChannelId())
                            .map(c -> c.getChannelName().equals(messageSearch.getChannelName()))
                            .orElse(false);
                })
                .toList();
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findByChannelId(channelId);
    }


    @Override
    public void update(UUID messageId,MessageUpdate MessageUpdate) {
        Message foundMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("해당 메세지를 찾을 수 없습니다."));
        foundMessage.updateInfo(MessageUpdate.content());
        messageRepository.save(foundMessage);
    }

    @Override
    public boolean delete(UUID messageId) {
        Message findMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("해당 메세지를 찾을 수 없습니다."));
        binaryContentRepository.deleteByRefId(findMessage.getId());
        messageRepository.deleteById(messageId);
        return true;
    }

    @Override
    public void printRemainMessages() {
        List<Message> messages = messageRepository.findAll();
        System.out.println("현재 남은 메세지 수: " + messages.size());
        messages.forEach(m -> System.out.println("- " + m.getContent()));
    }

    @Override
    public String formatMessage(Message message) {
        String userName = userRepository.findById(message.getAuthordId()).map(User::getUserName).orElse("알 수 없는 유저");
        String channelName = channelRepository.findById(message.getChannelId()).map(Channel::getChannelName).orElse("알 수 없는 채널");
        String attachments = binaryContentRepository.findAllByIdIn(message.getAttachmentIds()).stream()
                .map(BinaryContent::getFileName)
                .collect(Collectors.joining(", "));

        return "유저이름: " + userName + "\n" +
               "채널이름: " + channelName + "\n" +
               "내용: " + message.getContent() + "\n" +
               "첨부파일: " + (attachments.isEmpty() ? "없음" : attachments) + "\n" +
               "생성시간: " + formatTime(message.getCreatedAt()) + "\n" +
               "수정시간: " + formatTime(message.getUpdatedAt());
    }

}
