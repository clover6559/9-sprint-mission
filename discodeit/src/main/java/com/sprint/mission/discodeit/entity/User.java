package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.DateUtil.formatTime;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private String userName;
    private String email;
    private final Instant createdAt;
    private Instant updatedAt;
    private String password;
    private UUID profileId;


    public User(UserCreate UserCreate) {
        this.id = UUID.randomUUID();
        this.userName = UserCreate.basicUserInfo().userName();
        Instant now = Instant.now();
        this.updatedAt = now;
        this.email = UserCreate.basicUserInfo().email();
        this.createdAt = now;
        this.password = UserCreate.basicUserInfo().password();
        if(UserCreate.profileImageInfo() != null) {
            this.profileId = UserCreate.profileImageInfo().profileId();
        }

    }
    public void updateProfileId(UUID newProfileId) {
        this.profileId = newProfileId;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "유저 ID : " + id + '\n' +
                "유저 이름 : " + userName + '\n' +
                "유저 PW : " + password + '\n' +
                "이메일 : " + email + '\n' +
                "생성 시간 : " + formatTime(createdAt) + '\n' +
                "수정 시간 : " + formatTime(updatedAt) + '\n';
    }

    public void updateInfo(UserUpdate.UserUpdateInfo updateInfo) {
        if (updateInfo == null) return;
        StringBuilder changes = new StringBuilder();
        if (updateInfo.userName() != null && !updateInfo.userName().isBlank()) {
            this.userName = updateInfo.userName();
            changes.append("이름 : ").append(userName).append('\n');
        }
        if (updateInfo.email() != null && !updateInfo.email().isBlank()) {
            this.email = updateInfo.email();
            changes.append("이메일 : ").append(email).append('\n');
        }
        if (updateInfo.password() != null && !updateInfo.password().isBlank()) {
            this.password = updateInfo.password();
            changes.append("비밀번호 : ").append(password).append('\n');
        }
        if (changes.length() > 0) {
            this.updatedAt = Instant.now();
            System.out.println("[수정완료] " + '\n' + changes.toString());
        }

    }
    }
