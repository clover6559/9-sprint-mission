package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "읽음 상태 정보")
public record ReadStatusDto(UUID id, UUID userId, UUID channelId, Instant lastReadAt) {

}
