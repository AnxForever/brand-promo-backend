package com.brandpromo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartItem {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Integer checked;    // 0=unchecked, 1=checked
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
