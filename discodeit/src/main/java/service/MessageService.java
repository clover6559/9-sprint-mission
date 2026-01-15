package service;

import entity.Channel;
import entity.Message;
import entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, User user, Channel channel);

    Message find(UUID massageId);

    List<Message> findAll();

    Message update(UUID massageId, String content);

    boolean delete(UUID massageId);
}