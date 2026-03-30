package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record MessageCreateRequest(
        @NotNull(message = "메세지는 필수 입력 항목입니다.") String content,
        @NotNull(message = "채널 ID는 필수 입력 항목입니다.") UUID channelId,
        @NotNull(message = "유저 ID는 필수 입력 항목입니다.") UUID authorId) {}
