package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record CreatePublicRequest(String channelName, String description, UUID userId) {
}
