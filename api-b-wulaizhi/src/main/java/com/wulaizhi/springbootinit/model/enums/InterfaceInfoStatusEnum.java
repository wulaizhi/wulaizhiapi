package com.wulaizhi.springbootinit.model.enums;

import org.apache.commons.lang3.ObjectUtils;
public enum InterfaceInfoStatusEnum {
    OFFLINE("下线",0),
    ONLINE("上线",1);
    String text;

    int value;

    InterfaceInfoStatusEnum(String text, int value) {
        this.text=text;
        this.value=value;
    }
    public static InterfaceInfoStatusEnum getEnumByValue(int value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (InterfaceInfoStatusEnum anEnum : InterfaceInfoStatusEnum.values()) {
            if (anEnum.value==value) {
                return anEnum;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
