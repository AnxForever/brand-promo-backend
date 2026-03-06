package com.brandpromo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Coupon {
    private Long id;
    private String name;
    private Integer type;           // 1=满减, 2=折扣, 3=直减
    private BigDecimal threshold;   // 使用门槛
    private BigDecimal discount;    // 优惠值
    private Integer totalCount;
    private Integer usedCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;         // 0=disabled, 1=enabled
    private LocalDateTime createdAt;
}
