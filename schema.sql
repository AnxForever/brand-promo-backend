-- Brand Promotion System — PostgreSQL Schema

CREATE TABLE IF NOT EXISTS sys_user (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
    nickname      VARCHAR(50),
    avatar        VARCHAR(500),
    status        INT          NOT NULL DEFAULT 1,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS brand (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    logo_url      VARCHAR(500),
    description   TEXT,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS product_category (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    parent_id     BIGINT       DEFAULT 0,
    sort_order    INT          DEFAULT 0,
    icon          VARCHAR(255),
    status        INT          DEFAULT 1,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS product (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    brand_id        BIGINT       REFERENCES brand(id),
    category_id     BIGINT       REFERENCES product_category(id),
    category        VARCHAR(100),
    price           DECIMAL(10,2) NOT NULL,
    original_price  DECIMAL(10,2),
    stock           INT          DEFAULT 0,
    description     TEXT,
    image_url       VARCHAR(500),
    images          TEXT,
    specs           TEXT,
    status          INT          NOT NULL DEFAULT 1,
    merchant_id     BIGINT,
    sales_count     INT          DEFAULT 0,
    view_count      INT          DEFAULT 0,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS advertisement (
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    image_url     VARCHAR(500),
    link_url      VARCHAR(500),
    product_id    BIGINT       REFERENCES product(id),
    merchant_id   BIGINT,
    position      VARCHAR(50),
    start_time    TIMESTAMP,
    end_time      TIMESTAMP,
    status        INT          NOT NULL DEFAULT 0,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS shopping_cart (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    product_id    BIGINT       NOT NULL REFERENCES product(id),
    quantity      INT          NOT NULL DEFAULT 1,
    checked       INT          NOT NULL DEFAULT 1,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS orders (
    id                BIGSERIAL PRIMARY KEY,
    order_no          VARCHAR(64)   NOT NULL UNIQUE,
    user_id           BIGINT        NOT NULL,
    total_amount      DECIMAL(10,2) NOT NULL,
    discount_amount   DECIMAL(10,2) DEFAULT 0,
    pay_amount        DECIMAL(10,2) NOT NULL,
    status            INT           NOT NULL DEFAULT 0,
    payment_method    VARCHAR(20),
    receiver_name     VARCHAR(50),
    receiver_phone    VARCHAR(20),
    receiver_address  VARCHAR(500),
    coupon_id         BIGINT,
    remark            VARCHAR(500),
    pay_time          TIMESTAMP,
    ship_time         TIMESTAMP,
    finish_time       TIMESTAMP,
    created_at        TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS order_item (
    id              BIGSERIAL PRIMARY KEY,
    order_id        BIGINT        NOT NULL REFERENCES orders(id),
    product_id      BIGINT        NOT NULL,
    product_name    VARCHAR(200),
    product_image   VARCHAR(500),
    price           DECIMAL(10,2) NOT NULL,
    quantity        INT           NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS coupon (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    type          INT          NOT NULL DEFAULT 1,
    threshold     DECIMAL(10,2) DEFAULT 0,
    discount      DECIMAL(10,2) NOT NULL,
    total_count   INT          DEFAULT 0,
    used_count    INT          DEFAULT 0,
    start_time    TIMESTAMP,
    end_time      TIMESTAMP,
    status        INT          NOT NULL DEFAULT 1,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS user_coupon (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    coupon_id     BIGINT       NOT NULL REFERENCES coupon(id),
    status        INT          NOT NULL DEFAULT 0,
    order_id      BIGINT,
    used_at       TIMESTAMP,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS product_review (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    product_id    BIGINT       NOT NULL REFERENCES product(id),
    order_id      BIGINT,
    rating        INT          NOT NULL DEFAULT 5,
    content       TEXT,
    images        TEXT,
    status        INT          NOT NULL DEFAULT 0,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS browse_history (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    product_id    BIGINT       NOT NULL REFERENCES product(id),
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, product_id)
);

CREATE TABLE IF NOT EXISTS operation_log (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT,
    action        VARCHAR(100) NOT NULL,
    target_type   VARCHAR(50),
    target_id     BIGINT,
    ip_address    VARCHAR(50),
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS user_favorite (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    product_id    BIGINT       NOT NULL REFERENCES product(id),
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, product_id)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_product_brand      ON product(brand_id);
CREATE INDEX IF NOT EXISTS idx_product_category    ON product(category_id);
CREATE INDEX IF NOT EXISTS idx_product_merchant    ON product(merchant_id);
CREATE INDEX IF NOT EXISTS idx_cart_user           ON shopping_cart(user_id);
CREATE INDEX IF NOT EXISTS idx_order_user          ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_order_item_order    ON order_item(order_id);
CREATE INDEX IF NOT EXISTS idx_user_coupon_user    ON user_coupon(user_id);
CREATE INDEX IF NOT EXISTS idx_review_product      ON product_review(product_id);
CREATE INDEX IF NOT EXISTS idx_browse_user         ON browse_history(user_id);
CREATE INDEX IF NOT EXISTS idx_oplog_created       ON operation_log(created_at);

-- Seed admin user (password: admin123, BCrypt encoded)
INSERT INTO sys_user (username, password, role, nickname)
VALUES ('admin', '$2a$10$1OhHVXwzgZ30F1Pa1LIryuL0pnarVZMHmWGKrh71tbYuRZNcLxI9.', 'ADMIN', 'Administrator')
ON CONFLICT (username) DO NOTHING;
