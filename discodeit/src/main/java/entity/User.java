package entity;

import java.util.UUID;

public class User {
    private final UUID id;
    private String displayName;
    private String phoneNumber;
    private String email;
    private Long createdAt;
    private Long updatedAt;

    public User(String displayName, String phoneNumber, String email) {
        this.id = UUID.randomUUID();
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        long now = System.currentTimeMillis();
        this.updatedAt = now;
        this.email = email;
        this.createdAt = now;

    }

    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
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
    public User update(UUID id, String phoneNumber, String email) {

    }
//TODO
}