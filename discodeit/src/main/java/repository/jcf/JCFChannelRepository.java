package repository.jcf;

import entity.Channel;
import repository.ChannelRepository;
import service.serch.ChannelSearch;

import java.util.*;

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
        return channelRepo.get(channelId);
    }

    @Override
    public List<Channel> channelSearch(ChannelSearch channelSearch) {
        return List.of();
    }

    @Override
    public List<Channel> findAll() {
        return channelRepo.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return false;
    }

//    @Override
//    public Channel updateChannel(UUID channelId, String channelName, String description) {
//        return channelRepo.get(channelId);
//    }

    @Override
    public boolean deleteById(UUID channelId) { if (channelRepo.get(channelId) == null) {
        System.out.println("실패 : 존재하지 않는 채널 Id 입니다");
        return false;
    }
        channelRepo.remove(channelId);
        return true;}

    }

