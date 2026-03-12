package com.brandpromo.mapper;

import com.brandpromo.entity.BrowseHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BrowseHistoryMapper {

    int upsert(@Param("userId") Long userId, @Param("productId") Long productId);

    int deleteByProductId(@Param("productId") Long productId);

    /** 最近浏览的商品 ID 列表 */
    List<Long> findRecentProductIds(@Param("userId") Long userId, @Param("limit") int limit);
}
