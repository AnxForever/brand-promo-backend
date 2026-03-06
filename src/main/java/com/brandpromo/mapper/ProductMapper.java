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
                          @Param("sort") String sort,
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

    /** 热门商品（按 salesCount + viewCount 排序） */
    List<Product> findPopular(@Param("limit") int limit);

    /** 指定分类下的商品（排除已有 ID） */
    List<Product> findByCategoryIn(@Param("categories") List<String> categories,
                                    @Param("excludeIds") List<Long> excludeIds,
                                    @Param("limit") int limit);

    /** 扣减库存 */
    int decrementStock(@Param("id") Long id, @Param("quantity") int quantity);
}
