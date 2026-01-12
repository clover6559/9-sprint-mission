package entity;

import java.util.UUID;

public class Message {
    private UUID userId;
    private String message;
    private Long createdAt;
    private Long updatedAt;
    private UUID channelId;

    public Message(String message, User user, Channel channel) {
        this.userId = user.getUserId();
        Long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
        this.message = message;
        this.channelId = channel.getChannelId();
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userId=" + userId +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", channelId=" + channelId +
                '}';
    }

    public Message updateMessage(String message) {
            this.message = message;
            this.updatedAt = System.currentTimeMillis();
            return this;
        }


    }

