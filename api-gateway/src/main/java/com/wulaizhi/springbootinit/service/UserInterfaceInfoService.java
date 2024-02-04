package com.wulaizhi.springbootinit.service;

import com.wulaizhi.springbootinit.model.entity.User;
import com.wulaizhi.springbootinit.model.entity.InterfaceInfo;

/**
* @author lenovo
* @description 针对表【user_interface_info.sql】的数据库操作Service
* @createDate 2024-01-27 12:44:44
*/

public interface UserInterfaceInfoService  {
    public Boolean invokeCount(long userId,long interfaceId);
    User invokeUserInfo(String accesskey);
    InterfaceInfo invokeInterface(String path,String method);
}
