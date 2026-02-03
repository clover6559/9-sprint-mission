package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
@Getter
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

}
