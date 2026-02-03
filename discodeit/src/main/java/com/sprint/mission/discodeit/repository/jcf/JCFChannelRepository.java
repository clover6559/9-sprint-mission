package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreatePrivate;
import com.sprint.mission.discodeit.dto.channel.CreatePublic;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    public JCFChannelService(ChannelRepository channelRepository, ReadStatusRepository readStatusRepository,MessageRepository messageRepository) {
        this.channelRepository = channelRepository;
        this.readStatusRepository = readStatusRepository;
        this.messageRepository = messageRepository;
    }
    @Override
    public Channel create(CreatePublic createPublic) {
        Channel channel = new Channel(createPublic);
        channelRepository.save(channel);

        User member = createPublic.user();
        if (member != null) {
            ReadStatus readStatus = new ReadStatus(channel.getChannelId(), member.getUserId());
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