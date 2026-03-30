package com.sprint.mission.discodeit.exception.ReadStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ReadStatusAlreadyExistException extends ReadStatusException {

    public ReadStatusAlreadyExistException(UUID readStatusId) {
        super(ErrorCode.DUPLICATE_READSTATUS, String.format("이미 읽음상태가 존재합니다: ID=%s", readStatusId));
        addDetails("readStatusId", readStatusId);
        addDetails("searchType", "byId");
    }
}
