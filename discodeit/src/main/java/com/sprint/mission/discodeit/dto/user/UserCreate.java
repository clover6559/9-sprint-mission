package com.sprint.mission.discodeit.dto.user;

public record UserCreate(BasicUserInfo basicUserInfo, ProfileImageInfo profileImageInfo){
    public record BasicUserInfo(String userName, String email, String password) {}
    public record ProfileImageInfo(String fileName, byte[] data) {
    }
}
