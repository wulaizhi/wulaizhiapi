package com.wulaizhi.springbootinit.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 编辑请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfaceInfoEditRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}