package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/api/products/{productId}/reviews")
    public ApiResponse<Map<String, Object>> listReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(reviewService.getProductReviews(productId, page, size));
    }

    @PostMapping("/api/products/{productId}/reviews")
    public ApiResponse<Object> createReview(
            @PathVariable Long productId,
            @RequestBody Map<String, Object> body) {
        Integer rating = (Integer) body.get("rating");
        String content = (String) body.get("content");
        Long orderId = body.get("orderId") != null ? Long.valueOf(body.get("orderId").toString()) : null;
        return ApiResponse.ok("Review created", reviewService.createReview(productId, rating, content, orderId));
    }
}
