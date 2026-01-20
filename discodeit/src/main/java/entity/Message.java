package entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private UUID userId;
    private String content;
    private Long createdAt;
    private Long updatedAt;
    private String channelName;
    private String userName;
    private String email;
    private UUID channelId;
    private UUID massageId;

    public Message(String content, User user, Channel channel) {
        this.userId = user.getUserId();
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
        this.content = content;
        this.channelId = channel.getChannelId();
        this.channelName = channel.getChannelName();
        this.userName = user.getUserName();
        this.email = user.getEmail();
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

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void update(UUID massageId, String content){
            this.content = content;
            this.updatedAt = System.currentTimeMillis();
        }

    @Override
    public String toString() {
        return  "유저 이름 : " + userName + '\n' +
                "채널 이름 : " + channelName + '\n' +
                "채널 ID : " + channelId + '\n' +
                "내용 : " + content + '\n' +
                "메세지 ID : " + massageId + '\n' +
                "생성시간 : " + User.formatTime(createdAt) + '\n' +
                "수정시간 : " + User.formatTime(updatedAt) + '\n';
    }
}

