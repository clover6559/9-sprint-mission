package service;

import entity.Channel;
import entity.User;
import service.serch.ChannelSearch;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(Channel.ChannelType channelType,String channelName, String description, User user);

    Channel find(UUID channelId);

    List<Channel> ChannelSerch(ChannelSearch channelSearch);

    List<Channel> findAll();

    Channel update(UUID channelId, String channelName, String description);

    boolean delete(UUID channelId);
}
