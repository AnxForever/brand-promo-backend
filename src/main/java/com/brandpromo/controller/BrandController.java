package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.entity.Brand;
import com.brandpromo.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ApiResponse<List<Brand>> list() {
        return ApiResponse.ok(brandService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Brand> detail(@PathVariable Long id) {
        Brand brand = brandService.findById(id);
        if (brand == null) {
            return ApiResponse.error("Brand not found");
        }
        return ApiResponse.ok(brand);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Brand> create(@RequestBody Brand brand) {
        return ApiResponse.ok("Brand created", brandService.create(brand));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Brand> update(@PathVariable Long id, @RequestBody Brand brand) {
        return ApiResponse.ok("Brand updated", brandService.update(id, brand));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ApiResponse.ok("Brand deleted", null);
    }
}
