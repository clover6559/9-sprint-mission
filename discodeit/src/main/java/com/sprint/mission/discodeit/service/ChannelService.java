package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdate;
import com.sprint.mission.discodeit.dto.channel.CreatePrivate;
import com.sprint.mission.discodeit.dto.channel.CreatePublic;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.search.ChannelSearch;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPublicChannel(CreatePublic createPublic);
    Channel createPrivateChannel(CreatePrivate createPrivate);

    ChannelResponse find(UUID channelId);

    List<Channel> search(ChannelSearch channelSearch);

    List<ChannelResponse> findAllByUserId(UUID userId);

    String update(ChannelUpdate ChannelUpdate);

    boolean delete(UUID channelId);

    void printRemainChannel();
}
