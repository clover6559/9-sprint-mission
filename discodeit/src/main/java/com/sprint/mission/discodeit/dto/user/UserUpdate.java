package com.sprint.mission.discodeit.dto.user;


import java.util.UUID;

public record UserUpdate(UUID targetId, UserUpdateInfo userUpdateInfo) {
        public record UserUpdateInfo(String userName, String email, String password, UserCreate.ProfileImageInfo profileImageInfo) {
        }
}


