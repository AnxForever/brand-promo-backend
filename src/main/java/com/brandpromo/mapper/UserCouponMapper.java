package com.brandpromo.mapper;

import com.brandpromo.entity.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserCouponMapper {

    List<Map<String, Object>> findByUserId(@Param("userId") Long userId);

    UserCoupon findByUserAndCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);

    int insert(UserCoupon userCoupon);

    int markUsed(@Param("id") Long id, @Param("orderId") Long orderId);

    /** 退还优惠券（取消订单时） */
    int resetByOrderId(@Param("orderId") Long orderId);
}
