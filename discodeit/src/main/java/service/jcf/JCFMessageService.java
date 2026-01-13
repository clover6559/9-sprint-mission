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
    public Message create(String content, UUID channelId, UUID userId) {
        Message message = new Message(content, channelId, userId);
        messageData.put(message.getMassageId(), message);
        return message;
    }

    @Override
    public Message find(UUID massageId) {
        return messageData.get(massageId);
    }

    @Override
    public List<Message> findAll() {
        return messageData.values();
    }

    @Override
    public Message update(UUID massageId, String content) {

    }

    @Override
    public boolean delete(UUID massageId) {
        if (messageData.get(massageId == null) {
            return false;}
            {
                messageData.remove(massageId);
                return true;
            }
        }
    }

