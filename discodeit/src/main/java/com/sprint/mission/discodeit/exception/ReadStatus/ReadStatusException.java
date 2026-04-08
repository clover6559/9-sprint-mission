package com.sprint.mission.discodeit.exception.ReadStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ReadStatusException extends DiscodeitException {

    protected ReadStatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected ReadStatusException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
