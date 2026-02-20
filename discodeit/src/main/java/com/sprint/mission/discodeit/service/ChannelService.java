package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel create(CreatePublic request);

  Channel create(CreatePrivate request);

  ChannelResponse find(UUID channelId);

  List<ChannelResponse> findAllByUserId(UUID userId);

  Channel update(UUID channelId, ChannelUpdateRequest request);

  void delete(UUID channelId);
}
