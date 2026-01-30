package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.search.ChannelSearch;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelData = new HashMap<>();
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.userService = userService;
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
    public String update(UUID channelId, String channelName, String description) {
        Channel foundChannel = channelData.get(channelId);
        if (foundChannel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: ");
        }
        foundChannel.update(channelName, description);
        channelData.put(channelId, foundChannel);
        return foundChannel.update(channelName, description);
    }

    @Override
    public boolean delete(UUID channelId) {
        if (channelData.get(channelId) == null) {
            throw new IllegalArgumentException("실패 : 존재하지 않는 채널 Id 입니다");
        }
        channelData.remove(channelId);
        return true;
    }

    @Override
    public void printRemainChannel() {
        List<Channel> channels = findAll();
        System.out.println("현재 남은 채널 수: " + channels.size());
        channels.forEach(c -> System.out.println("- " + c.getChannelName()));
    }
}

