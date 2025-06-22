CREATE TABLE IF NOT EXISTS carts
(
    id              BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL,
    date_created    DATE DEFAULT (CURDATE()) NOT NULL,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cart_items
(
    id              BIGINT     NOT NULL AUTO_INCREMENT,
    cart_id         BINARY(16) NOT NULL,
    product_id      BIGINT     NOT NULL,
    quantity        INT        NOT NULL DEFAULT 1,
    CONSTRAINT pk_cart_items PRIMARY KEY (id),
    CONSTRAINT uq_cart_items UNIQUE (cart_id, product_id),
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);