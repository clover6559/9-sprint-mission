package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelRepo = new HashMap<>();

    public JCFChannelRepository() {
    }

    @Override
    public Channel save(Channel channel) {
        channelRepo.put(channel.getChannelId(),channel);
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

    public Channel update(UUID channelId, String channelName, String description) {
        Channel channel = findById(channelId)
                .orElseThrow(()-> new NoSuchElementException("수정할 채널이 없습니다."));
        channel.update(channelName, description);
        return channel;
    }

    @Override
    public void deleteById(UUID channelId) { if (channelRepo.get(channelId) == null) {
        System.out.println("실패 : 존재하지 않는 채널 Id 입니다");

    }
        channelRepo.remove(channelId);
        }

    }

