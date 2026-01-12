package service.jcf;

import entity.Channel;
import service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelData = new HashMap<>();

    @Override
    public void addChannel(Channel channel) {
       channelData.put(channel.getId(), channel);
    }

    @Override
    public Channel findChenel(UUID channelId) {
        return channelData.get(channelId);
    }

    @Override
    public List<Channel> findChannel() {
        return channelData.values().stream().toList();
    }

    @Override
    public void updateChannel(Channel channel) {
        Channel foundChannel = channelData.get(channel.getChannelId());
        if (foundChannel == null) {
            return;
        }
        foundChannel.updateChannel(channel.getTopic(), channel.getContent());
        channelData.put(channel.getChannelId(), foundChannel);
    }

    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelData.get(channelId) == null) {
            return false;
        }
        channelData.remove(channelId);
        return true;
    }
}

