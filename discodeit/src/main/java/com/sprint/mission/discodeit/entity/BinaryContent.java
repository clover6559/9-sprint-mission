package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BinaryContent {
    private UUID id;
    private Instant createdAt;

public BinaryContent() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
}

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
