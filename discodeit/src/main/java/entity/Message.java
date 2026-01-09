package entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private String message;
    private Long createdAt;
    private Long updatedAt;


    public UUID Message(Long createdAt, Long updatedA, String message) {
        this.id = id;
        Long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
        this.message = message;

        public UUID getId() {
            return id;
        }

        public Long getCreatedAt() {
            return now;
        }

        public Long getUpdatedAt() {
            return now;
        }
        public String getMessage;() {
            return message;
        }



    }
}

