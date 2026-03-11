package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "채널 정보")
public record ChannelDto(UUID id,
                         ChannelType type,
                         String name,
                         String description,
                         List<UUID> participantIds,
                         Instant lastMessageAt
) {

}
