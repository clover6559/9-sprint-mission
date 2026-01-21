package basic;

import entity.Channel;
import repository.ChannelRepository;
import service.serch.ChannelSearch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelRepository {

    @Override
    public Channel save(Channel channel) {
        return null;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return null;
    }

    @Override
    public List<Channel> channelSearch(ChannelSearch channelSearch) {
        return List.of();
    }

    @Override
    public List<Channel> findAll() {
        return List.of();
    }

    @Override
    public Channel updateChannel(UUID channelId, String channelName, String description) {
        return null;
    }

    @Override
    public boolean deleteById(UUID channelId) {
        return false;
    }
}
