package com.sprint.mission.discodeit.exception.User;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(UUID userId) {
    super(ErrorCode.USER_NOT_FOUND,
        String.format("사용자를 찾을 수 없습니다: ID=%s", userId));
    addDetails("userId", userId);
    addDetails("searchType", "byId");
  }

  public UserNotFoundException(String email) {
    super(ErrorCode.USER_NOT_FOUND,
        String.format("사용자를 찾을 수 없습니다: Email=%s", email));
    addDetails("email", email);
    addDetails("searchType", "byEmail");
  }
}
