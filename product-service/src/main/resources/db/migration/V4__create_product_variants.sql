CREATE TABLE product_variants (
                                  id BIGSERIAL PRIMARY KEY,
                                  sku VARCHAR(255) UNIQUE NOT NULL,
                                  color VARCHAR(100),
                                  size VARCHAR(100),
                                  material VARCHAR(100),
                                  price NUMERIC(12, 2),
                                  inventory_id VARCHAR(255),
                                  product_id BIGINT NOT NULL,
                                  CONSTRAINT fk_variant_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
