package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BinaryContent {
    private final UUID id;
    private final Instant createdAt;
    private final UUID refId;
    private final String fileName;
    private final byte[] data;

public BinaryContent(UUID refId, String fileName, byte[] data) {
    this.id = UUID.randomUUID();
    this.refId = refId;
    this.fileName = fileName;
    this.data = data;
    this.createdAt = Instant.now();
}

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getRefId() {
        return refId;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }
}
