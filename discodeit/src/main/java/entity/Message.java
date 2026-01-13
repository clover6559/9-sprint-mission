package entity;

import java.util.UUID;

public class Message {
    private UUID userId;
    private String content;
    private Long createdAt;
    private Long updatedAt;
    private String channelName;
    private UUID channelId;
    private UUID massageId;

    public Message(String content, User user, Channel channel) {
        this.userId = user.getUserId();
        Long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
        this.content = content;
        this.channelId = channel.getChannelId();
        this.channelName = channel.getChannelName();
        this.massageId = UUID.randomUUID();
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

    public String getChannelName() {
        return channelName;
    }

    public UUID getMassageId() {
        return massageId;
    }

    public String getContent() {
        return content;
    }
    public void update(String content){
            this.content = content;
            this.updatedAt = System.currentTimeMillis();
        }

    @Override
    public String toString() {
        return "Message{" +
                "userId=" + userId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", channelName='" + channelName + '\'' +
                ", channelId=" + channelId +
                ", massageId=" + massageId +
                '}';
    }
}

