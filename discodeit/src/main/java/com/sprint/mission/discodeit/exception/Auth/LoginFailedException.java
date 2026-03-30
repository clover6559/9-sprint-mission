package com.sprint.mission.discodeit.exception.Auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class LoginFailedException extends AuthException {

    public LoginFailedException(String username) {
        super(ErrorCode.INVALID_CREDENTIALS);
        addDetails("attemptedUsername", username);
    }
}
