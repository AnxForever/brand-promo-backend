package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.entity.CartItem;
import com.brandpromo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ApiResponse<List<Map<String, Object>>> list() {
        return ApiResponse.ok(cartService.getCartItems());
    }

    @PostMapping
    public ApiResponse<CartItem> add(@RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = body.containsKey("quantity") ? Integer.parseInt(body.get("quantity").toString()) : 1;
        return ApiResponse.ok("Added to cart", cartService.addToCart(productId, quantity));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateQuantity(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        int quantity = Integer.parseInt(body.get("quantity").toString());
        cartService.updateQuantity(id, quantity);
        return ApiResponse.ok("Quantity updated", null);
    }

    @PutMapping("/{id}/checked")
    public ApiResponse<Void> updateChecked(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        int checked = Integer.parseInt(body.get("checked").toString());
        cartService.updateChecked(id, checked);
        return ApiResponse.ok("Checked updated", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> remove(@PathVariable Long id) {
        cartService.removeItem(id);
        return ApiResponse.ok("Removed from cart", null);
    }

    @DeleteMapping
    public ApiResponse<Void> clear() {
        cartService.clearCart();
        return ApiResponse.ok("Cart cleared", null);
    }

    @GetMapping("/count")
    public ApiResponse<Integer> count() {
        return ApiResponse.ok(cartService.getCartCount());
    }
}
