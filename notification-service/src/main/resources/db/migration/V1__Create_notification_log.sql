CREATE TABLE IF NOT EXISTS notification_logs (
                                                 id SERIAL PRIMARY KEY,
                                                 user_id BIGINT,
                                                 type VARCHAR(50),
                                                 recipient VARCHAR(255),
                                                 subject VARCHAR(255),
                                                 status VARCHAR(50),
                                                 sent_at TIMESTAMP,
                                                 kafka_partition INT,
                                                 kafka_offset BIGINT,
                                                 event_type VARCHAR(50),
                                                 triggered_by VARCHAR(100)
);
