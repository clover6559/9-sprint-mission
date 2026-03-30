package com.sprint.mission.discodeit.dto.request;


import java.time.Instant;
import jakarta.validation.constraints.*;


public record UserStatusUpdateRequest(
    @PastOrPresent(message = "마지막 활동시간은 과거 또는 현재 시간이여야 합니다.")
    @NotNull(message = "마지막 활동 시간은 필수입니다.")
    Instant newLastActiveAt) {

}
