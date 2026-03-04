package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserFind;

import java.util.List;
import java.util.UUID;

public record CreatePrivate(List<UUID> participantIds) {

}
