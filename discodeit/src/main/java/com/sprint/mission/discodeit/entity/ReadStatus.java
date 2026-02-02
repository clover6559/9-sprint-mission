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
    private Instant lastReadAt;

public ReadStatus(UUID channelId, UUID userId) {
    this.id = UUID.randomUUID();
    this.userId = userId;
    this.channelId = channelId;
    Instant now = Instant.now();
    this.createdAt = now;
    this.lastReadAt = now;
}
public void updateLastReadAt(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
}
}
