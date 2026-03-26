package com.sprint.mission.discodeit.exception.UserStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserStatusException extends DiscodeitException {

  protected UserStatusException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected UserStatusException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }
}
