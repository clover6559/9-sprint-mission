package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.serch.MessageSearch;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageData = new HashMap<>();

    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, User user, Channel channel) {
        Message message = new Message(content, user, channel);
        messageData.put(message.getMessageId(), message);
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return messageData.get(messageId);
    }

    @Override
    public List<Message> Search(MessageSearch messageSearch) {
        return messageData.values().stream()
                .filter(m-> messageSearch.getUserName() == null || messageSearch.getUserName().equals(m.getUserName()))
                .filter(m -> messageSearch.getChannelName() == null || messageSearch.getChannelName().equals(m.getChannelName()))
                .toList();
    }

    @Override
    public List<Message> findAll() {
        return messageData.values().stream().toList();
    }


    @Override
    public String update(UUID messageId, String content) {
        Message foundMessage = messageData.get(messageId);
        if (foundMessage == null) {
            throw new IllegalArgumentException("존재하지 않는 메세지입니다.");
        } foundMessage.update(content);
        messageData.put(messageId,foundMessage);
        return foundMessage.update(content);
    }

    @Override
    public boolean delete(UUID messageId) {
        if (messageData.get(messageId) == null) {
            System.out.println("실패 : 존재하지 않는 메세지 Id 입니다");
            return false;
        }
            {messageData.remove(messageId);
                return true;
            }
        }

    @Override
    public void printRemainMessages() {
        List<Message> messages = findAll();
        System.out.println("현재 남은 메세지 수: " + messages.size());
        messages.forEach(m -> System.out.println("- " + m.getContent()));
    }

}

