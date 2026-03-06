package com.brandpromo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Advertisement {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Long productId;
    private Long merchantId;
    private String position;    // banner, sidebar, popup
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;     // 0=pending, 1=active, 2=offline
    private LocalDateTime createdAt;
}
