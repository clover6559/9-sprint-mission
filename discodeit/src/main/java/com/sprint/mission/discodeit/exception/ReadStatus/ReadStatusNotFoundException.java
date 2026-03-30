package com.sprint.mission.discodeit.exception.ReadStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {

    public ReadStatusNotFoundException(UUID readStatusId) {
        super(
                ErrorCode.READSTATUS_NOT_FOUND,
                String.format("읽음 상태를 찾을 수 없습니다: ID=%s", readStatusId));
        addDetails("readStatusId", readStatusId);
        addDetails("searchType", "byId");
    }
}
