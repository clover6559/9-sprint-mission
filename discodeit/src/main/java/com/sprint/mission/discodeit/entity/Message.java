package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
public class Message implements Serializable {
    private UUID userId;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private String channelName;
    private String userName;
    private String email;
    private UUID channelId;
    private UUID messageId;
    private List<UUID> attachmentIds;

    public Message(String content, User user, Channel channel, BinaryContent binaryContent) {
        this.userId = user.getUserId();
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.content = content;
        this.channelId = channel.getChannelId();
        this.channelName = channel.getChannelName();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.messageId = UUID.randomUUID();
        this.attachmentIds = new ArrayList<>();
        if (binaryContent.getId() != null) {
         this.attachmentIds.add(binaryContent.getId());
    }
}

    public String update(String content) {
        List<String> changes = new ArrayList<>();
        if (content != null && !content.isBlank()) {
            this.content = content;
            changes.add("메세지 내용 : " + content);
        }
        this.updatedAt = Instant.now();
        return changes.isEmpty() ? "변경 사항 없음: " : String.join(", ", changes) + "로 수정됨";

    }

    @Override
    public String toString() {
        return  "유저 이름 : " + userName + '\n' +
                "채널 이름 : " + channelName + '\n' +
                "채널 ID : " + channelId + '\n' +
                "내용 : " + content + '\n' +
                "메세지 ID : " + messageId + '\n' +
                "생성시간 : " + User.formatTime(createdAt) + '\n' +
                "수정시간 : " + User.formatTime(updatedAt) + '\n';
    }
}

