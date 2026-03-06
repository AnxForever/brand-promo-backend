package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.entity.Product;
import com.brandpromo.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping
    public ApiResponse<List<Product>> list() {
        return ApiResponse.ok(recommendService.getRecommendations());
    }
}
