package com.brandpromo.controller;

import com.brandpromo.dto.ApiResponse;
import com.brandpromo.service.BrowseHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/browse-history")
@RequiredArgsConstructor
public class BrowseHistoryController {

    private final BrowseHistoryService browseHistoryService;

    @PostMapping
    public ApiResponse<Void> record(@RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        browseHistoryService.record(productId);
        return ApiResponse.ok("Recorded", null);
    }
}
