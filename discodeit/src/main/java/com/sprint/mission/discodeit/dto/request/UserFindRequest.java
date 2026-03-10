package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record UserFindRequest(boolean userStatus, UUID userId, String userName, String email) {

  @Override
  public String toString() {
    return "이름 : " + userName + "\n" +
        "ID : " + userId + "\n" +
        "이메일 : " + email + "\n" +
        "상태 : " + (userStatus ? "온라인" : "오프라인");
  }
}
