package repository;

import entity.Channel;
import service.serch.ChannelSearch;


import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Channel findById(UUID channelId);
    List<Channel> channelSearch(ChannelSearch channelSearch);
    List<Channel> findAll();
    Channel updateChannel(UUID channelId, String channelName, String description);
    //삭제
    boolean deleteById(UUID channelId);
}
