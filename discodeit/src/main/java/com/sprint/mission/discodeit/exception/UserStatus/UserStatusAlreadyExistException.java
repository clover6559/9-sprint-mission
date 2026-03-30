package com.sprint.mission.discodeit.exception.UserStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserStatusAlreadyExistException extends UserStatusException {

    public UserStatusAlreadyExistException(UUID userStatusId) {
        super(
                ErrorCode.DUPLICATE_USERSTATUS,
                String.format("이미 유저 상태가 존재합니다: ID=%s", userStatusId));
        addDetails("userStatusId", userStatusId);
        addDetails("searchType", "byId");
    }
}
