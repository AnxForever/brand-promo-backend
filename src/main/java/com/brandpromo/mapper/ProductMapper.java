package com.brandpromo.mapper;

import com.brandpromo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findAll(@Param("keyword") String keyword,
                          @Param("category") String category,
                          @Param("merchantId") Long merchantId,
                          @Param("offset") int offset,
                          @Param("limit") int limit);

    int countAll(@Param("keyword") String keyword,
                 @Param("category") String category,
                 @Param("merchantId") Long merchantId);

    Product findById(@Param("id") Long id);

    int insert(Product product);

    int update(Product product);

    int deleteById(@Param("id") Long id);

    List<String> findAllCategories();

    int incrementViewCount(@Param("id") Long id);
}
