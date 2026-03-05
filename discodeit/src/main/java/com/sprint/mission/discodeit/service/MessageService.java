package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.request.MessageCreate;
import com.sprint.mission.discodeit.dto.request.MessageUpdate;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto create(MessageCreate messageCreate, List<BinaryContentCreate> binaryContentCreates);

  MessageDto findById(UUID messageId);

  List<MessageDto> findAllByChannelId(UUID channelId);

  MessageDto update(UUID messageId, MessageUpdate request);

  void delete(UUID messageId);


}
