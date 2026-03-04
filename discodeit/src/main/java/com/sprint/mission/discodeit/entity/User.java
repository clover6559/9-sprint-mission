package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.DateUtil.formatTime;

@Getter
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private String username;
  private String email;
  private final Instant createdAt;
  private Instant updatedAt;
  private String password;
  private UUID profileId;


  public User(String username, String email, String password, UUID profileId) {
    this.id = UUID.randomUUID();
    this.username = username;
    Instant now = Instant.now();
    this.updatedAt = now;
    this.email = email;
    this.createdAt = now;
    this.password = password;
    this.profileId = profileId;

  }


  @Override
  public String toString() {
    return "유저 ID : " + id + '\n' +
        "유저 이름 : " + username + '\n' +
        "유저 PW : " + password + '\n' +
        "이메일 : " + email + '\n' +
        "생성 시간 : " + formatTime(createdAt) + '\n' +
        "수정 시간 : " + formatTime(updatedAt) + '\n';
  }

  public void update(String newUsername, String newEmail, String newPassword, UUID newProfileId) {
    boolean anyValueUpdated = false;
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
      anyValueUpdated = true;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
      anyValueUpdated = true;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
      anyValueUpdated = true;
    }
    if (newProfileId != null && !newProfileId.equals(this.profileId)) {
      this.profileId = newProfileId;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }
}
