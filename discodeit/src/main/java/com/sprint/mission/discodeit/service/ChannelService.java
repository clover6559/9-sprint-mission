package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.CreatePrivate;
import com.sprint.mission.discodeit.dto.request.CreatePublic;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto create(CreatePublic request);

  ChannelDto create(CreatePrivate request);

  ChannelDto find(UUID channelId);

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto update(UUID channelId, ChannelUpdateRequest request);

  void delete(UUID channelId);
}
