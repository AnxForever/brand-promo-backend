package com.brandpromo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductCategory {
    private Long id;
    private String name;
    private Long parentId;
    private Integer sortOrder;
    private String icon;
    private Integer status;
    private LocalDateTime createdAt;
}
