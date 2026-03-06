package com.brandpromo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductReview {
    private Long id;
    private Long userId;
    private Long productId;
    private Long orderId;
    private Integer rating;     // 1-5
    private String content;
    private String images;      // JSON array
    private Integer status;     // 0=待审核, 1=已发布, 2=已隐藏
    private LocalDateTime createdAt;
}
