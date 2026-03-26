package com.sprint.mission.discodeit.exception.User;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public abstract class UserException extends DiscodeitException {

  protected UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected UserException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }
}
