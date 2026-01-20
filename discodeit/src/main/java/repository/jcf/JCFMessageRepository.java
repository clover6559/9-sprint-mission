package repository.jcf;

import entity.Channel;
import entity.Message;
import entity.User;
import repository.MessageRepository;
import service.serch.MessageSearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageRepo = new HashMap<>();
    public JCFMessageRepository(){
    }

    @Override
    public Message save(String content, User user, Channel channel) {
        return null;
    }

    @Override
    public Message findById(UUID massageId) {
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
    public Message updateMessage(UUID massageId, String content) {
        return null;
    }

    @Override
    public boolean deleteById(UUID massageId) {
        return false;
    }
}
