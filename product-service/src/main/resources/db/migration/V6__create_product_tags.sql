-- ========================
-- TABLE: product_tags (join table)
-- ========================
CREATE TABLE product_tags (
                              product_id BIGINT NOT NULL,
                              tag_id BIGINT NOT NULL,
                              PRIMARY KEY (product_id, tag_id),
                              CONSTRAINT fk_producttag_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
                              CONSTRAINT fk_producttag_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);