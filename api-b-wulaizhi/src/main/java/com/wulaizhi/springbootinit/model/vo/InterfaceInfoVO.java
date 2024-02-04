package com.wulaizhi.springbootinit.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.google.gson.Gson;
import com.wulaizhi.springbootinit.model.entity.InterfaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 帖子视图
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfaceInfoVO implements Serializable {

    private final static Gson GSON = new Gson();

    private String name;

    private String description;

    private String url;

    private String requestHeader;

    private String responseHeader;

    private String requestParams;

    private String method;

    private Integer status;

    private Long userId;

    @TableLogic
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    public static InterfaceInfoVO objToVo(InterfaceInfo interfaceInfo) {
        if (interfaceInfo == null) {
            return null;
        }
        InterfaceInfoVO interfaceInfoVO=new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);

        return interfaceInfoVO;
    }
}
