package service;

import entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, UUID channelId, UUID userId);

    Message find(UUID massageId);

    List<Message> findAll();

    void update(UUID massageId, String content);

    boolean delete(UUID massageId);


}
