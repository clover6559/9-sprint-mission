package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.search.ChannelSearch;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(CreatePublic createPublic);
    Channel createPrivateChannel(CreatePrivate createPrivate);
    ChannelFind find(UUID channelId);
    List<Channel> search(ChannelSearch channelSearch);
    List<ChannelResponse> findAllByUserId(UUID userId);
    void update(ChannelUpdate ChannelUpdate);
    boolean delete(UUID channelId);
    void printRemainChannel();
}
