package com.brandpromo.mapper;

import com.brandpromo.entity.ProductReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductReviewMapper {

    List<Map<String, Object>> findByProductId(@Param("productId") Long productId,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit);

    int countByProductId(@Param("productId") Long productId);

    int insert(ProductReview review);

    int deleteByProductId(@Param("productId") Long productId);
}
