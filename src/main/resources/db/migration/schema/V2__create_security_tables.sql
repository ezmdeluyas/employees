CREATE TABLE system_users (
    user_id VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE roles (
    user_id VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT uq_user_role UNIQUE (user_id, role),
    CONSTRAINT fk_user_role FOREIGN KEY (user_id)
        REFERENCES system_users (user_id)
        ON DELETE CASCADE
);
