package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public abstract class ChannelMapper {

  @Autowired
  protected MessageRepository messageRepository;
  @Autowired
  protected ReadStatusRepository readStatusRepository;

  @Mapping(target = "participantIds", expression = "java(fetchParticipantIds(channel))")
  @Mapping(target = "lastMessageAt", expression = "java(fetchLastMessageAt(channel))")
  public abstract ChannelDto toDto(Channel channel);

  protected List<UUID> fetchParticipantIds(Channel channel) {
    return readStatusRepository.findAllByChannelId(channel.getId()).stream()
        .map(readStatus -> readStatus.getUser().getId())
        .toList();
  }

  protected Instant fetchLastMessageAt(Channel channel) {
    return messageRepository.findByChannel(channel).stream()
        .map(Message::getCreatedAt)
        .max(Comparator.naturalOrder())
        .orElse(channel.getUpdatedAt());
  }
}
