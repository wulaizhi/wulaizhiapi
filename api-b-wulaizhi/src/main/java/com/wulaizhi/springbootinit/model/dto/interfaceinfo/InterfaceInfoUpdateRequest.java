package com.wulaizhi.springbootinit.model.dto.interfaceinfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String url;

    private String requestHeader;

    private String responseHeader;

    private Integer status;

    private String method;

    private String requestParams;

    private Long userId;

    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}