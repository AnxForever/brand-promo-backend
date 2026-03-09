package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.entity.Product;
import com.brandpromo.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ApiResponse<List<Product>> list() {
        return ApiResponse.ok(favoriteService.listMyFavorites());
    }

    @GetMapping("/check/{productId}")
    public ApiResponse<Map<String, Boolean>> check(@PathVariable Long productId) {
        return ApiResponse.ok(Map.of("favorited", favoriteService.isFavorite(productId)));
    }

    @PostMapping("/{productId}")
    public ApiResponse<Void> add(@PathVariable Long productId) {
        favoriteService.addFavorite(productId);
        return ApiResponse.ok("收藏成功", null);
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> remove(@PathVariable Long productId) {
        favoriteService.removeFavorite(productId);
        return ApiResponse.ok("已取消收藏", null);
    }
}
