package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(Channel channel, Instant lastMessageAt, List<UUID> userList) {
    @Override
    public String toString() {
        return  channel.toString() + "\n" +
                "최근 메시지: " + (lastMessageAt == null ? "없음" : lastMessageAt) + "\n" +
                "참여 중인 유저 수: " + (userList == null ? 0 : userList.size()) + "명\n"; }
}
