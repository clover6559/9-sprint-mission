package basic;

import entity.Channel;
import entity.Message;
import entity.User;
import repository.MessageRepository;
import service.MessageService;
import service.serch.MessageSearch;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

public BasicMessageService(MessageRepository messageRepository) {
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
    public List<Message> Search(MessageSearch condition) {
        return List.of();
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String content) {
    Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + "not found"));
    message.update(content);
    return messageRepository.save(message);
    }

    @Override
    public boolean delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + "not found");
        }
        messageRepository.deleteById(messageId);
        return true;
    }
}
