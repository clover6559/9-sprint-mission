package repository.jcf;

import entity.Message;
import repository.MessageRepository;
import service.serch.MessageSearch;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageRepo = new HashMap<>();
    public JCFMessageRepository(){
    }

    @Override
    public Message save(Message message) {
        messageRepo.put(message.getMessageId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return messageRepo.get(messageId);
    }

    @Override
    public List<Message> MessageSearch(MessageSearch condition) {
        return List.of();
    }

    @Override
    public List<Message> findAll() {
        return messageRepo.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return false;
    }

//    @Override
//    public Message updateMessage(UUID messageId, String content) {
//        return messageRepo.get(messageId);
//    }

    @Override
    public boolean deleteById(UUID messageId) { if (messageRepo.get(messageId) == null) {
        System.out.println("실패 : 존재하지 않는 채널 Id 입니다");
        return false;
    }
        messageRepo.remove(messageId);
        return true;}
}
