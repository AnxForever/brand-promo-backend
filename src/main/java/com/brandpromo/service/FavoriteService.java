package com.brandpromo.service;

import com.brandpromo.entity.Product;
import com.brandpromo.entity.User;
import com.brandpromo.exception.BusinessException;
import com.brandpromo.mapper.ProductMapper;
import com.brandpromo.mapper.UserFavoriteMapper;
import com.brandpromo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserFavoriteMapper userFavoriteMapper;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    private Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new BusinessException("用户未登录");
        }

        User user = userMapper.findByUsername(auth.getName());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user.getId();
    }

    public void addFavorite(Long productId) {
        Product product = productMapper.findById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        userFavoriteMapper.insertIgnore(getCurrentUserId(), productId);
    }

    public void removeFavorite(Long productId) {
        userFavoriteMapper.deleteByUserAndProduct(getCurrentUserId(), productId);
    }

    public boolean isFavorite(Long productId) {
        return userFavoriteMapper.countByUserAndProduct(getCurrentUserId(), productId) > 0;
    }

    public List<Product> listMyFavorites() {
        return userFavoriteMapper.findProductsByUserId(getCurrentUserId());
    }
}
