//package com.sprint.mission.discodeit.mapper;
//
//import com.sprint.mission.discodeit.dto.data.ChannelDto;
//import com.sprint.mission.discodeit.dto.data.UserDto;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.ReadStatus;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.ReadStatusRepository;
//import java.time.Instant;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ChannelMapper {
//
//  private final MessageRepository messageRepository;
//  private final ReadStatusRepository readStatusRepository;
//  private final UserMapper userMapper;
//
//  public ChannelDto toDto(Channel channel) {
//    List<UUID> participantIds = readStatusRepository.findByChannel(channel).stream()
//        .map(ReadStatus::getUser) // ReadStatus 엔티티에서 User 엔티티 추출
//        .map(userMapper::toDto)   // 다이어그램 요구사항대로 UserMapper 활용
//        .map(UserDto::id)         // UserDto에서 UUID만 추출
//        .toList();
//    Instant lastMessageAt = messageRepository.findTopByChannelOrderByCreatedAtDesc(channel);
//
//    return new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
//        channel.getDescription(), participantIds, lastMessageAt);
//  }
//
//}
