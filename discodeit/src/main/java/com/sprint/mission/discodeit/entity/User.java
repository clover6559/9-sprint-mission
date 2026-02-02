package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
public class User implements Serializable {
    private final UUID userId;
    private String userName;
    private String email;
    private final Instant createdAt;
    private Instant updatedAt;
    private String password;
    private UUID profileId;


    public User(UserCreate UserCreate) {
        this.userId = UUID.randomUUID();
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
        return "유저 ID : " + userId + '\n' +
                "유저 이름 : " + userName + '\n' +
                "유저 PW : " + password + '\n' +
                "이메일 : " + email + '\n' +
                "생성 시간 : " + formatTime(createdAt) + '\n' +
                "수정 시간 : " + formatTime(updatedAt) + '\n';
    }

    public String changes(UserUpdate.UserUpdateInfo updateInfo) {
        List<String> changes = new ArrayList<>();
        if (updateInfo.userName() != null && ! updateInfo.userName().isBlank()) {
            this.userName = updateInfo.userName();
            changes.add("이름 : " + userName);
        }
        if (updateInfo.email() != null && !updateInfo.email().isBlank()) {
            this.email = updateInfo.email();
            changes.add("이메일 : " + email);
        }
        if (updateInfo.password() != null && !updateInfo.password().isBlank()) {
            this.password = updateInfo.password();
            changes.add("비밀번호 : " + password);
        }
        this.updatedAt = Instant.now();
        return changes.isEmpty() ? "변경 사항 없음: " : String.join(", ", changes) + "로 수정됨";
    }
    public static DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String formatTime(Instant timeStamp) {
        String zonedDateTime = timeStamp.atZone(ZoneId.systemDefault()).format(formatter);
        return zonedDateTime;
    }
}