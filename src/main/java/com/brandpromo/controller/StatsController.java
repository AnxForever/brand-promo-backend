package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.mapper.AdMapper;
import com.brandpromo.mapper.OperationLogMapper;
import com.brandpromo.mapper.ProductMapper;
import com.brandpromo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StatsController {

    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final AdMapper adMapper;
    private final OperationLogMapper operationLogMapper;

    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> overview() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userMapper.countAll());
        stats.put("productCount", productMapper.countAll(null, null, null));
        stats.put("adCount", adMapper.countAll());
        return ApiResponse.ok(stats);
    }

    @GetMapping("/operations")
    public ApiResponse<List<Map<String, Object>>> operations() {
        return ApiResponse.ok(operationLogMapper.countByAction(30));
    }
}
