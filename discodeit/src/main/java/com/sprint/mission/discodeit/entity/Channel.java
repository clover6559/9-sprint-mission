package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdate;
import com.sprint.mission.discodeit.dto.channel.CreatePrivate;
import com.sprint.mission.discodeit.dto.channel.CreatePublic;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    public enum ChannelType {
        PUBLIC, PRIVATE
    }
    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private String channelName;
    private String description;
    private UUID id;
    private ChannelType channelType;
    private String userName;

    public Channel(CreatePublic createPublic) {
        this.userId = createPublic.user().getId();
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.id = UUID.randomUUID();
        this.channelName = createPublic.channelName();
        this.description = createPublic.description();
        this.channelType = ChannelType.PUBLIC;
        this.userName = createPublic.user().getUserName();
    }
    public Channel(CreatePrivate createPrivate) {
        this.userId = createPrivate.user().getId();
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.id = UUID.randomUUID();
        this.channelType = ChannelType.PRIVATE;
    }

    public void updateInfo(ChannelUpdate.ChannelUpdateInfo updateInfo) {
        if (updateInfo == null) return;
        StringBuilder changes = new StringBuilder();
        if (updateInfo.channelName() != null && !updateInfo.channelName().isBlank()) {
            this.channelName = updateInfo.channelName();
            changes.append("채널 이름 : ").append(channelName).append('\n');
        }
        if (updateInfo.description() != null && !updateInfo.description().isBlank()) {
            this.description = updateInfo.description();
            changes.append("소개 : ").append(channelName).append('\n');
        }
        if (changes.length() > 0) {
            this.updatedAt = Instant.now();
            System.out.println("[수정완료] " + '\n' + changes.toString());
        }
    }


    @Override
    public String toString() {
        return  "유저 이름 : " + userName  + '\n' +
                "채널 타입 : " + channelType + '\n' +
                "채널 이름 : " + channelName + '\n' +
                "채널 소개 : " + description + '\n' ;
//                "생성시간 : " + formatTime(createdAt) + '\n' +
//                "수정시간 : " + formatTime(updatedAt) + '\n';
    }
}

