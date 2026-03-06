package com.brandpromo.service;

import com.brandpromo.entity.CartItem;
import com.brandpromo.entity.User;
import com.brandpromo.mapper.CartItemMapper;
import com.brandpromo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemMapper cartItemMapper;
    private final UserMapper userMapper;

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userMapper.findByUsername(auth.getName());
        return user.getId();
    }

    public List<Map<String, Object>> getCartItems() {
        return cartItemMapper.findByUserId(getCurrentUserId());
    }

    public CartItem addToCart(Long productId, int quantity) {
        Long userId = getCurrentUserId();
        CartItem existing = cartItemMapper.findByUserAndProduct(userId, productId);
        if (existing != null) {
            cartItemMapper.updateQuantity(existing.getId(), existing.getQuantity() + quantity);
            return cartItemMapper.findById(existing.getId());
        }
        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setChecked(1);
        cartItemMapper.insert(item);
        return item;
    }

    public void updateQuantity(Long id, int quantity) {
        cartItemMapper.updateQuantity(id, quantity);
    }

    public void updateChecked(Long id, int checked) {
        cartItemMapper.updateChecked(id, checked);
    }

    public void removeItem(Long id) {
        cartItemMapper.deleteById(id);
    }

    public void clearCart() {
        cartItemMapper.deleteByUserId(getCurrentUserId());
    }

    public void clearCheckedItems() {
        cartItemMapper.deleteCheckedByUserId(getCurrentUserId());
    }

    public int getCartCount() {
        return cartItemMapper.countByUserId(getCurrentUserId());
    }
}
