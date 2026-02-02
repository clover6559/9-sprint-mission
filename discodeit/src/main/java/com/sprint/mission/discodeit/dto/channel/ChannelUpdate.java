package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record ChannelUpdate(UUID targetId, ChannelUpdateInfo channelUpdateInfo ) {
    public record ChannelUpdateInfo(String channelName, String description){
}
}
