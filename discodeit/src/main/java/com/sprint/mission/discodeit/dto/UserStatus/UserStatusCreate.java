package com.sprint.mission.discodeit.dto.UserStatus;


import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;


public record UserStatusCreate(UUID userId, Instant lastActiveAt) {
}
