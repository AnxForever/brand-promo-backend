package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<Object> create(@RequestBody Map<String, Object> body) {
        String receiverName = (String) body.get("receiverName");
        String receiverPhone = (String) body.get("receiverPhone");
        String receiverAddress = (String) body.get("receiverAddress");
        String paymentMethod = (String) body.get("paymentMethod");
        Long couponId = body.get("couponId") != null ? Long.valueOf(body.get("couponId").toString()) : null;
        String remark = (String) body.get("remark");
        return ApiResponse.ok("Order created", orderService.createOrder(receiverName, receiverPhone, receiverAddress, paymentMethod, couponId, remark));
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(orderService.getOrders(status, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Long id) {
        return ApiResponse.ok(orderService.getOrderDetail(id));
    }

    @PostMapping("/{id}/pay")
    public ApiResponse<Void> pay(@PathVariable Long id, @RequestBody Map<String, String> body) {
        orderService.payOrder(id, body.getOrDefault("paymentMethod", "ALIPAY"));
        return ApiResponse.ok("Payment successful", null);
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ApiResponse.ok("Order cancelled", null);
    }

    @PutMapping("/{id}/ship")
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ApiResponse<Void> ship(@PathVariable Long id) {
        orderService.shipOrder(id);
        return ApiResponse.ok("Order shipped", null);
    }

    @PutMapping("/{id}/complete")
    public ApiResponse<Void> complete(@PathVariable Long id) {
        orderService.completeOrder(id);
        return ApiResponse.ok("Order completed", null);
    }
}
