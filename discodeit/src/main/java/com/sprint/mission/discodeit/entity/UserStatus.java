package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private UUID userId;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastActiveTime;
    private String statusMessage;
    public enum Status {
        ONLINE, AWAY, OFFLINE
    }
    private Status status;

    public UserStatus(UUID userId, String statusMessage, Status statusType) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        Instant now = Instant.now();
        this.updatedAt = now;
        this.createdAt = now;
        this.lastActiveTime = now;
        this.statusMessage = statusMessage;
        this.status = statusType;
    }
    public boolean isOnline() {
        Instant fiveminute =  Instant.now().minus(5, ChronoUnit.MINUTES);
        return this.lastActiveTime.isAfter(fiveminute); }

    public void updateUserStatus(String statusMessage, UserStatus.Status statusType) {
        this.statusMessage = statusMessage;
        this.status = statusType;
        this.lastActiveTime = Instant.now();
    }
}