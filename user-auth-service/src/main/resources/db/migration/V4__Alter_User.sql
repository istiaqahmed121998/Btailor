ALTER TABLE users
    ADD COLUMN account_enable BOOLEAN default True,
    ADD COLUMN account_expiration_date TIMESTAMP NULL,
    ADD COLUMN password_expiration_date TIMESTAMP NULL