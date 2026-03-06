package com.brandpromo.mapper;

import com.brandpromo.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CartItemMapper {

    List<Map<String, Object>> findByUserId(@Param("userId") Long userId);

    CartItem findById(@Param("id") Long id);

    CartItem findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    int insert(CartItem cartItem);

    int updateQuantity(@Param("id") Long id, @Param("quantity") int quantity);

    int updateChecked(@Param("id") Long id, @Param("checked") int checked);

    int deleteById(@Param("id") Long id);

    int deleteByUserId(@Param("userId") Long userId);

    int deleteCheckedByUserId(@Param("userId") Long userId);

    int countByUserId(@Param("userId") Long userId);
}
