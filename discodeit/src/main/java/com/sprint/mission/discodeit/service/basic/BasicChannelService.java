package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.search.ChannelSearch;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
@Service
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
        if (channelSearch == null) {
            return findAll();
        }
        return channelRepository.findAll().stream()
                .filter(u -> {
                    String searchName = channelSearch.getUserName();
                    return searchName == null || searchName.equals(u.getUserName());
                })
                .filter(u -> {
                    String searchName = channelSearch.getChannelName();
                    return searchName == null || searchName.equals(u.getChannelName());
                })
                .toList();
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public String update(UUID channelId, String channelName, String description) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + "not found"));
        channel.update(channelName, description);
        return channelRepository.save(channel).update(channelName,description);
    }

    @Override
    public boolean delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + "not found");
        }
        channelRepository.deleteById(channelId);
        return true;
    }

    @Override
    public void printRemainChannel() {
        List<Channel> channels = channelRepository.findAll();
        if (channels.isEmpty()) {
            System.out.println("\n 남아있는 채널 정보가 존재하지 않습니다.");
        } else {
            System.out.println("현재 남은 채널 수: " + channels.size());
            channels.forEach(c -> System.out.println("- " + c.getChannelName()));
        }
    }
}