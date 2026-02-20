package com.sprint.mission.discodeit.dto.user;


public record UserUpdateRequest(String newUsername, String newEmail, String newPassword) {

//
//
//  @Override
//  public String toString() {
//    String profileInfo = (userUpdateInfo.profileImageInfo() != null)
//        ? userUpdateInfo.profileImageInfo().fileName()
//        : "변경 없음";
//    return "이름 : " + userUpdateInfo.newUsername + "\n" +
//        "ID: " + targetId + "\n" +
//        "이메일 : " + userUpdateInfo.newEmail + "\n" +
//        "비밀번호 : " + userUpdateInfo.newPassword + "\n" +
//        "프로필 이미지 : " + profileInfo;
//  }
}
