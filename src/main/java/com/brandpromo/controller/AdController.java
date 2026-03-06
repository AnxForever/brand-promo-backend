package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.entity.Advertisement;
import com.brandpromo.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adService.findAll(merchantId, status, page, size));
    }

    @GetMapping("/active")
    public ApiResponse<List<Advertisement>> active() {
        return ApiResponse.ok(adService.findActive());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ApiResponse<Advertisement> detail(@PathVariable Long id) {
        Advertisement ad = adService.findById(id);
        if (ad == null) {
            return ApiResponse.error("Advertisement not found");
        }
        return ApiResponse.ok(ad);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ApiResponse<Advertisement> create(@RequestBody Advertisement ad) {
        return ApiResponse.ok("Ad created", adService.create(ad));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ApiResponse<Advertisement> update(@PathVariable Long id, @RequestBody Advertisement ad) {
        return ApiResponse.ok("Ad updated", adService.update(id, ad));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        adService.updateStatus(id, status);
        return ApiResponse.ok("Status updated", null);
    }
}
