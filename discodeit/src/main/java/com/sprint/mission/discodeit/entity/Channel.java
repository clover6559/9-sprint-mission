package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {
    public enum ChannelType {
        PUBLIC, PRIVATE
    }
    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private String channelName;
    private String description;
    private UUID channelId;
    private ChannelType channelType;
    private String userName;

    public Channel(ChannelType channelType, String channelName, String description, User user) {
        this.userId = user.getUserId();
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.channelId = UUID.randomUUID();
        this.channelName = channelName;
        this.description = description;
        this.channelType = channelType;
        this.userName = user.getUserName();
    }

    public String getUserName() {
        return userName;
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

    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public String update(String channelName, String description) {
        List<String> changes = new ArrayList<>();
        if (channelName != null && !channelName.isBlank()) {
            this.channelName = channelName;
            changes.add("채널 이름 : " + channelName);
        }
        if (description != null && !description.isBlank()) {
            this.description = description;
            changes.add("소개 : " + description);
        }
        this.updatedAt = Instant.now();
    return changes.isEmpty() ? "변경 사항 없음: " : String.join(", ", changes) + "로 수정됨";
    }


    @Override
    public String toString() {
        return  "유저 Id : " + userId + '\n' +
                "유저 이름 : " + userName  + '\n' +
                "채널 타입 : " + channelType + '\n' +
                "채널 이름 : " + channelName + '\n' +
                "채널 ID : " + channelId + '\n' +
                "채널 소개 : " + description + '\n' +
                "생성시간 : " + User.formatTime(createdAt) + '\n' +
                "수정시간 : " + User.formatTime(updatedAt) + '\n';
    }
}

