package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.DateUtil.formatTime;

@Getter
public class Message implements Serializable {

  private UUID authorId;
  private String content;
  private Instant createdAt;
  private Instant updatedAt;
  private UUID channelId;
  private UUID id;
  private List<UUID> attachmentIds;

  public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.authorId = authorId;
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
    this.content = content;
    this.channelId = channelId;
    this.attachmentIds = attachmentIds;
  }

  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }
}
