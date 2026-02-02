package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.search.MessageSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String content, User user, Channel channel) {
        Message message = new Message(content, user, channel);
        return messageRepository.save(message);
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + "not found"));
    }

    @Override
    public List<Message> Search(MessageSearch messageSearch) {
        if (messageSearch == null) {
            return findAllByChannelId();
        }
        return messageRepository.findAll().stream()
                .filter(m -> {
                    String searchName = messageSearch.getUserName();
                    return searchName == null || searchName.equals(m.getUserName());
                })
                .filter(m -> {
                    String searchName = messageSearch.getChannelName();
                    return searchName == null || searchName.equals(m.getChannelName());
                })
                .toList();
    }

    @Override
    public List<Message> findAllByChannelId() {
        return messageRepository.findAll();
    }

    @Override
    public String update(UUID messageId, String content) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + "not found"));
        message.update(content);
        messageRepository.save(message);
        return content;
    }

    @Override
    public boolean delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + "not found");
        }
        messageRepository.deleteById(messageId);
        return true;
    }

    @Override
    public void printRemainMessages() {
        List<Message> messages = messageRepository.findAll();
        if (messages.isEmpty()) {
            System.out.println("\n 남아있는 메세지 정보가 존재하지 않습니다.");
        } else {
            System.out.println("현재 남은 메세지 수: " + messages.size());
            messages.forEach(m -> System.out.println("- " + m.getContent()));
        }
    }
}
