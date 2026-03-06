package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.entity.ProductCategory;
import com.brandpromo.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService categoryService;

    /** 获取分类树 */
    @GetMapping
    public ApiResponse<List<Map<String, Object>>> tree() {
        return ApiResponse.ok(categoryService.getTree());
    }

    /** 获取扁平列表 */
    @GetMapping("/list")
    public ApiResponse<List<ProductCategory>> list() {
        return ApiResponse.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductCategory> detail(@PathVariable Long id) {
        return ApiResponse.ok(categoryService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductCategory> create(@RequestBody ProductCategory category) {
        return ApiResponse.ok("创建成功", categoryService.create(category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductCategory> update(@PathVariable Long id, @RequestBody ProductCategory category) {
        return ApiResponse.ok("更新成功", categoryService.update(id, category));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.ok("删除成功", null);
    }
}
