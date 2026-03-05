package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;

public record UserStatus(UUID id, UUID userId, Instant lastActiveAt) {

}
