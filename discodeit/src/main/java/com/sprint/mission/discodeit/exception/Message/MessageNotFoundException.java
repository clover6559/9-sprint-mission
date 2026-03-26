package com.sprint.mission.discodeit.exception.Message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException(UUID messageId) {
    super(ErrorCode.MESSAGE_NOT_FOUND,
        String.format("메시지를 찾을 수 없습니다: ID=%s", messageId));
    addDetails("messageId", messageId);
    addDetails("searchType", "byId");
  }
}
