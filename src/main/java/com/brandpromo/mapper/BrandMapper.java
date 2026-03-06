package com.brandpromo.mapper;

import com.brandpromo.entity.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BrandMapper {

    List<Brand> findAll();

    Brand findById(@Param("id") Long id);

    int insert(Brand brand);

    int update(Brand brand);

    int deleteById(@Param("id") Long id);
}
