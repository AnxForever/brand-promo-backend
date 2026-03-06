package com.brandpromo.service;

import com.brandpromo.entity.Coupon;
import com.brandpromo.entity.User;
import com.brandpromo.entity.UserCoupon;
import com.brandpromo.mapper.CouponMapper;
import com.brandpromo.mapper.UserCouponMapper;
import com.brandpromo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;
    private final UserMapper userMapper;

    private Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userMapper.findByUsername(auth.getName());
        return user.getId();
    }

    public List<Coupon> getAllCoupons() {
        return couponMapper.findAll();
    }

    /** 返回所有优惠券，字段名对齐前端（minAmount / total / remaining） */
    public List<Map<String, Object>> getAllCouponsMapped() {
        List<Coupon> all = couponMapper.findAll();
        return all.stream().map(this::mapCouponToFrontend).toList();
    }

    public List<Map<String, Object>> getAvailableCoupons() {
        List<Coupon> active = couponMapper.findActive();
        return active.stream().map(this::mapCouponToFrontend).toList();
    }

    /** Coupon 实体 → 前端字段名 */
    private Map<String, Object> mapCouponToFrontend(Coupon c) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", c.getId());
        map.put("name", c.getName());
        map.put("type", c.getType());
        map.put("discount", c.getDiscount());
        map.put("minAmount", c.getThreshold());
        map.put("total", c.getTotalCount());
        map.put("remaining", c.getTotalCount() != null && c.getTotalCount() > 0
                ? c.getTotalCount() - (c.getUsedCount() != null ? c.getUsedCount() : 0)
                : null);
        map.put("startTime", c.getStartTime());
        map.put("endTime", c.getEndTime());
        map.put("status", c.getStatus());
        return map;
    }

    public List<Map<String, Object>> getMyCoupons() {
        Long userId = getCurrentUserId();
        List<Map<String, Object>> list = userCouponMapper.findByUserId(userId);
        return list.stream().map(item -> {
            Map<String, Object> result = new LinkedHashMap<>(item);
            result.put("used", Integer.valueOf(1).equals(item.get("status")));
            return result;
        }).toList();
    }

    @Transactional
    public void claimCoupon(Long couponId) {
        Long userId = getCurrentUserId();

        Coupon coupon = couponMapper.findById(couponId);
        if (coupon == null || coupon.getStatus() != 1) {
            throw new RuntimeException("优惠券不存在或已下架");
        }
        if (coupon.getEndTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("优惠券已过期");
        }
        if (coupon.getTotalCount() > 0 && coupon.getUsedCount() >= coupon.getTotalCount()) {
            throw new RuntimeException("优惠券已领完");
        }
        if (userCouponMapper.findByUserAndCoupon(userId, couponId) != null) {
            throw new RuntimeException("您已领取过该优惠券");
        }

        UserCoupon uc = new UserCoupon();
        uc.setUserId(userId);
        uc.setCouponId(couponId);
        uc.setStatus(0);
        userCouponMapper.insert(uc);
        couponMapper.incrementUsedCount(couponId);
    }

    public Coupon createCoupon(Coupon coupon) {
        if (coupon.getStatus() == null) coupon.setStatus(1);
        if (coupon.getType() == null) coupon.setType(1);
        couponMapper.insert(coupon);
        return coupon;
    }

    public Coupon updateCoupon(Long id, Coupon coupon) {
        coupon.setId(id);
        couponMapper.update(coupon);
        return couponMapper.findById(id);
    }

    public void deleteCoupon(Long id) {
        couponMapper.deleteById(id);
    }
}
