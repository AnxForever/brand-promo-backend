package com.brandpromo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserCoupon {
    private Long id;
    private Long userId;
    private Long couponId;
    private Integer status;     // 0=未使用, 1=已使用, 2=已过期
    private Long orderId;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;
}
