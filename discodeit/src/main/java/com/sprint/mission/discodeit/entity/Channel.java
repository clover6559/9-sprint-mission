package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

  public enum ChannelType {
    PUBLIC, PRIVATE
  }

  private UUID userId;
  private Instant createdAt;
  private Instant updatedAt;
  private String name;
  private String description;
  private UUID id;
  private ChannelType type;

  public Channel(ChannelType type, String name, String description) {
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
    this.id = UUID.randomUUID();
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public void update(String newName, String newDescription) {
    boolean anyValueUpdated = false;
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      anyValueUpdated = true;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

}

