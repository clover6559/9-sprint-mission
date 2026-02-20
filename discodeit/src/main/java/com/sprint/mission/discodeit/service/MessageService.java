package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.message.MessageCreate;
import com.sprint.mission.discodeit.dto.message.MessageUpdate;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.search.MessageSearch;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreate messageCreate, BinaryContentCreate binaryCreate);
    Message findById(UUID messageId);
    List<Message> search(MessageSearch condition);
    List<Message> findAllByChannelId(UUID channelId);
    void update(UUID messageId, MessageUpdate MessageUpdate);
    boolean delete(UUID messageId);
    void printRemainMessages();
    String formatMessage(Message message);

}
