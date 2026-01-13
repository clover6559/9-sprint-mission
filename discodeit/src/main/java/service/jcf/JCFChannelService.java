package service.jcf;

import entity.Channel;
import entity.User;
import service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelData = new HashMap<>();

    @Override
    public Channel create(Channel.ChannelType channelType, String channelName, String description) {
       Channel channel = new Channel(channelType, channelName, description)
        channelData.put(channel.getChannelId(), channel);
       return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        return channelData.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return channelData.values().stream().toList();
    }

    @Override
    public void update(UUID channelId, String channelName, String description) {
        Channel foundChannel = channelData.get(channelId);
        if (foundChannel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널 아이디입니다: " + channelId);
        }
        foundChannel.update(channelName, description);
        channelData.put(channelId, foundChannel);
        System.out.println("성공: 채넗 정보가 업데이트되었습니다.");
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

