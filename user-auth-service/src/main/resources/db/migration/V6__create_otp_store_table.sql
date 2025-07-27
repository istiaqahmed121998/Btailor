-- V2__create_otp_store_table.sql
-- The 'V2' prefix assumes this is your second migration script.
-- Flyway uses the version number to run migrations in order.

-- This table will store the OTP, its expiration, and link it to a user.
CREATE TABLE otp_store (
    -- A unique identifier for each OTP record.
    -- BIGSERIAL is the PostgreSQL equivalent of AUTO_INCREMENT for a BIGINT.
                           id BIGSERIAL NOT NULL,

    -- The OTP code itself. Stored as a string to accommodate various formats.
                           otp_code VARCHAR(10) NOT NULL,

    -- The exact date and time when this OTP will expire and become invalid.
    -- Using TIMESTAMP WITH TIME ZONE is best practice for handling time across servers.
                           expires_at TIMESTAMP WITH TIME ZONE NOT NULL,

    -- A foreign key linking this OTP to the user it was generated for.
    -- This assumes you have a 'users' table with an 'id' column.
                           user_id BIGINT NOT NULL,

    -- Sets the primary key for this table.
                           CONSTRAINT pk_otp_store PRIMARY KEY (id),

    -- Establishes the foreign key relationship to the users table.
    -- Ensures that if a user is deleted, their associated OTP records can be handled accordingly.
                           CONSTRAINT fk_otp_store_on_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Creating an index on the user_id column can speed up lookups
-- when you need to find an OTP for a specific user.
CREATE INDEX idx_otp_store_user_id ON otp_store(user_id);
