package repository;


import entity.Message;
import service.serch.MessageSearch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    Optional<Message> findById(UUID messageId);

    List<Message> MessageSearch(MessageSearch condition);

    List<Message> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID messageId);
}
