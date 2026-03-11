package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "메세지 정보")
public record MessageDto(UUID id, Instant createdAt, Instant updatedAt, String content,
                         UUID channelId, UserDto author, List<BinaryContentDto> attachments) {

}
