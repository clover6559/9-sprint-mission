package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserResponse(boolean userStatus, UUID userId, String userName, String email) {
}
