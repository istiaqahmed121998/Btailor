CREATE TABLE payment_transaction (
                                     id                BIGSERIAL PRIMARY KEY,
                                     order_id          VARCHAR(50) NOT NULL,
                                     user_id           BIGINT      NOT NULL,
                                     amount_cents      NUMERIC(12, 2) NOT NULL,
                                     method            VARCHAR(20) NOT NULL,
                                     success           BOOLEAN     NOT NULL,
                                     transaction_id    VARCHAR(100),
                                     failure_reason    TEXT,
                                     created_at        TIMESTAMP   NOT NULL DEFAULT now()
);
