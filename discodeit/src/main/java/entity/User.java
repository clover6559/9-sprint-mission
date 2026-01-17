package entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class User {
    private final UUID userId;
    private String userName;
    private String email;
    private final Long createdAt;
    private Long updatedAt;
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

    //    public String timeUtile {
//
//    }
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

    public void update(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.updatedAt = System.currentTimeMillis();
    }
    public static DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String formatTime(long timeStamp) {
        Instant instant = Instant.ofEpochMilli(timeStamp);
        String zonedDateTime = instant.atZone(ZoneId.systemDefault()).format(formatter);
        return zonedDateTime;
    }
}