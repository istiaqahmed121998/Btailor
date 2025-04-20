CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          seller_id BIGINT NOT NULL,
                          category_id BIGINT,
                          CONSTRAINT fk_product_category
                              FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);
