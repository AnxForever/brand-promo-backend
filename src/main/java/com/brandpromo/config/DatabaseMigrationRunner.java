package com.brandpromo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Runs idempotent ALTER TABLE statements on startup to ensure
 * the production database has all required columns.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseMigrationRunner implements ApplicationRunner {

    private final JdbcTemplate jdbc;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Running database migrations...");
        try {
            // Product promo columns
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS promo_price DECIMAL(10,2)");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS promo_start_time TIMESTAMP");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS promo_end_time TIMESTAMP");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS promo_status INT DEFAULT 0");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS original_price DECIMAL(10,2)");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS sales_count INT DEFAULT 0");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS view_count INT DEFAULT 0");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS images TEXT");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS specs TEXT");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS brand_id BIGINT");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS category_id BIGINT");
            jdbc.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS merchant_id BIGINT");

            // Fill defaults
            jdbc.execute("UPDATE product SET promo_status = 0 WHERE promo_status IS NULL");
            jdbc.execute("UPDATE product SET original_price = price WHERE original_price IS NULL");
            jdbc.execute("UPDATE product SET sales_count = 0 WHERE sales_count IS NULL");
            jdbc.execute("UPDATE product SET view_count = 0 WHERE view_count IS NULL");

            log.info("Database migrations completed successfully.");
        } catch (Exception e) {
            log.warn("Database migration warning (non-fatal): {}", e.getMessage());
        }
    }
}
