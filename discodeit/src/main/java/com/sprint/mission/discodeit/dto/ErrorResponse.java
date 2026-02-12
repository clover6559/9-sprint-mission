package com.sprint.mission.discodeit.dto;

import java.time.LocalTime;

public record ErrorResponse(String message, int status, LocalTime timestamp) {
    public ErrorResponse(String message, int status) {
        this(message, status, LocalTime.now());
    }
}
