package com.brandpromo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String name;
    private Long brandId;
    private String category;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private Integer status;     // 0=off-shelf, 1=on-shelf
    private Long merchantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
