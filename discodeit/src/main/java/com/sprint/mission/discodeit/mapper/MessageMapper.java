package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;

  public MessageDto toDto(Message message) {
    UserDto authorDto = userMapper.toDto(message.getAuthor());
    List<BinaryContentDto> attachmentDto = message.getAttachmentIds().stream()
        .map(binaryContentMapper::toDto)
        .toList();
    return new MessageDto(message.getId(), message.getCreatedAt(), message.getUpdatedAt(),
        message.getContent(), message.getChannel().getId(), authorDto, attachmentDto);
  }

}
