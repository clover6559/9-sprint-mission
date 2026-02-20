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
  private ChannelType channelType;

  public Channel(ChannelType type, String name, String description) {
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
    this.id = UUID.randomUUID();
    this.name = name;
    this.description = description;
    this.channelType = type;
  }
//
//  public Channel(CreatePrivate createPrivate) {
//    this.userId = createPrivate.user().userId();
//    Instant now = Instant.now();
//    this.createdAt = now;
//    this.updatedAt = now;
//    this.id = UUID.randomUUID();
//    this.channelType = ChannelType.PRIVATE;
//  }

  public void updateInfo(String newName, String newDescription) {
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

