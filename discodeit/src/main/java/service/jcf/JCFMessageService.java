package service.jcf;

import entity.Message;
import service.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageData = new HashMap<>();


    @Override
    public void addMessage(Message message) {
        messageData.put(message.getChannelId(), message);
    }

    @Override
    public Message findMessage(UUID id) {
        return null;
    }

    @Override
    public List<Message> getAllMessage() {
        return List.of();
    }

    @Override
    public void updateMessage(Message message) {

    }

    @Override
    public boolean deleteMessage(Message message) {
        return false;
    }
}
