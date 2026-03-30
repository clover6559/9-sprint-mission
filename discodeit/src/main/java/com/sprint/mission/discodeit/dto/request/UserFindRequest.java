package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record UserFindRequest(
        boolean userStatus,
        @NotNull(message = "유저 ID는 필수 입력 항목입니다.") UUID userId,
        @NotBlank(message = "이름은 필수 입력 항목입니다.")
                @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
                String username,
        @NotBlank(message = "이메일은 필수 입력 항목입니다.") @Email(message = "올바른 이메일 형식이 아닙니다.")
                String email) {}
