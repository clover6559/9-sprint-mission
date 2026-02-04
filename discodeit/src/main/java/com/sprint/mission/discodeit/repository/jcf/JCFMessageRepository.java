package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type",havingValue = "jcf", matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageRepo = new HashMap<>();
    public JCFMessageRepository(){
    }

    @Override
    public Message save(Message message) {
        messageRepo.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageRepo.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        return messageRepo.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID messageId) {
        return messageRepo.containsKey(messageId);
    }


    @Override
    public void deleteById(UUID messageId) {
        messageRepo.remove(messageId);
        }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return messageRepo.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        messageRepo.values().removeIf(message -> message.getChannelId().equals(channelId));
    }
}
