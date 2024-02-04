package com.wulaizhi.springbootinit.controller;


import com.wulaizhi.springbootinit.common.BaseResponse;
import com.wulaizhi.springbootinit.common.ErrorCode;
import com.wulaizhi.springbootinit.model.entity.InterfaceInfo;
import com.wulaizhi.springbootinit.model.vo.InterfaceInfoVO;
import com.wulaizhi.springbootinit.service.InterfaceInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    public BaseResponse<List> analysysInterfaceInvoke() {
        Map<String, Integer> stringIntegerMap = interfaceInfoService.analysisInterfaceInvoke(10);
        List<Integer> list = stringIntegerMap.values().stream().collect(Collectors.toList());
        return new BaseResponse(ErrorCode.SUCCESS.getCode(),list,ErrorCode.SUCCESS.getMessage());
    }

}
