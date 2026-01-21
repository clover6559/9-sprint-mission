package basic;

import entity.Channel;
import entity.User;
import repository.ChannelRepository;
import service.ChannelService;
import service.serch.ChannelSearch;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

public BasicChannelService(ChannelRepository channelRepository) {
    this.channelRepository = channelRepository;
}

    @Override
    public Channel create(Channel.ChannelType channelType, String channelName, String description, User user) {
       Channel channel = new Channel(channelType, channelName,description, user);
        return channelRepository.save(channel);
    }

    @Override
    public Channel findCById(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(()-> new NoSuchElementException("수정할 채널이 없습니다."));
    }

    @Override
    public List<Channel> Search(ChannelSearch channelSearch) {
        return List.of();
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String channelName, String description) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + "not found"));
        channel.update(channelName, description);
        return channelRepository.save(channel);
    }

    @Override
    public boolean delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + "not found");
        }
        channelRepository.deleteById(channelId);
        return true;
    }
}