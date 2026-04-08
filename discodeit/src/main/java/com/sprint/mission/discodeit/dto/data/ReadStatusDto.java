package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "읽음 상태 정보")
public record ReadStatusDto(
        @Schema(description = "읽음 상태 기록 ID") UUID id,
        @Schema(description = "유저 식별자") UUID userId,
        @Schema(description = "채널 식별자") UUID channelId,
        @Schema(description = "마지막으로 읽은 시점") Instant lastReadAt) {}
