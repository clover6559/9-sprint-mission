package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdate;
import com.sprint.mission.discodeit.dto.channel.CreatePrivate;
import com.sprint.mission.discodeit.dto.channel.CreatePublic;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.search.ChannelSearch;

import java.time.Instant;
import java.util.*;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    public JCFChannelService(ChannelRepository channelRepository, ReadStatusRepository readStatusRepository,MessageRepository messageRepository) {
        this.channelRepository = channelRepository;
        this.readStatusRepository = readStatusRepository;
        this.messageRepository = messageRepository;
}

    public Channel create(CreatePublic createPublic) {
        Channel channel = new Channel(createPublic);
        channelRepository.save(channel);

        User member = createPublic.user();
        if (member != null) {
            ReadStatus readStatus = new ReadStatus(member,channel);
            readStatusRepository.save(readStatus);
        }
       return channel;
    }
    public Channel create(CreatePrivate createPrivate) {
        Channel channel = new Channel(createPrivate);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public ChannelResponse findCById(UUID channelId) {
        Channel channel =  channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다. "));
        messageRepository.findByChannelId(channelId);
        Instant lastMessageAt = messageRepository.findByChannelId(channelId).stream()
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);


        List<UUID> memberIds = new ArrayList<>();
        if (channel.getChannelType() == Channel.ChannelType.PRIVATE) {
            memberIds =  readStatusRepository.findByChannelId(channelId).stream()
                    .map(ReadStatus::getUserId).toList();
        }
        return new ChannelResponse(channel, lastMessageAt, memberIds);

    }

    @Override
    public List<Channel> Search(ChannelSearch channelSearch) {
        return channelRepository.findAll().stream()
                .filter(c -> channelSearch.getUserName() == null || channelSearch.getUserName().equals(c.getUserName()))
                .filter(c -> channelSearch.getChannelName() == null || channelSearch.getChannelName().equals(c.getChannelName()))
                .toList();
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel -> {
                            if (channel.getChannelType() == Channel.ChannelType.PUBLIC)
                                return true;
                            if (channel.getChannelType() == Channel.ChannelType.PRIVATE)
                                return readStatusRepository.existsByChannelIdAndUserId(channel.getChannelId(), userId);
                        return false;
                        })
                        .map(channel -> {
                            UUID currentId = channel.getChannelId();
                            Instant lastMessageAt = messageRepository.findByChannelId(currentId).stream()
                                    .map(Message::getCreatedAt)
                                    .max(Comparator.naturalOrder())
                                    .orElse(null);
                            List<UUID> memberIds = new ArrayList<>();
                            if (channel.getChannelType() == Channel.ChannelType.PRIVATE) {
                                memberIds =  readStatusRepository.findByChannelId(currentId).stream()
                                        .map(ReadStatus::getUserId).toList();
                            }
                            return new ChannelResponse(channel, lastMessageAt, memberIds);
                        })
                .toList();
                }

    @Override
    public String update(ChannelUpdate updateChannel) {
        Channel foundChannel = channelRepository.findById(updateChannel.targetId())
                .orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다."));
        if (foundChannel.getChannelType() == Channel.ChannelType.PRIVATE) {
            throw new IllegalArgumentException("수정이 불가능한 채널입니다");
        }
        String changes = foundChannel.changes(updateChannel.channelUpdateInfo());
             channelRepository.save(foundChannel);
        return changes;
    }


    @Override
    public boolean delete(UUID channelId) {
        Channel findChannel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("해당 채널을를 찾을 수 없습니다."));
        messageRepository.deleteByChannelId(findChannel.getChannelId());
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.deleteById(channelId);
        return true;

    }

    @Override
    public void printRemainChannel() {
        List<Channel> channels = channelRepository.findAll();
        System.out.println("현재 남은 채널 수: " + channels.size());
        channels.forEach(c -> System.out.println("- " + c.getChannelName()));
    }
}

