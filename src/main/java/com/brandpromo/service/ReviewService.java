package com.brandpromo.service;

import com.brandpromo.entity.ProductReview;
import com.brandpromo.entity.User;
import com.brandpromo.mapper.ProductReviewMapper;
import com.brandpromo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ProductReviewMapper reviewMapper;
    private final UserMapper userMapper;

    public Map<String, Object> getProductReviews(Long productId, int page, int size) {
        int offset = (page - 1) * size;
        List<Map<String, Object>> list = reviewMapper.findByProductId(productId, offset, size);
        int total = reviewMapper.countByProductId(productId);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public ProductReview createReview(Long productId, Integer rating, String content, Long orderId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userMapper.findByUsername(auth.getName());

        ProductReview review = new ProductReview();
        review.setUserId(user.getId());
        review.setProductId(productId);
        review.setOrderId(orderId);
        review.setRating(rating);
        review.setContent(content);
        review.setStatus(1); // auto-publish
        reviewMapper.insert(review);
        return review;
    }
}
