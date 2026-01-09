package entity;

import java.util.UUID;

public class Channel {
    private final UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String topic;
    private String content;

    public Channel(String topic) {
        this.id = id;
        Long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
        this.topic = topic;
    }

    public UUID getId() {
        return id;
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
}
