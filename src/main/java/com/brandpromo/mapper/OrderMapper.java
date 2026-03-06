package com.brandpromo.mapper;

import com.brandpromo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    int insert(Order order);

    Order findById(@Param("id") Long id);

    Order findByOrderNo(@Param("orderNo") String orderNo);

    List<Order> findByUserId(@Param("userId") Long userId,
                             @Param("status") Integer status,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    int countByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    int updateStatus(@Param("id") Long id, @Param("status") int status);

    int updatePayment(@Param("id") Long id, @Param("status") int status, @Param("paymentMethod") String paymentMethod);

    int countAll();
}
