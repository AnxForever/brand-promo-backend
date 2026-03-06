package com.brandpromo.mapper;

import com.brandpromo.entity.BrowseHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BrowseHistoryMapper {

    int upsert(@Param("userId") Long userId, @Param("productId") Long productId);
}
