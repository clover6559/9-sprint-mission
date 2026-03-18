package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "채널 정보")
public record ChannelDto(
    @Schema(description = "채널의 고유 식별자")
    UUID id,
    @Schema(description = "채널의 타입 (PUBLIC, PRIVATE)")
    ChannelType type,
    @Schema(description = "채널의 이름")
    String name,
    @Schema(description = "채널의 설명")
    String description,
    @Schema(description = "채널의 참여자")
    List<UUID> participantIds,
    @Schema(description = "채널의 마지막 메세지 시간")
    Instant lastMessageAt
) {

}
