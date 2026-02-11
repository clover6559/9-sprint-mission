package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record CreatePrivateRequest(UUID creatorId, UUID userId) {
}
