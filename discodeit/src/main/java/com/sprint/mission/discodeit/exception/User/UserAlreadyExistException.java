package com.sprint.mission.discodeit.exception.User;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserAlreadyExistException extends UserException {

    public UserAlreadyExistException(UUID userId) {
        super(ErrorCode.DUPLICATE_USER, String.format("이미 존재하는 사용자입니다: ID=%s", userId));
        addDetails("userId", userId);
        addDetails("searchType", "byId");
    }

    private UserAlreadyExistException(String message) {
        super(ErrorCode.DUPLICATE_USER, message);
    }

    public static UserAlreadyExistException ofEmail(String email) {
        UserAlreadyExistException exception =
                new UserAlreadyExistException(String.format("이미 가입된 이메일입니다: %s", email));
        exception.addDetails("email", email);
        exception.addDetails("duplicateType", "email");
        return exception;
    }

    public static UserAlreadyExistException ofUsername(String username) {
        UserAlreadyExistException exception =
                new UserAlreadyExistException(String.format("이미 사용 중인 이름입니다: %s", username));
        exception.addDetails("username", username);
        exception.addDetails("duplicateType", "username");
        return exception;
    }
}
