package repository;

import entity.Channel;
import service.serch.ChannelSearch;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Optional<Channel> findById(UUID channelId);
    List<Channel> channelSearch(ChannelSearch channelSearch);
    List<Channel> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID channelId);
}
