package com.brandpromo.mapper;

import com.brandpromo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFavoriteMapper {

    int insertIgnore(@Param("userId") Long userId, @Param("productId") Long productId);

    int deleteByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    int deleteByProductId(@Param("productId") Long productId);

    int countByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    List<Product> findProductsByUserId(@Param("userId") Long userId);
}
