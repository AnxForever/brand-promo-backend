package com.brandpromo.mapper;

import com.brandpromo.entity.Advertisement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdMapper {

    List<Advertisement> findAll(@Param("merchantId") Long merchantId,
                                @Param("status") Integer status,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    int countAll(@Param("merchantId") Long merchantId,
                 @Param("status") Integer status);

    Advertisement findById(@Param("id") Long id);

    List<Advertisement> findActive();

    int insert(Advertisement ad);

    int update(Advertisement ad);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int countTotal();
}
