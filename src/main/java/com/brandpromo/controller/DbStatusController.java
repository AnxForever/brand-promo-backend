package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/db-status")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DbStatusController {

    private final JdbcTemplate jdbc;

    private static final List<String> TABLES = List.of(
        "sys_user", "brand", "product_category", "product",
        "advertisement", "shopping_cart", "orders", "order_item",
        "coupon", "user_coupon", "product_review", "browse_history",
        "operation_log", "user_favorite"
    );

    @GetMapping
    public ApiResponse<Map<String, Object>> overview() {
        List<Map<String, Object>> tables = new ArrayList<>();
        for (String table : TABLES) {
            Map<String, Object> info = new LinkedHashMap<>();
            info.put("table", table);
            info.put("count", jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class));
            try {
                info.put("lastUpdated", jdbc.queryForObject(
                    "SELECT MAX(created_at) FROM " + table, String.class));
            } catch (Exception e) {
                info.put("lastUpdated", null);
            }
            tables.add(info);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tables", tables);
        result.put("serverTime", jdbc.queryForObject("SELECT NOW()", String.class));
        result.put("dbVersion", jdbc.queryForObject("SELECT version()", String.class));
        return ApiResponse.ok(result);
    }

    @GetMapping("/{table}")
    public ApiResponse<List<Map<String, Object>>> queryTable(
            @PathVariable String table,
            @RequestParam(defaultValue = "10") int limit) {
        if (!TABLES.contains(table)) {
            return ApiResponse.error("Invalid table name");
        }
        String sql = "SELECT * FROM " + table + " ORDER BY id DESC LIMIT ?";
        List<Map<String, Object>> rows = jdbc.queryForList(sql, limit);
        return ApiResponse.ok(rows);
    }
}
