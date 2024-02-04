package com.wulaizhi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wulaizhi.springbootinit.model.entity.InterfaceInfo;
import com.wulaizhi.springbootinit.model.entity.User;
import com.wulaizhi.springbootinit.model.entity.UserInterfaceInfo;
import com.wulaizhi.springbootinit.mapper.UserInterfaceInfoMapper;
import com.wulaizhi.springbootinit.service.InterfaceInfoService;
import com.wulaizhi.springbootinit.service.UserInterfaceInfoService;
import com.wulaizhi.springbootinit.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
* @author lenovo
* @description 针对表【user_interface_info.sql】的数据库操作Service实现
* @createDate 2024-01-27 12:44:44
*/

@DubboService
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{


    @Resource
    private UserService userService;
    @Resource
    private InterfaceInfoService interfaceInfoService;
    private static final Map<Long, Object> userIdLocks = new ConcurrentHashMap<>();
    @Override
    public Boolean invokeCount(long userId, long interfaceId) {
        Object userLock = userIdLocks.computeIfAbsent(userId, k -> new Object());
        synchronized (userLock) {
            UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("userId",userId ).eq("interfaceInfoId",interfaceId)
                    .ge("leftNum",1);
            updateWrapper.setSql( "leftNum = leftNum-1 , totalNum = totalNum +1" );
            return  this.update(updateWrapper);
        }
    }

    @Override
    public User invokeUserInfo(String accesskey) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accesskey",accesskey);
        return userService.getOne(userQueryWrapper);
    }

    @Override
    public InterfaceInfo invokeInterface(String path,String method) {
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        interfaceInfoQueryWrapper.eq("url",path);
        interfaceInfoQueryWrapper.eq("method",method);
        return interfaceInfoService.getOne(interfaceInfoQueryWrapper);
    }



}




