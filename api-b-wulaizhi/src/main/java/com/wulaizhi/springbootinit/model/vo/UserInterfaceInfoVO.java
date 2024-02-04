package com.wulaizhi.springbootinit.model.vo;
import com.wulaizhi.springbootinit.model.entity.UserInterfaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
@Data
public class UserInterfaceInfoVO implements Serializable {
    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常,1-禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0-未删除,1-已删除)
     */
    private Integer isDelete;
    private static final long serialVersionUID = 1L;
    public static UserInterfaceInfoVO objToVo(UserInterfaceInfo userinterfaceInfo) {
        if (userinterfaceInfo == null) {
            return null;
        }
        UserInterfaceInfoVO userinterfaceInfoVO=new UserInterfaceInfoVO();
        BeanUtils.copyProperties(userinterfaceInfo, userinterfaceInfoVO);

        return userinterfaceInfoVO;
    }
}
