package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.UserStatus.StatusType;
import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static java.time.LocalTime.now;
@Getter
public class UserStatus {
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
    }
}