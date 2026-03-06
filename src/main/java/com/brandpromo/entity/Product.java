package com.brandpromo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String name;
    private Long brandId;
    private String brandName;   // 来自 JOIN brand 表
    private Long categoryId;
    private String category;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private String description;
    private String imageUrl;
    private String images;
    private String specs;
    private Integer status;     // 0=off-shelf, 1=on-shelf
    private Long merchantId;
    private Integer salesCount;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
