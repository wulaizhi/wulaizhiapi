package com.wulaizhi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wulaizhi.springbootinit.common.ErrorCode;
import com.wulaizhi.springbootinit.constant.CommonConstant;
import com.wulaizhi.springbootinit.exception.BusinessException;
import com.wulaizhi.springbootinit.exception.ThrowUtils;
import com.wulaizhi.springbootinit.mapper.UserInterfaceInfoMapper;
import com.wulaizhi.springbootinit.model.entity.InterfaceInfo;
import com.wulaizhi.springbootinit.utils.SqlUtils;
import com.wulaizhi.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.wulaizhi.springbootinit.model.entity.*;
import com.wulaizhi.springbootinit.mapper.InterfaceInfoMapper;
import com.wulaizhi.springbootinit.model.vo.InterfaceInfoVO;
import com.wulaizhi.springbootinit.service.InterfaceInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author lenovo
* @description 针对表【interface_info】的数据库操作Service实现
* @createDate 2024-01-15 15:50:50
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        Integer status = interfaceInfo.getStatus();
        String method = interfaceInfo.getMethod();
        // 创建时，参数不能为空
//        if (add) {
//            if (status==null) {
//                throw  new BusinessException(ErrorCode.PARAMS_ERROR);
//            }
//        }
//        if (add) {
//            ThrowUtils.throwIf(StringUtils.isAnyBlank(name,description,url,method), ErrorCode.PARAMS_ERROR);
//        }
    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        String searchText = interfaceInfoQueryRequest.getSearchText();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("description", searchText);
        }
        queryWrapper.eq("isDelete", 0);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
        List<InterfaceInfo> interfaceInfoList =interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        if (CollectionUtils.isEmpty(interfaceInfoList)) {
            return interfaceInfoVOPage;
        }
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map((item) -> {
            InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(item);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        // 填充信息
        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }

    @Override
    public Map<String, Integer> analysisInterfaceInvoke(Integer limit) {
        if (limit==null||limit==0) {
            throw new  BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return  interfaceInfoMapper.analysisInterfaceInvoke(limit);
    }

}




