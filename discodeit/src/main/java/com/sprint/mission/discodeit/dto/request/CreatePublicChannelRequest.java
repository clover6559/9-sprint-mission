package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.*;

public record CreatePublicChannelRequest(
        @NotBlank(message = "이름은 필수 입력 항목입니다.")
                @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
                String name,
        @NotNull(message = "설명은 필수 입력 항목입니다.") String description) {}
