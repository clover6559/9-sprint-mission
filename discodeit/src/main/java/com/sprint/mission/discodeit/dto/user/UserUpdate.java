package com.sprint.mission.discodeit.dto.user;


import java.util.UUID;

public record UserUpdate(UUID targetId, UserUpdateInfo userUpdateInfo) {
        public record UserUpdateInfo(String userName, String email, String password,
                                     UserCreate.ProfileImageInfo profileImageInfo) { }
        @Override
        public String toString() {
                String profileInfo = (userUpdateInfo.profileImageInfo() != null)
                        ? userUpdateInfo.profileImageInfo().fileName()
                        : "변경 없음";
                return  "이름 : " + userUpdateInfo.userName + "\n" +
                        "ID: " + targetId + "\n" +
                        "이메일 : " + userUpdateInfo.email + "\n" +
                        "비밀번호 : " + userUpdateInfo.password + "\n" +
                        "프로필 이미지 : " + profileInfo;
        }
}
