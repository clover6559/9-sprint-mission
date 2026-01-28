package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;

public record UserBasicInfo(String userName, String email, String password, UserStatus status) {
}
