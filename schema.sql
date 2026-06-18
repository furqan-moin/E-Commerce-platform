-- =====================================================
-- USERS
-- =====================================================

CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,

    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    last_name VARCHAR(255),

    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,

    phone_number VARCHAR(20),
    country_code VARCHAR(10) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    language VARCHAR(20) DEFAULT 'english',

    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP
);

-- Sample User

INSERT INTO users (
    first_name,
    last_name,
    email,
    password,
    country_code
)
VALUES (
    'Furqan',
    'Moin',
    'furqan.moin7@gmail.com',
    'password123',
    '+91'
);

-- =====================================================
-- ADDRESSES
-- =====================================================

CREATE TABLE addresses (
    address_id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    address_type VARCHAR(20) NOT NULL,

    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),

    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pin_code VARCHAR(20) NOT NULL,

    country VARCHAR(100) NOT NULL,

    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_address_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);

CREATE INDEX idx_addresses_user_id
ON addresses(user_id);

-- Sample Address

INSERT INTO addresses (
    user_id,
    address_type,
    address_line1,
    city,
    state,
    pin_code,
    country
)
VALUES (
    1,
    'SHIPPING',
    'Koramangala 5th Block',
    'Bangalore',
    'Karnataka',
    '560095',
    'India'
);

-- =====================================================
-- CATEGORIES
-- =====================================================

CREATE TABLE categories (
    category_id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,

    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO categories(name, description)
VALUES
('Electronics', 'Electronic devices and gadgets'),
('Fashion', 'Clothing and accessories'),
('Books', 'Books and educational materials');

-- =====================================================
-- PRODUCTS
-- =====================================================

CREATE TABLE products (
    product_id BIGSERIAL PRIMARY KEY,

    category_id BIGINT NOT NULL,

    name VARCHAR(255) NOT NULL,
    description TEXT,

    price DECIMAL(10,2) NOT NULL,

    stock_quantity INT NOT NULL DEFAULT 0,

    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES categories(category_id),

    CONSTRAINT chk_product_price
        CHECK(price >= 0),

    CONSTRAINT chk_stock_quantity
        CHECK(stock_quantity >= 0)
);

CREATE INDEX idx_products_category_id
ON products(category_id);


INSERT INTO products(
    category_id,
    name,
    description,
    price,
    stock_quantity
)
VALUES
(
    1,
    'iPhone 16 Pro',
    'Apple flagship smartphone',
    129999.00,
    50
),
(
    1,
    'MacBook Pro M4',
    'Apple laptop',
    199999.00,
    20
),
(
    2,
    'Nike Running Shoes',
    'Comfortable sports shoes',
    5999.00,
    100
),
(
    3,
    'Java Complete Reference',
    'Java programming book',
    899.00,
    40
);

-- =====================================================
-- CART
-- =====================================================

CREATE TABLE cart (
    cart_id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    CONSTRAINT uk_cart_user
        UNIQUE(user_id)
);

CREATE INDEX idx_cart_user_id
ON cart(user_id);


INSERT INTO cart(
    user_id,
    is_active
)
VALUES
(
    1,
    TRUE
);

-- =====================================================
-- CART ITEMS
-- =====================================================

CREATE TABLE cart_items (
    cart_item_id BIGSERIAL PRIMARY KEY,

    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,

    quantity INT NOT NULL DEFAULT 1,

    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_items_cart
        FOREIGN KEY (cart_id)
        REFERENCES cart(cart_id),

    CONSTRAINT fk_cart_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(product_id),

    CONSTRAINT uk_cart_product
        UNIQUE(cart_id, product_id),

    CONSTRAINT chk_cart_item_quantity
        CHECK(quantity > 0)
);

CREATE INDEX idx_cart_items_cart_id
ON cart_items(cart_id);

CREATE INDEX idx_cart_items_product_id
ON cart_items(product_id);


INSERT INTO cart_items (
    cart_id,
    product_id,
    quantity
)
VALUES
(
    1,
    1,
    1
),
(
    1,
    4,
    2
);

-- =====================================================
-- ORDERS
-- =====================================================

CREATE TABLE orders (
    order_id BIGSERIAL PRIMARY KEY,

    order_number VARCHAR(100) UNIQUE NOT NULL,

    user_id BIGINT NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'CREATED',

    payment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    total_amount DECIMAL(10,2) NOT NULL,

    shipping_address_id BIGINT,
    billing_address_id BIGINT,

    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    CONSTRAINT fk_orders_shipping_address
        FOREIGN KEY (shipping_address_id)
        REFERENCES addresses(address_id),

    CONSTRAINT fk_orders_billing_address
        FOREIGN KEY (billing_address_id)
        REFERENCES addresses(address_id)
);

CREATE INDEX idx_orders_user_id
ON orders(user_id);


INSERT INTO orders(
    order_number,
    user_id,
    status,
    payment_status,
    total_amount,
    shipping_address_id,
    billing_address_id
)
VALUES
(
    'ORD-20260610-0001',
    1,
    'CONFIRMED',
    'SUCCESS',
    131797.00,
    1,
    1
);

-- =====================================================
-- ORDER ITEMS
-- =====================================================

CREATE TABLE order_items (
    order_item_id BIGSERIAL PRIMARY KEY,

    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,

    quantity INT NOT NULL,

    price DECIMAL(10,2) NOT NULL,

    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders(order_id),

    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(product_id),

    CONSTRAINT chk_order_item_quantity
        CHECK(quantity > 0),

    CONSTRAINT chk_order_item_price
        CHECK(price >= 0)
);

CREATE INDEX idx_order_items_order_id
ON order_items(order_id);

CREATE INDEX idx_order_items_product_id
ON order_items(product_id);

INSERT INTO order_items(
    order_id,
    product_id,
    quantity,
    price
)
VALUES
(
    1,
    1,
    1,
    129999.00
),
(
    1,
    4,
    2,
    899.00
);

-- =====================================================
-- Verification Queries
-- =====================================================
--User Addresses
SELECT *
FROM addresses
WHERE user_id = 1;

--=====================================================
--User Cart

SELECT
    c.cart_id,
    p.name,
    ci.quantity,
    p.price
FROM cart c
JOIN cart_items ci
    ON c.cart_id = ci.cart_id
JOIN products p
    ON p.product_id = ci.product_id
WHERE c.user_id = 1;

--=====================================================
--User Orders

SELECT
    o.order_number,
    o.status,
    o.payment_status,
    o.total_amount
FROM orders o
WHERE o.user_id = 1;

--=====================================================
--Order Details

SELECT
    o.order_number,
    p.name,
    oi.quantity,
    oi.price
FROM orders o
JOIN order_items oi
    ON o.order_id = oi.order_id
JOIN products p
    ON p.product_id = oi.product_id
WHERE o.order_id = 1;

