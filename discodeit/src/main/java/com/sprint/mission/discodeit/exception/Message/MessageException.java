package com.sprint.mission.discodeit.exception.Message;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageException extends DiscodeitException {

  protected MessageException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected MessageException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }
}
