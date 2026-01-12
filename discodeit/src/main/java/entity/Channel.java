package entity;

import java.util.UUID;

public class Channel {
    private final UUID userId;
    private Long createdAt;
    private Long updatedAt;
    private String topic;
    private String content;
    private UUID channelId;

    public Channel(String topic, String content, User user) {
        this.userId = user.getUserId();
        Long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
        this.channelId = UUID.randomUUID();
        this.topic = topic;
        this.content = content;
    }

    public UUID getId() {
        return userId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }
    public Long getUpdatedAt() {
        return updatedAt;
    }
    public String getTopic() {
        return topic;
    }
    public String getContent() {
        return content;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public Channel updateChannel(String topic, String content) {
        this.topic = topic;
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
        return this;

    }
}
