package service.jcf;

import entity.Channel;
import entity.User;
import service.ChannelService;
import service.UserService;
import service.serch.ChannelSearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Channel find(UUID channelId) {
        return channelData.get(channelId);
    }

    @Override
    public List<Channel> ChannelSearch(ChannelSearch channelSearch) {
        return channelData.values().stream()
                .filter(c-> {
                    String searchName = channelSearch.getUserName();
                    if (searchName == null) return true;
                    return searchName.equals(c.getUserName());
                    })
                .filter(c -> {
                    String searchChannel = channelSearch.getChannelName();
                    if (searchChannel == null) return true;
                    return searchChannel.equals(c.getChannelName());
                })
                .collect(Collectors.toList());

//                .filter(c -> channelSearch.getUserName() == null || c.getUserName().equals(channelSearch.getUserName()))
//                .filter(c -> channelSearch.getChannelName() == null || c.getChannelName().equals(channelSearch.getChannelName()))
//                .collect(Collectors.toList());
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
            return false;
        }
        channelData.remove(channelId);
        return true;
    }
}

