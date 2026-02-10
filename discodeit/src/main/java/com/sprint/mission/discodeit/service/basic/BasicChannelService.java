package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.user.UserFind;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.search.ChannelSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel create(CreatePublic createPublic) {
        Channel channel = new Channel(createPublic);
        channelRepository.save(channel);

        UserFind member = createPublic.user();
        if (member != null) {
            ReadStatus readStatus = new ReadStatus(channel.getId(), member.userId());
            readStatusRepository.save(readStatus);
        }
        return channel;
    }

    @Override
    public Channel create(CreatePrivate createPrivate) {
        Channel channel = new Channel(createPrivate);
        channelRepository.save(channel);

        UserFind member = createPrivate.user();
        if (member != null) {
            ReadStatus readStatus = new ReadStatus(channel.getId(), member.userId());
            readStatusRepository.save(readStatus);
        }
        return channel;
    }

    @Override
    public ChannelFind find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다. "));
        messageRepository.findByChannelId(channelId);
        Instant lastMessageAt = messageRepository.findByChannelId(channelId).stream()
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        List<UUID> memberIds = new ArrayList<>();
        if (channel.getChannelType() == Channel.ChannelType.PRIVATE) {
            memberIds = readStatusRepository.findByChannelId(channelId).stream()
                    .map(ReadStatus::getUserId).toList();
        }
        return new ChannelFind(channel, lastMessageAt, memberIds);

    }

    @Override
    public List<Channel> search(ChannelSearch search) {
        return channelRepository.findAll().stream()
                .filter(c -> search.getUserName() == null || search.getUserName().equals(c.getUserName()))
                .filter(c -> search.getChannelType() == null || c.getChannelType().name().equals(search.getChannelType().name()))
                .filter(c -> search.getChannelName() == null || c.getChannelName().contains(search.getChannelName()))
                .toList();
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel -> {
                    if (channel.getChannelType() == Channel.ChannelType.PUBLIC)
                        return true;
                    if (channel.getChannelType() == Channel.ChannelType.PRIVATE)
                        return readStatusRepository.existsByChannelIdAndUserId(channel.getId(), userId);
                    return false;
                })
                .map(channel -> {
                    UUID currentId = channel.getId();
                    Instant lastMessageAt = messageRepository.findByChannelId(currentId).stream()
                            .map(Message::getCreatedAt)
                            .max(Comparator.naturalOrder())
                            .orElse(null);
                    List<UUID> memberIds = new ArrayList<>();
                    if (channel.getChannelType() == Channel.ChannelType.PRIVATE) {
                        memberIds = readStatusRepository.findByChannelId(currentId).stream()
                                .map(ReadStatus::getUserId).toList();
                    }
                    return new ChannelResponse(channel, lastMessageAt, memberIds);
                })
                .toList();
    }

    @Override
    public void update(ChannelUpdate channelUpdateChannel) {
        Channel foundChannel = channelRepository.findById(channelUpdateChannel.targetId())
                .orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다."));
        if (foundChannel.getChannelType() == Channel.ChannelType.PRIVATE) {
            throw new IllegalArgumentException("수정이 불가능한 채널입니다");
        }
        foundChannel.updateInfo(channelUpdateChannel.channelUpdateInfo());
        channelRepository.save(foundChannel);
    }


    @Override
    public boolean delete(UUID channelId) {
        Channel findChannel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("해당 채널을를 찾을 수 없습니다."));
        messageRepository.deleteByChannelId(findChannel.getId());
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
//
//    private ChannelDto toDto(Channel channel) {
//        Instant lastMessageAt = messageRepository.findByChannelId(channel.getId())
//                .stream()
//                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
//                .map(Message::getCreatedAt)
//                .limit(1)
//                .findFirst()
//                .orElse(Instant.MIN);
//        List<UUID> participantIds = new ArrayList<>();
//        if (channel.getChannelType().equals(Channel.ChannelType.PRIVATE)) {
//            readStatusRepository.findByChannelId(channel.getId())
//                    .stream()
//                    .map(ReadStatus::getUserId)
//                    .forEach(participantIds::add);
//        }
//
//        return new ChannelDto(
//                channel.getId(),
//                channel.getChannelType(),
//                channel.getChannelName(),
//                channel.getDescription(),
//                participantIds,
//                lastMessageAt
//        );
//    }
}