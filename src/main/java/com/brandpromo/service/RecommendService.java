package com.brandpromo.service;

import com.brandpromo.entity.Product;
import com.brandpromo.entity.User;
import com.brandpromo.mapper.BrowseHistoryMapper;
import com.brandpromo.mapper.OrderItemMapper;
import com.brandpromo.mapper.ProductMapper;
import com.brandpromo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 推荐算法服务
 * <p>
 * 策略：
 * 1. 已登录用户 → 基于浏览历史 + 购买历史的分类相似推荐 + 热门商品补充
 * 2. 匿名用户   → 纯热门商品排行
 */
@Service
@RequiredArgsConstructor
public class RecommendService {

    private final ProductMapper productMapper;
    private final BrowseHistoryMapper browseHistoryMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserMapper userMapper;

    private static final int RECOMMEND_LIMIT = 10;
    private static final int HISTORY_LOOK_BACK = 20;

    public List<Product> getRecommendations() {
        Long userId = tryGetCurrentUserId();

        if (userId != null) {
            return personalizedRecommend(userId);
        }
        return productMapper.findPopular(RECOMMEND_LIMIT);
    }

    /**
     * 个性化推荐：
     * 1. 合并浏览历史 + 购买历史的商品 ID → 提取对应分类
     * 2. 在这些分类中找同类商品（排除已浏览+已购买）
     * 3. 不足则用热门商品补齐
     */
    private List<Product> personalizedRecommend(Long userId) {
        // Step 1: 合并浏览 + 购买历史商品 ID
        List<Long> browseIds = browseHistoryMapper.findRecentProductIds(userId, HISTORY_LOOK_BACK);
        List<Long> purchaseIds = orderItemMapper.findRecentProductIdsByUserId(userId, HISTORY_LOOK_BACK);

        Set<Long> allHistoryIds = new LinkedHashSet<>(browseIds);
        allHistoryIds.addAll(purchaseIds);

        if (allHistoryIds.isEmpty()) {
            return productMapper.findPopular(RECOMMEND_LIMIT);
        }

        // Step 2: 获取历史商品的分类集合
        Set<String> categories = new LinkedHashSet<>();
        for (Long pid : allHistoryIds) {
            Product p = productMapper.findById(pid);
            if (p != null && p.getCategory() != null) {
                categories.add(p.getCategory());
            }
        }

        if (categories.isEmpty()) {
            return productMapper.findPopular(RECOMMEND_LIMIT);
        }

        // Step 3: 在这些分类中推荐（排除已浏览 + 已购买）
        List<Long> excludeIds = new ArrayList<>(allHistoryIds);
        List<Product> similar = productMapper.findByCategoryIn(
                new ArrayList<>(categories), excludeIds, RECOMMEND_LIMIT);

        // Step 4: 不足 RECOMMEND_LIMIT 则用热门补齐
        if (similar.size() < RECOMMEND_LIMIT) {
            Set<Long> existIds = similar.stream().map(Product::getId).collect(Collectors.toSet());
            existIds.addAll(allHistoryIds);
            List<Product> popular = productMapper.findPopular(RECOMMEND_LIMIT);
            for (Product p : popular) {
                if (similar.size() >= RECOMMEND_LIMIT) break;
                if (!existIds.contains(p.getId())) {
                    similar.add(p);
                    existIds.add(p.getId());
                }
            }
        }

        return similar;
    }

    private Long tryGetCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        try {
            User user = userMapper.findByUsername(auth.getName());
            return user != null ? user.getId() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
