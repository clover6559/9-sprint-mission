package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.*;

public record UserUpdateRequest(
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
    String newUsername,

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    String newEmail,

    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    String newPassword) {

}
