package com.wulaizhi.springbootinit.model.dto.interfaceinfo;

import com.wulaizhi.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {
    private String searchText;

    private Long id;

    private String name;

    private String description;

    private String url;

    private String requestHeader;

    private String responseHeader;

    private String requestParams;

    private Integer status;

    private String method;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}