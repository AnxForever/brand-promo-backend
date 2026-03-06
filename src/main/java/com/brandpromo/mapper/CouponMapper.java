package com.brandpromo.mapper;

import com.brandpromo.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CouponMapper {

    List<Coupon> findAll();

    List<Coupon> findActive();

    Coupon findById(@Param("id") Long id);

    int insert(Coupon coupon);

    int update(Coupon coupon);

    int deleteById(@Param("id") Long id);

    int incrementUsedCount(@Param("id") Long id);
}
