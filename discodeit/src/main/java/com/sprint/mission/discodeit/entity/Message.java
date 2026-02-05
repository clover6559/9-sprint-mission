package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.message.MessageCreate;
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
    private UUID channelId;
    private UUID id;
    private List<UUID> attachmentIds;

    public Message(MessageCreate MessageCreate) {
        this.userId = MessageCreate.basicMessageInfo().senderId();
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.content = MessageCreate.basicMessageInfo().content();
        this.channelId = MessageCreate.basicMessageInfo().channelId();
        this.id = UUID.randomUUID();
        this.attachmentIds = new ArrayList<>();
}
    public void updateAttachmentIds(List<UUID> attachmentIds) {
        this.attachmentIds = attachmentIds;
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
        return  "메시지 ID: " + id + '\n' +
                "채널 ID: " + channelId + '\n' +
                "유저 ID: " + userId + '\n' +
                "내용: " + content + '\n' +
                "생성 시간: " + User.formatTime(createdAt) + '\n' +
                "수정 시간: " + User.formatTime(updatedAt) + '\n' +
                "첨부 파일 ID: " + attachmentIds;
    }
}
