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
//        this.channelName = privateChannel.channelName();
        this.channelType = ChannelType.PRIVATE;
//        this.userName = privateChannel.user().getUserName();
    }

    public String changes(ChannelUpdate.ChannelUpdateInfo channelUpdateInfo) {
        List<String> changes = new ArrayList<>();
        if (channelUpdateInfo.channelName() != null && !channelUpdateInfo.channelName().isBlank()) {
            this.channelName = channelUpdateInfo.channelName();
            changes.add("채널 이름 : " + channelName);
        }
        if (channelUpdateInfo.description() != null && !channelUpdateInfo.description().isBlank()) {
            this.description = channelUpdateInfo.description();
            changes.add("소개 : " + description);
        }
        this.updatedAt = Instant.now();
    return changes.isEmpty() ? "변경 사항 없음: " : String.join(", ", changes) + "로 수정됨";
    }


    @Override
    public String toString() {
        return  "유저 Id : " + userId + '\n' +
                "유저 이름 : " + userName  + '\n' +
                "채널 타입 : " + channelType + '\n' +
                "채널 이름 : " + channelName + '\n' +
                "채널 ID : " + id + '\n' +
                "채널 소개 : " + description + '\n' +
                "생성시간 : " + User.formatTime(createdAt) + '\n' +
                "수정시간 : " + User.formatTime(updatedAt) + '\n';
    }
}

