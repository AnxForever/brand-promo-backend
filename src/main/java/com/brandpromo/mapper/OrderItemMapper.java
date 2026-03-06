package com.brandpromo.mapper;

import com.brandpromo.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    int insertBatch(@Param("items") List<OrderItem> items);

    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    /** 查询用户最近购买的商品 ID（从已支付/已完成订单中提取） */
    List<Long> findRecentProductIdsByUserId(@Param("userId") Long userId, @Param("limit") int limit);
}
