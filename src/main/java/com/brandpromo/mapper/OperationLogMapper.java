package com.brandpromo.mapper;

import com.brandpromo.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OperationLogMapper {

    int insert(OperationLog log);

    List<Map<String, Object>> countByAction(@Param("days") int days);
}
