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
    private Instant lastTime;
    private enum Status {
        ONLINE, OFFLINE
    }
    private Status status;

    public UserStatus(Status status, User user) {
        this.id = UUID.randomUUID();
        this.userId = user.getUserId();
        Instant now = Instant.now();
        this.updatedAt = now;
        this.createdAt = now;
        this.lastTime = now;
        this.status = status;
    }
    public boolean Connectionstatus() {
        Instant fiveminute =  Instant.now().minus(5, ChronoUnit.MINUTES);
        if (this.lastTime.isAfter(fiveminute)) {
            return true;
        }return false;
    }

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

    public Instant getLastTime() {
        return lastTime;
    }

    public Status getStatus() {
        return status;
    }
}