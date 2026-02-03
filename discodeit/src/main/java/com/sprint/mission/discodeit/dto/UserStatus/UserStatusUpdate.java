package com.sprint.mission.discodeit.dto.UserStatus;


import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public record UserStatusUpdate(UUID id, String statusMessage, UserStatus.Status statusType) {
}
