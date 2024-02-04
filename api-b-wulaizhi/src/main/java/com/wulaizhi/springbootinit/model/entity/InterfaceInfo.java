package com.wulaizhi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName interface_info
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfo implements Serializable {
    private Long id;

    private String name;

    private String description;

    private String url;

    private String requestHeader;

    private String responseHeader;

    private Integer status;

    private String method;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private String requestParams;

    @TableLogic
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}