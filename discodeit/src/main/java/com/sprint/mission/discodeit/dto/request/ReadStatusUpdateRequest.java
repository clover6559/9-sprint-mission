package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.*;
import java.time.Instant;

public record ReadStatusUpdateRequest(
        @PastOrPresent(message = "마지막 활동시간은 과거 또는 현재 시간이여야 합니다.") Instant newLastReadAt) {}
