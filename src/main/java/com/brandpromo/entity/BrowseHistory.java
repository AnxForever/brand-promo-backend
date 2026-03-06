package com.brandpromo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BrowseHistory {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;
}
