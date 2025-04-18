CREATE TABLE product_images (
                                id BIGSERIAL PRIMARY KEY,
                                image_url TEXT NOT NULL,
                                alt_text TEXT,
                                is_primary BOOLEAN DEFAULT FALSE,
                                product_id BIGINT NOT NULL,
                                CONSTRAINT fk_image_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
