package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User  implements Serializable {
    private final UUID userId;
    private String userName;
    private String email;
    private final Instant createdAt;
    private Instant updatedAt;
    private String password;
    private UUID profileId;


    public User(String userName, String email, String password, BinaryContent binaryContent) {
        this.userId = UUID.randomUUID();
        this.userName = userName;
        Instant now = Instant.now();
        this.updatedAt = now;
        this.email = email;
        this.createdAt = now;
        this.password = password;
        this.profileId = binaryContent.getId();
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getPassword() {
        return password;
    }

    public UUID getProfileId() {
        return profileId;
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

    public String update(String userName, String email, String password) {
        List<String> changes = new ArrayList<>();
        if (userName != null && !userName.isBlank()) {
            this.userName = userName;
            changes.add("이름 : " + userName);
        }
        if (email != null && !email.isBlank()) {
            this.email = email;
            changes.add("이메일 : " + email);
        }
        if (password != null && !password.isBlank()) {
            this.password = password;
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