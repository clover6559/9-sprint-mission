package com.sprint.mission.discodeit.exception.BinaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class BinaryContentException extends DiscodeitException {

    protected BinaryContentException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected BinaryContentException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
