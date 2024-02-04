package com.wulaizhi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wulaizhi.apiclientsdk.client.ApiClient;
import com.wulaizhi.springbootinit.annotation.AuthCheck;
import com.wulaizhi.springbootinit.common.*;
import com.wulaizhi.springbootinit.constant.UserConstant;
import com.wulaizhi.springbootinit.exception.BusinessException;
import com.wulaizhi.springbootinit.exception.ThrowUtils;
import com.wulaizhi.springbootinit.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.wulaizhi.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.wulaizhi.springbootinit.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.wulaizhi.springbootinit.model.dto.interfaceinfo.InterfaceInvokeRequest;
import com.wulaizhi.springbootinit.model.entity.InterfaceInfo;
import com.wulaizhi.springbootinit.model.entity.User;
import com.wulaizhi.springbootinit.model.enums.InterfaceInfoStatusEnum;
import com.wulaizhi.springbootinit.model.vo.InterfaceInfoVO;
import com.wulaizhi.springbootinit.service.InterfaceInfoService;
import com.wulaizhi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;

import static com.wulaizhi.springbootinit.model.enums.InterfaceInfoStatusEnum.OFFLINE;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo=new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!interfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo infoServiceById = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(infoServiceById == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(InterfaceInfoVO.objToVo(interfaceInfo));
    }

    @PostMapping("/search/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> searchInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                HttpServletRequest request) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listMyPostVOByPage(int current ,int pageSize,
                                                         HttpServletRequest request) {
        if (current<=0|pageSize<=0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        if (user==null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> postPage = interfaceInfoService.page(new Page<>(current, pageSize));
        return ResultUtils.success(postPage);
    }

    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request) {
        Long id = idRequest.getId();
        //非空判断
        if (id==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo==null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口不存在!");
        }
        //判断接口是否可用
        // TODO: 2024/1/25  
        //修改该接口status
        InterfaceInfo updateInterfaceInfo = new InterfaceInfo();
        updateInterfaceInfo.setId(id);
        InterfaceInfoStatusEnum online = InterfaceInfoStatusEnum.ONLINE;
        updateInterfaceInfo.setStatus(online.getValue());
        boolean b = interfaceInfoService.updateById(updateInterfaceInfo);
        return new BaseResponse(ErrorCode.SUCCESS.getCode(),b,ErrorCode.SUCCESS.getMessage());
    }

    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request) {
        Long id = idRequest.getId();
        //非空判断
        if (idRequest==null||id==null||id<0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo==null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口不存在!");
        }
        //判断接口是否可用
        // TODO: 2024/1/25  
        //修改该接口status
        InterfaceInfo updateInterfaceInfo = new InterfaceInfo();
        updateInterfaceInfo.setId(id);
        InterfaceInfoStatusEnum offline = OFFLINE;
        updateInterfaceInfo.setStatus(offline.getValue());
        boolean b = interfaceInfoService.updateById(updateInterfaceInfo);
        return new BaseResponse(ErrorCode.SUCCESS.getCode(),b,ErrorCode.SUCCESS.getMessage());
    }
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> interfaceInfoGetById(@RequestParam("id") long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfo);
    }
    @PostMapping("/invoke")
    public BaseResponse interfaceInvoke(@RequestBody InterfaceInvokeRequest interfaceInvokeRequest, HttpServletRequest request) throws InstantiationException, IllegalAccessException {
        Long id = interfaceInvokeRequest.getId();
        //非空判断
        if (interfaceInvokeRequest==null||id==null||id<0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo==null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口不存在!");
        }
        //判断接口是否可用
        if (interfaceInfo.getStatus()==OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口不可用!");
        }
        //调用接口
        User loginUser = userService.getLoginUser(request);
        if (loginUser==null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"未登录!");
        }
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        ApiClient apiClient = new ApiClient(accessKey,secretKey,"saltwulaizhi");
        apiClient.setUrl(interfaceInfo.getUrl());
//        String result;
//        if (Objects.equals(interfaceInfo.getName(), "getNameByJson")) {
//            com.wulaizhi.apiclientsdk.model.User user = JsonUtils.toJavaObject(interfaceInvokeRequest.getUserRequestParams(),com.wulaizhi.apiclientsdk.model.User.class);
//            result= apiClient.getNameByJson(user);
//        } else if (Objects.equals(interfaceInfo.getName(), "getNameByPost")) {
//            result=apiClient.getNameByPost(interfaceInvokeRequest.getUserRequestParams());
//        }else {
//           result=apiClient.getNameByGet(interfaceInvokeRequest.getUserRequestParams());
//        }
        Object result=null;
        try {
            Class<? extends ApiClient> aClass = apiClient.getClass();
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(interfaceInfo.getName())) {
                    result = method.invoke(apiClient, interfaceInvokeRequest.getUserRequestParams());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("调用失败!");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return new BaseResponse(ErrorCode.SUCCESS.getCode(),result,ErrorCode.SUCCESS.getMessage());
    }
}
