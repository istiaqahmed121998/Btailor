CREATE TABLE profiles (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255),
                          email VARCHAR(100),
                          phone VARCHAR(20),
                          address VARCHAR(255),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);