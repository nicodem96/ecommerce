CREATE TABLE IF NOT EXISTS customer_order (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(255) NOT NULL UNIQUE,
    total_amount DECIMAL(38, 2),
    payment_method VARCHAR(255),
    customer_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer_line (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity DOUBLE,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES customer_order(id)
);