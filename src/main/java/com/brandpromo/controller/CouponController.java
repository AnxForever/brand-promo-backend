package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.entity.Coupon;
import com.brandpromo.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 所有优惠券（已映射为前端字段名） */
    @GetMapping
    public ApiResponse<List<Map<String, Object>>> list() {
        return ApiResponse.ok(couponService.getAllCouponsMapped());
    }

    /** 我的优惠券 */
    @GetMapping("/mine")
    public ApiResponse<List<Map<String, Object>>> mine() {
        return ApiResponse.ok(couponService.getMyCoupons());
    }

    /** 领取优惠券 */
    @PostMapping("/{id}/claim")
    public ApiResponse<Void> claim(@PathVariable Long id) {
        couponService.claimCoupon(id);
        return ApiResponse.ok("领取成功", null);
    }

    /** 新建优惠券（管理员） */
    @PostMapping
    public ApiResponse<Coupon> create(@RequestBody Map<String, Object> body) {
        Coupon coupon = mapFromRequest(body);
        return ApiResponse.ok(couponService.createCoupon(coupon));
    }

    /** 更新优惠券（管理员） */
    @PutMapping("/{id}")
    public ApiResponse<Coupon> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Coupon coupon = mapFromRequest(body);
        return ApiResponse.ok(couponService.updateCoupon(id, coupon));
    }

    /** 删除优惠券（管理员） */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ApiResponse.ok("删除成功", null);
    }

    /** 前端字段 → Coupon 实体映射 */
    private Coupon mapFromRequest(Map<String, Object> body) {
        Coupon c = new Coupon();
        c.setName((String) body.get("name"));
        if (body.get("discount") != null) {
            c.setDiscount(new BigDecimal(body.get("discount").toString()));
        }
        // 前端 minAmount → 后端 threshold
        if (body.get("minAmount") != null) {
            c.setThreshold(new BigDecimal(body.get("minAmount").toString()));
        }
        // 前端 total → 后端 totalCount
        if (body.get("total") != null) {
            c.setTotalCount(((Number) body.get("total")).intValue());
        }
        if (body.get("startTime") != null) {
            c.setStartTime(LocalDateTime.parse(body.get("startTime").toString(), FMT));
        }
        if (body.get("endTime") != null) {
            c.setEndTime(LocalDateTime.parse(body.get("endTime").toString(), FMT));
        }
        if (body.get("type") != null) {
            c.setType(((Number) body.get("type")).intValue());
        }
        if (body.get("status") != null) {
            c.setStatus(((Number) body.get("status")).intValue());
        }
        return c;
    }
}
