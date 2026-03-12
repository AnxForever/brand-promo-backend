package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.entity.Product;
import com.brandpromo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(productService.findAll(keyword, category, merchantId, sort, page, size));
    }

    @GetMapping("/storefront")
    public ApiResponse<Map<String, Object>> storefront(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ApiResponse.ok(productService.findStorefront(keyword, category, sort, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> detail(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            return ApiResponse.error("Product not found");
        }
        return ApiResponse.ok(product);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ApiResponse<Product> create(@RequestBody Product product) {
        return ApiResponse.ok("Product created", productService.create(product));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ApiResponse<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return ApiResponse.ok("Product updated", productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.ok("Product deleted", null);
    }

    @GetMapping("/categories")
    public ApiResponse<List<String>> categories() {
        return ApiResponse.ok(productService.getCategories());
    }

    @GetMapping("/diag/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Map<String, Object>> diagStock() {
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("columnInfo", productService.diagStockColumn());
        result.put("insertTest", productService.diagInsertStock(42));
        return ApiResponse.ok(result);
    }
}
