package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BinaryContent {
    private UUID id;
    private Instant createdAt;
    private UUID refId;
    private String path;
    private String file;

public BinaryContent(UUID refId, String path,String file) {
    this.id = UUID.randomUUID();
    this.refId = refId;
    this.path = path;
    this.file = file;
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

    public String getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }
}
