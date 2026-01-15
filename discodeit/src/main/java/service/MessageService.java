package service;

import entity.Channel;
import entity.Message;
import entity.User;
import service.serch.MessageSearch;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, User user, Channel channel);

    Message find(UUID massageId);

    List<Message> MessageSearch(MessageSearch condition);

    Message update(UUID massageId, String content);

    boolean delete(UUID massageId);
}