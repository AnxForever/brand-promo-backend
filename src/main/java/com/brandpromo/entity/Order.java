package com.brandpromo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal payAmount;
    private Integer status;         // 0=待付款, 1=已付款, 2=已发货, 3=已完成, 4=已取消
    private String paymentMethod;   // ALIPAY, WECHAT, CARD
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Long couponId;
    private String remark;
    private LocalDateTime payTime;
    private LocalDateTime shipTime;
    private LocalDateTime finishTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
