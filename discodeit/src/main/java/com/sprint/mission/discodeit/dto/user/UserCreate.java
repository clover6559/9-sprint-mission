package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public record UserCreate(BasicUserInfo basicUserInfo, ProfileImageInfo profileImageInfo){
    public record BasicUserInfo(String userName, String email, String password) {}
    public record ProfileImageInfo(String path, String file) {
    }
}
