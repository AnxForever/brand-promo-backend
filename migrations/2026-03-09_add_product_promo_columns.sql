ALTER TABLE product ADD COLUMN IF NOT EXISTS promo_price DECIMAL(10,2);
ALTER TABLE product ADD COLUMN IF NOT EXISTS promo_start_time TIMESTAMP;
ALTER TABLE product ADD COLUMN IF NOT EXISTS promo_end_time TIMESTAMP;
ALTER TABLE product ADD COLUMN IF NOT EXISTS promo_status INT;

ALTER TABLE product ALTER COLUMN promo_status SET DEFAULT 0;

UPDATE product
SET promo_status = 0
WHERE promo_status IS NULL;

ALTER TABLE product ALTER COLUMN promo_status SET NOT NULL;

UPDATE product
SET original_price = price
WHERE original_price IS NULL;
