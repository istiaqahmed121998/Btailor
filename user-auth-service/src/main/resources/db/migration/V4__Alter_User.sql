ALTER TABLE users
    ADD COLUMN account_non_locked BOOLEAN DEFAULT TRUE,
    ADD COLUMN failed_attempts INT DEFAULT 0,
    ADD COLUMN lock_time TIMESTAMP,
    ADD COLUMN account_expiration_date TIMESTAMP,
    ADD COLUMN password_expiration_date TIMESTAMP;