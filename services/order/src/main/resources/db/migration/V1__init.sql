CREATE TABLE customer_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(255),
    total_amount DECIMAL(19, 2),
    payment_method VARCHAR(50),
    customer_id VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);