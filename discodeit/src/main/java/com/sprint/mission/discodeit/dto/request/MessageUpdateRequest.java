package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.*;

public record MessageUpdateRequest(@NotNull(message = "메세지는 필수입니다.") String newContent) {}
