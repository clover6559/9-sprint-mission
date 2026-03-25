package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(CreatePublicChannelRequest request) {
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    Channel savedChannel = channelRepository.save(channel);
    return channelMapper.toDto(savedChannel);
  }

  @Transactional
  @Override
  public ChannelDto create(CreatePrivateChannelRequest request) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);
    List<ReadStatus> readStatuses = request.participantIds().stream()
        .map(userId -> {
          User participant = userRepository.findById(userId)
              .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
          return new ReadStatus(participant, createdChannel, Instant.MIN);
        })
        .toList();
    readStatusRepository.saveAll(readStatuses);
    return channelMapper.toDto(createdChannel);

  }

  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다. "));
  }


  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();
    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("Private channel cannot be updated");
    }
    channel.update(newName, newDescription);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    channelRepository.deleteById(channelId);
  }

}