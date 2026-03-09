package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.request.MessageCreate;
import com.sprint.mission.discodeit.dto.request.MessageUpdate;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageService {

  MessageDto create(MessageCreate messageCreate, List<BinaryContentCreate> binaryContentCreates);

  MessageDto findById(UUID messageId);

  List<MessageDto> findAllByChannelId(UUID channelId);

  MessageDto update(UUID messageId, MessageUpdate request);

  void delete(UUID messageId);

  PageResponse<MessageDto> findSliceByMessage(MessageDto messageDto, Pageable pageable);


}
