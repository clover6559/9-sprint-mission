package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "사용자 상태 정보")
public record UserStatusDto(
    @Schema(description = "유저 상태의 고유 식별자")
    UUID id,
    @Schema(description = "유저의 고유 식별자")
    UUID userId,
    @Schema(description = "마지막 활동 시간")
    Instant lastActiveAt) {

}
