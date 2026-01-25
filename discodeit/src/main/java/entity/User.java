package entity;

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
    private final long createdAt;
    private long updatedAt;
    private String password;

    public User(String userName, String email, String password) {
        this.userId = UUID.randomUUID();
        this.userName = userName;
        long now = System.currentTimeMillis();
        this.updatedAt = now;
        this.email = email;
        this.createdAt = now;
        this.password = password;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getPassword() {
        return password;
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
        this.updatedAt = System.currentTimeMillis();
        return changes.isEmpty() ? "변경 사항 없음: " : String.join(", ", changes) + "로 수정됨";
    }
    public static DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String formatTime(long timeStamp) {
        Instant instant = Instant.ofEpochMilli(timeStamp);
        String zonedDateTime = instant.atZone(ZoneId.systemDefault()).format(formatter);
        return zonedDateTime;
    }
}