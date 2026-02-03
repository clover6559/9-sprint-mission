package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelRepo = new HashMap<>();

    public JCFChannelRepository() {
    }

    @Override
    public Channel save(Channel channel) {
        channelRepo.put(channel.getChannelId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channelRepo.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepo.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID channelId) {
        return channelRepo.containsKey(channelId);
    }

    @Override
    public void deleteById(UUID channelId) {
        channelRepo.remove(channelId);

    }
}

