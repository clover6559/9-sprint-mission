package entity;

import java.util.UUID;

public class User {
    private UUID userId;
    private String username;
    private String phoneNumber;
    private String email;
    private Long createdAt;
    private Long updatedAt;

    public User(String displayName, String phoneNumber, String email) {
        this.userId = UUID.randomUUID();
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        long now = System.currentTimeMillis();
        this.updatedAt = now;
        this.email = email;
        this.createdAt = now;
    }

    public UUID getId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

    @Override
    public String toString() {
        return "유저 정보" + '\n' +
                "유저 Id = " + userId + '\n' +
                "닉네임 = '" + displayName + '\'' + '\n' +
                "전화번호 = '" + phoneNumber + '\'' + '\n' +
                "이메일 = '" + email + '\'' + '\n' +
                "createdAt = " + createdAt + '\n' +
                "updatedAt = " + updatedAt + '\n';

    }


    public User updateUser(String displayName, String email, String phoneNumber) {
        this.displayName = displayName;
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
        this.phoneNumber = phoneNumber;
        return this;
    }
}