package com.wulaizhi.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wulaizhi.springbootinit.model.entity.InterfaceInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author lenovo
* @description 针对表【interface_info】的数据库操作Mapper
* @createDate 2024-01-15 15:50:50
* @Entity generator.domain.InterfaceInfo
*/
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {
    @MapKey("_name")
    Map<String,Integer> analysisInterfaceInvoke(@Param("limit") Integer limit);
}




