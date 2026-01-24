package service.jcf;

import entity.Channel;
import entity.User;
import service.ChannelService;
import service.UserService;
import service.serch.ChannelSearch;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelData = new HashMap<>();
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.userService = userService;
//        List<User> users = userService.findAll();
//        channels.add(new Channel(Channel.ChannelType.PUBLIC, "질문", "질문있어요", users));
//        channels.add(new Channel(Channel.ChannelType.PRIVATE, "5조", "5조방입니다", users));

    }

    @Override
    public Channel create(Channel.ChannelType channelType, String channelName, String description, User user) {
        Channel channel = new Channel(channelType, channelName, description, user);
        channelData.put(channel.getChannelId(), channel);
       return channel;
    }

    @Override
    public Channel findCById(UUID channelId) {
        return channelData.get(channelId);
    }

    @Override
    public List<Channel> Search(ChannelSearch channelSearch) {
        return channelData.values().stream()
                .filter(c -> channelSearch.getUserName() == null || channelSearch.getUserName().equals(c.getUserName()))
                .filter(c -> channelSearch.getChannelName() == null || channelSearch.getChannelName().equals(c.getChannelName()))
                .toList();
    }

    @Override
    public List<Channel> findAll() {
        return channelData.values().stream().toList();
    }

    @Override
    public Channel update(UUID channelId, String channelName, String description) {
        Channel foundChannel = channelData.get(channelId);
        if (foundChannel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: ");
        }
        foundChannel.update(channelName, description);
        channelData.put(channelId, foundChannel);
        return foundChannel;
    }

    @Override
    public boolean delete(UUID channelId) {
        if (channelData.get(channelId) == null) {
            throw new IllegalArgumentException("실패 : 존재하지 않는 채널 Id 입니다");
        }
        channelData.remove(channelId);
        return true;
    }
}

