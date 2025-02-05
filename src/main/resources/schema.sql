CREATE TABLE currency
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency_code VARCHAR(3) UNIQUE NOT NULL,
    ch_name        VARCHAR(50)       NOT NULL,
    symbol        VARCHAR(10),
    exchange_rate DECIMAL(18, 6),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);