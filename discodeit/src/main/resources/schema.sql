DROP TABLE IF EXISTS message_attachments CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS read_statuses CASCADE;
DROP TABLE IF EXISTS user_statuses CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS channels CASCADE;
DROP TABLE IF EXISTS binary_contents CASCADE;
DROP TYPE IF EXISTS channel_type;

CREATE TABLE binary_contents (
     id UUID PRIMARY KEY,
     created_at TIMESTAMPTZ NOT NULL DEFAULT Now(),
     file_name VARCHAR(255) NOT NULL,
     size BIGINT NOT NULL,
     content_type VARCHAR(100) NOT NULL
);
CREATE TABLE users (
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT Now(),
    updated_at TIMESTAMPTZ,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(60)  NOT NULL,
    profile_id UUID UNIQUE
);
CREATE TABLE user_statuses (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT Now(),
    updated_at TIMESTAMPTZ,
    user_id UUID NOT NULL UNIQUE NOT NULL DEFAULT Now(),
    last_active_at TIMESTAMPTZ,
    FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
);
CREATE TYPE channel_type AS ENUM ('PUBLIC', 'PRIVATE');
CREATE TABLE channels (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT Now(),
    updated_at TIMESTAMPTZ,
    name VARCHAR(100),
    description VARCHAR(500),
    type channel_type NOT NULL
);
CREATE TABLE messages (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT Now(),
    updated_at TIMESTAMPTZ,
    content TEXT,
    channel_id UUID NOT NULL,
    FOREIGN KEY (channel_id) REFERENCES channels (id)
        ON DELETE CASCADE,
    author_id UUID,
    FOREIGN KEY (author_id) REFERENCES users (id)
        ON DELETE SET NULL
);
CREATE TABLE read_statuses (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT Now(),
    updated_at TIMESTAMPTZ,
    user_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    last_read_at TIMESTAMPTZ NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
       ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channels (id)
        ON DELETE CASCADE,
    UNIQUE(user_id, channel_id)
);
CREATE TABLE message_attachments (
    message_id UUID NOT NULL,
    attachment_id UUID NOT NULL,
    PRIMARY KEY (message_id, attachment_id),
    FOREIGN KEY (message_id) REFERENCES messages (id)
        ON DELETE CASCADE,
    FOREIGN KEY (attachment_id) REFERENCES binary_contents (id)
        ON DELETE CASCADE
);
ALTER TABLE users
    ADD CONSTRAINT fk_user_binary_content
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL;

