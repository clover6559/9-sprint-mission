package service;

import entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    void create(Channel channel);

    Channel find(UUID channelId);

    List<Channel> findAll();

    void update(Channel channel);

    boolean delete(UUID channelId);
}
