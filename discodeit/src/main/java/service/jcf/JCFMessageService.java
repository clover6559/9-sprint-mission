package service.jcf;

import entity.Channel;
import entity.Message;
import entity.User;
import service.ChannelService;
import service.MessageService;
import service.UserService;

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
        messageData.put(message.getMassageId(), message);
        return message;
    }

    @Override
    public Message find(UUID massageId) {
        return messageData.get(massageId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageData.values());
    }

    @Override
    public Message update(UUID massageId, String content) {
        Message foundMessage = messageData.get(massageId);
        if (foundMessage == null) {
            throw new IllegalArgumentException("존재하지 않는 메세지입니다.");
        } foundMessage.update(massageId, content);
        messageData.put(massageId,foundMessage);
        return foundMessage;
    }

    @Override
    public boolean delete(UUID massageId) {
        if (messageData.get(massageId) == null) {
            System.out.println("존재하지 않는 메세지입니다");
            return false;
        }
            {messageData.remove(massageId);
                System.out.println("성공: 메세지가 삭제되었습니다.");
                return true;
            }
        }

    }

