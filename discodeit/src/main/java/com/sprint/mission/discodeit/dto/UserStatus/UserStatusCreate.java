package com.sprint.mission.discodeit.dto.UserStatus;


import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;


public record UserStatusCreate(UUID userId, String statusMessage, UserStatus.Status statusType) {
}
