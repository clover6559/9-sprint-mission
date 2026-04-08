package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Channel.ChannelAlreadyExistException;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.Channel.EmptyParticipantListException;
import com.sprint.mission.discodeit.exception.Channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(CreatePublicChannelRequest request) {
    String name = request.name();
    String description = request.description();
    if (channelRepository.existsByName(request.name())) {
      throw new ChannelAlreadyExistException("이미 존재하는 채널 이름입니다: ");
    }
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    log.info(
        "공개 채널 생성 요청 - 채널 타입: {}, 채널 ID: {}, 채널 이름:{}, 채널 설명: {}",
        channel.getType(),
        channel.getId(),
        name,
        description);

    Channel savedChannel = channelRepository.save(channel);
    log.info("채널 생성 성공 - 채널 타입: {}, 채널 ID: {}, 채널 이름: {}", channel.getType(), channel.getId(),
        name);
    return channelMapper.toDto(savedChannel);
  }

  @Transactional
  @Override
  public ChannelDto create(CreatePrivateChannelRequest request) {
    log.info("비공개 채널 생성 요청 - 참여자 수: {}", request.participantIds().size());
    log.debug("채널 참여자 ID 목록: {}", request.participantIds());
    if (request.participantIds() == null || request.participantIds().isEmpty()) {
      throw new EmptyParticipantListException(ChannelType.PRIVATE);
    }
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    log.info("채널 생성 요청 - 채널 타입: {}, 채널 ID: {}", channel.getType(), channel.getId());

    Channel createdChannel = channelRepository.save(channel);
    log.info("채널 생성 성공 - 채널 타입: {}, 채널 ID: {}", channel.getType(), channel.getId());
    List<ReadStatus> readStatuses = request.participantIds().stream()
        .map(userId -> {
          User participant = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("참여자 조회 실패 - 유저 ID: {}", userId);
            return new UserNotFoundException(userId);
          });
          log.info("읽음 상태 생성 - 참여자 ID: {}, 채널 ID: {}", participant.getId(), createdChannel.getId());
          return new ReadStatus(participant, createdChannel, Instant.now());
        })
        .toList();
    readStatusRepository.saveAll(readStatuses);
    return channelMapper.toDto(createdChannel);
  }

  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository
        .findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();
    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    log.info("채널 업데이트 요청 - 채널 ID: {}, 변경할 이름: {}, 변경할 내용: {}", channelId, newName, newDescription);
    Channel channel = channelRepository.findById(channelId).orElseThrow(() -> {
      log.warn("존재하지 않는 채널로 업데이트 실패 - 채널 ID: {}", channelId);
      return new ChannelNotFoundException(channelId);
    });
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      log.warn("비공개 채널은 업데이트가 불가능합니다.");
      throw new PrivateChannelUpdateException(ChannelType.PRIVATE);
    }
    channel.update(newName, newDescription);
    channelRepository.save(channel);
    log.info("채널 업데이트 성공 - 채널 ID: {}, 변경한 이름: {}, 변경한 내용: {}", channelId, newName, newDescription);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    log.info("채널 삭제 요청 - 채널 ID: {}", channelId);
    if (!channelRepository.existsById(channelId)) {
      throw new ChannelNotFoundException(channelId);
    }
    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    channelRepository.deleteById(channelId);
    log.info("채널 삭제 성공 - 채널 ID: {}", channelId);
  }
}
