package com.brandpromo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private Integer status;     // 0=pending, 1=active, 2=offline
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
