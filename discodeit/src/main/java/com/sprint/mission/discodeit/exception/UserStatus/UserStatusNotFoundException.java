package com.sprint.mission.discodeit.exception.UserStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {

    public UserStatusNotFoundException(UUID userStatusId) {
        super(ErrorCode.USERSTATUS_NOT_FOUND, String.format("유저 상태를 찾을 수 없습니다: ID=%s", userStatusId));
        addDetails("userStatusId", userStatusId);
        addDetails("searchType", "byId");
    }
}
