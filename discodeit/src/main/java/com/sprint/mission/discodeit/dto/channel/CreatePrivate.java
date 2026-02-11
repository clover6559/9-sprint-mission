package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserFind;

import java.util.UUID;

public record CreatePrivate(UUID creatorId, UserFind user) {
}
