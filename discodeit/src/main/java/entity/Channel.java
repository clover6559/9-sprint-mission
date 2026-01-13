package entity;

import java.util.UUID;

public class Channel {
    public enum ChannelType {
        PUBLIC, PRIVATE
    }
    private UUID userId;
    private Long createdAt;
    private Long updatedAt;
    private String channelName;
    private String description;
    private UUID channelId;
    private ChannelType channelType;
    private String userName;

    public Channel(String channelName, String description, User user) {
        this.userId = user.getUserId();
        Long now = System.currentTimeMillis();
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
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

    public void update(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }
}

