package com.sprint.mission.discodeit.exception.Auth;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class AuthException extends DiscodeitException {

  protected AuthException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected AuthException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }
}
