package com.brandpromo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String role;        // ADMIN, MERCHANT, USER
    private String nickname;
    private String avatar;
    private Integer status;     // 0=disabled, 1=enabled
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
