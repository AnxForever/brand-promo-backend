package com.brandpromo.service;

import com.brandpromo.entity.*;
import com.brandpromo.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    private Long getCurrentUserId() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        User user = userMapper.findByUsername(auth.getName());
        return user.getId();
    }

    @Transactional
    public Order createOrder(String receiverName, String receiverPhone, String receiverAddress,
                             String paymentMethod, Long couponId, String remark) {
        Long userId = getCurrentUserId();
        List<Map<String, Object>> cartItems = cartItemMapper.findByUserId(userId);

        List<Map<String, Object>> checkedItems = cartItems.stream()
                .filter(item -> Integer.valueOf(1).equals(item.get("checked")))
                .toList();

        if (checkedItems.isEmpty()) {
            throw new RuntimeException("购物车中没有选中的商品");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (Map<String, Object> item : checkedItems) {
            OrderItem oi = new OrderItem();
            oi.setProductId(Long.valueOf(item.get("productId").toString()));
            oi.setProductName(item.get("productName").toString());
            oi.setProductImage(item.get("imageUrl") != null ? item.get("imageUrl").toString() : null);
            oi.setPrice(new BigDecimal(item.get("price").toString()));
            oi.setQuantity(Integer.parseInt(item.get("quantity").toString()));
            oi.setSubtotal(new BigDecimal(item.get("subtotal").toString()));
            orderItems.add(oi);
            totalAmount = totalAmount.add(oi.getSubtotal());
        }

        BigDecimal discountAmount = BigDecimal.ZERO;
        UserCoupon usedUserCoupon = null;

        if (couponId != null) {
            // 1. 查询优惠券
            Coupon coupon = couponMapper.findById(couponId);
            if (coupon == null || coupon.getStatus() != 1) {
                throw new RuntimeException("优惠券不存在或已下架");
            }
            // 2. 验证用户是否已领取且未使用
            UserCoupon uc = userCouponMapper.findByUserAndCoupon(userId, couponId);
            if (uc == null) {
                throw new RuntimeException("您尚未领取该优惠券");
            }
            if (uc.getStatus() != 0) {
                throw new RuntimeException("优惠券已使用或已过期");
            }
            // 3. 验证是否过期
            if (coupon.getEndTime() != null && coupon.getEndTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("优惠券已过期");
            }
            // 4. 验证门槛
            if (coupon.getThreshold() != null && totalAmount.compareTo(coupon.getThreshold()) < 0) {
                throw new RuntimeException("订单金额未达到优惠券使用门槛（满" + coupon.getThreshold() + "可用）");
            }
            // 5. 按类型计算折扣
            if (coupon.getType() == null || coupon.getType() == 1) {
                // 满减：直接减 discount
                discountAmount = coupon.getDiscount();
            } else if (coupon.getType() == 2) {
                // 折扣：discount 表示折扣率（如 8.5 = 85折），payAmount = total * discount / 10
                discountAmount = totalAmount.subtract(
                        totalAmount.multiply(coupon.getDiscount()).divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP));
            } else if (coupon.getType() == 3) {
                // 直减
                discountAmount = coupon.getDiscount();
            }
            // 确保折扣不超过总额
            if (discountAmount.compareTo(totalAmount) > 0) {
                discountAmount = totalAmount;
            }
            usedUserCoupon = uc;
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setPayAmount(totalAmount.subtract(discountAmount));
        order.setStatus(0); // PENDING
        order.setPaymentMethod(paymentMethod);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        order.setCouponId(couponId);
        order.setRemark(remark);
        orderMapper.insert(order);

        for (OrderItem oi : orderItems) {
            oi.setOrderId(order.getId());
        }
        orderItemMapper.insertBatch(orderItems);

        // 扣减库存 & 增加销量
        for (OrderItem oi : orderItems) {
            int rows = productMapper.decrementStock(oi.getProductId(), oi.getQuantity());
            if (rows == 0) {
                throw new RuntimeException("商品「" + oi.getProductName() + "」库存不足");
            }
        }

        // 清除已选中的购物车项
        cartItemMapper.deleteCheckedByUserId(userId);

        // 标记优惠券已使用
        if (usedUserCoupon != null) {
            userCouponMapper.markUsed(usedUserCoupon.getId(), order.getId());
        }

        return order;
    }

    public Map<String, Object> getMyOrders(Integer status, int page, int size) {
        Long userId = getCurrentUserId();
        int offset = (page - 1) * size;
        List<Order> orders = orderMapper.findByUserId(userId, status, offset, size);
        int total = orderMapper.countByUserId(userId, status);

        List<Map<String, Object>> list = new ArrayList<>();
        for (Order o : orders) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("order", o);
            map.put("items", orderItemMapper.findByOrderId(o.getId()));
            list.add(map);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public Map<String, Object> getOrderDetail(Long id) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("order", order);
        result.put("items", orderItemMapper.findByOrderId(id));
        return result;
    }

    @Transactional
    public void payOrder(Long id, String paymentMethod) {
        Order order = orderMapper.findById(id);
        if (order == null || order.getStatus() != 0) {
            throw new RuntimeException("订单不可支付");
        }
        orderMapper.updatePayment(id, 1, paymentMethod);
    }

    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderMapper.findById(id);
        if (order == null || order.getStatus() != 0) {
            throw new RuntimeException("仅待付款订单可取消");
        }
        orderMapper.updateStatus(id, 4);

        // 恢复库存
        List<OrderItem> items = orderItemMapper.findByOrderId(id);
        for (OrderItem oi : items) {
            productMapper.decrementStock(oi.getProductId(), -oi.getQuantity());
        }

        // 退还优惠券
        if (order.getCouponId() != null) {
            userCouponMapper.resetByOrderId(id);
        }
    }

    public void shipOrder(Long id) {
        Order order = orderMapper.findById(id);
        if (order == null || order.getStatus() != 1) {
            throw new RuntimeException("仅已付款订单可发货");
        }
        orderMapper.updateStatus(id, 2);
    }

    public void completeOrder(Long id) {
        Order order = orderMapper.findById(id);
        if (order == null || order.getStatus() != 2) {
            throw new RuntimeException("仅已发货订单可确认收货");
        }
        orderMapper.updateStatus(id, 3);
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "ORD" + timestamp + random;
    }
}
