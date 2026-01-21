package basic;

import entity.Message;
import repository.MessageRepository;
import service.serch.MessageSearch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicMessageService implements MessageRepository {
    @Override
    public Message save(Message message) {
        return null;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return null;
    }

    @Override
    public List<Message> MessageSearch(MessageSearch condition) {
        return List.of();
    }

    @Override
    public List<Message> findAll() {
        return List.of();
    }

    @Override
    public Message updateMessage(UUID messageId, String content) {
        return null;
    }

    @Override
    public boolean deleteById(UUID messageId) {
        return false;
    }
}
