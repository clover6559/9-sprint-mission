package com.sprint.mission.discodeit.exception.Channel;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelException extends DiscodeitException {

  protected ChannelException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected ChannelException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }
}
