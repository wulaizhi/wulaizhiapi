package com.wulaizhi.springbootinit.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInvokeRequest implements Serializable {

    private long id;

    private String userRequestParams;

    private static final long serialVersionUID = 1L;

}