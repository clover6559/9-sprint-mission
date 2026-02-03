package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserCreate(BasicUserInfo basicUserInfo, ProfileImageInfo profileImageInfo){
    public record BasicUserInfo(String userName, String email, String password) {}
    public record ProfileImageInfo(UUID profileId, String fileName, byte[] data) {
    }
}
