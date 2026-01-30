package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static java.time.LocalTime.now;

public class UserStatus {
    private UUID id;
    private UUID userId;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastActiveTime;
    public enum Status {
        ONLINE, OFFLINE
    }
    private Status status;

    public UserStatus(Status status, UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        Instant now = Instant.now();
        this.updatedAt = now;
        this.createdAt = now;
        this.lastActiveTime = now;
        this.status = status;
    }
    public boolean isOnline() {
        Instant fiveminute =  Instant.now().minus(5, ChronoUnit.MINUTES);
        return this.lastActiveTime.isAfter(fiveminute); }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getLastActiveTime() {
        return lastActiveTime;
    }

    public Status getStatus() {
        return status;
    }
}