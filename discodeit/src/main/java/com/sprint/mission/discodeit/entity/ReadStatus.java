package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
@Getter
public class ReadStatus {
    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant createdAt;
    private Instant updatedAt;

public ReadStatus(User user, Channel channel) {
    this.id = UUID.randomUUID();
    this.userId = user.getUserId();
    this.channelId = channel.getChannelId();
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
}
}
