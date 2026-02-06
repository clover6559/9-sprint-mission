package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.message.MessageCreate;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.DateUtil.formatTime;

@Getter
public class Message implements Serializable {
    private UUID authordId;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID channelId;
    private UUID id;
    private List<UUID> attachmentIds;

    public Message(MessageCreate MessageCreate) {
        this.authordId = MessageCreate.basicMessageInfo().senderId();
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

    public void updateInfo(String content) {
        StringBuilder changes = new StringBuilder();
        if (content != null && !content.isBlank()) {
            this.content = content;
            changes.append("메세지 내용 : ").append(content);
        }
        if (changes.length() > 0 ) {
            System.out.println("[수정완료] " + '\n' + changes.toString());
            this.updatedAt = Instant.now();
        }
     }

    @Override
    public String toString() {
        return  "채널 ID: " + channelId + '\n' +
                "유저 ID: " + authordId + '\n' +
                "내용: " + content + '\n' +
                "생성 시간: " + formatTime(createdAt) + '\n' +
                "수정 시간: " + formatTime(updatedAt) + '\n' +
                "첨부 파일 ID: " + attachmentIds;
    }
}
