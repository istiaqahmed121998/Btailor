CREATE TABLE inventory_items (
                                 id BIGSERIAL PRIMARY KEY,

                                 variant_sku VARCHAR(255) NOT NULL,
                                 available_quantity INT,
                                 reserved_quantity INT,
                                 last_updated TIMESTAMP,
                                 UNIQUE (variant_sku)
);