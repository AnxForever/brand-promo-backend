package com.brandpromo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperationLog {
    private Long id;
    private Long userId;
    private String action;
    private String targetType;
    private Long targetId;
    private String ipAddress;
    private LocalDateTime createdAt;
}
