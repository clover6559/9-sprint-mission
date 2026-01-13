package service;

import entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(Channel.ChannelType channelType,String channelName, String description);

    Channel find(UUID channelId);

    List<Channel> findAll();

    Channel update(UUID channelId, String channelName, String description);

    boolean delete(UUID channelId);
}
