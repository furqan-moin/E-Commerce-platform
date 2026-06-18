-- Create Users Table
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP(0),
    updated_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    phone_number VARCHAR(20),
    country_code VARCHAR(10) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    language VARCHAR(20) DEFAULT 'english'
);

-- Insert user query
INSERT INTO users(firstName, lastName, email, password, country_code)
VALUES
('Furqan', 'Moin', 'furqan.moin7@gmail.com', 'password123', '+91');


-- Create addresses Table

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

-- Insert address query
INSERT INTO addresses (
    address_line_1,
    city,
    state,
    pincode,
    country,
    country_code,
    address_type,
    user_id
)
VALUES (
    'Koramangala 5th Block',
    'Bangalore',
    'Karnataka',
    '560095',
    'India',
    'IN',
    'SHIPPING',
    1
);


-- Create Categories

CREATE TABLE categories (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP
);


-- Create Products Table

CREATE TABLE products (
    product_id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES categories(category_id)
);
CREATE INDEX idx_products_category_id ON products(category_id);