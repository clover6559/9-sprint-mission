package service;

import entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(Message message);

    Message find(UUID id);

    List<Message> findAll();

    void update(Message message);

    boolean delete(Message message);

}
